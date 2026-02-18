#!/bin/bash
#
# Response Time Check Script
# Validates that page response times meet SLA requirements
#
# Usage: ./check-response-times.sh <base-url>
#

set -e

BASE_URL="${1:-http://localhost:8080}"

# SLA thresholds (in seconds)
THRESHOLD_TTFB=0.5      # Time to first byte
THRESHOLD_TOTAL=3.0     # Total response time
THRESHOLD_CACHED=0.2    # Cached response time

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

PASS_COUNT=0
FAIL_COUNT=0

# Pages to test
declare -a PAGES=(
    "/content/bmad-showcase/en/home.html"
    "/content/bmad-showcase/en/about.html"
    "/content/bmad-showcase/en/products.html"
)

check_response_time() {
    local path="$1"
    local url="${BASE_URL}${path}"

    # Get timing metrics
    metrics=$(curl -s -o /dev/null -w "%{time_namelookup},%{time_connect},%{time_starttransfer},%{time_total},%{http_code}" "$url" 2>/dev/null)

    IFS=',' read -r dns connect ttfb total status <<< "$metrics"

    echo "  Page: $path"
    echo "    Status: $status"
    echo "    DNS Lookup: ${dns}s"
    echo "    Connect: ${connect}s"
    echo "    TTFB: ${ttfb}s"
    echo "    Total: ${total}s"

    # Check thresholds
    if [ "$status" != "200" ]; then
        echo -e "    ${RED}[FAIL]${NC} Non-200 status code"
        ((FAIL_COUNT++))
        return
    fi

    # Check TTFB
    if (( $(echo "$ttfb > $THRESHOLD_TTFB" | bc -l) )); then
        echo -e "    ${RED}[FAIL]${NC} TTFB exceeds threshold (${ttfb}s > ${THRESHOLD_TTFB}s)"
        ((FAIL_COUNT++))
    else
        echo -e "    ${GREEN}[PASS]${NC} TTFB within threshold"
        ((PASS_COUNT++))
    fi

    # Check total time
    if (( $(echo "$total > $THRESHOLD_TOTAL" | bc -l) )); then
        echo -e "    ${RED}[FAIL]${NC} Total time exceeds threshold (${total}s > ${THRESHOLD_TOTAL}s)"
        ((FAIL_COUNT++))
    else
        echo -e "    ${GREEN}[PASS]${NC} Total time within threshold"
        ((PASS_COUNT++))
    fi

    echo ""
}

check_cached_response() {
    local path="$1"
    local url="${BASE_URL}${path}"

    echo "  Cached Response Test: $path"

    # First request (prime cache)
    curl -s -o /dev/null "$url"

    # Second request (should be cached)
    metrics=$(curl -s -o /dev/null -w "%{time_total}" "$url" 2>/dev/null)

    echo "    Cached Response Time: ${metrics}s"

    if (( $(echo "$metrics > $THRESHOLD_CACHED" | bc -l) )); then
        echo -e "    ${YELLOW}[WARN]${NC} Cached response slower than expected"
    else
        echo -e "    ${GREEN}[PASS]${NC} Cached response fast"
        ((PASS_COUNT++))
    fi

    echo ""
}

echo "============================================"
echo "Response Time Check"
echo "Target: $BASE_URL"
echo "============================================"
echo ""
echo "Thresholds:"
echo "  TTFB: ${THRESHOLD_TTFB}s"
echo "  Total: ${THRESHOLD_TOTAL}s"
echo "  Cached: ${THRESHOLD_CACHED}s"
echo ""

echo "--- Initial Response Times ---"
for page in "${PAGES[@]}"; do
    check_response_time "$page"
done

echo "--- Cached Response Times ---"
for page in "${PAGES[@]}"; do
    check_cached_response "$page"
done

echo "--- Load Test (10 concurrent requests) ---"
echo "  Testing: ${PAGES[0]}"

# Simple load test with parallel curl requests
start_time=$(date +%s.%N)
for i in {1..10}; do
    curl -s -o /dev/null "${BASE_URL}${PAGES[0]}" &
done
wait
end_time=$(date +%s.%N)
duration=$(echo "$end_time - $start_time" | bc)

echo "  10 concurrent requests completed in ${duration}s"
avg_time=$(echo "scale=3; $duration / 10" | bc)
echo "  Average time per request: ${avg_time}s"

if (( $(echo "$avg_time > $THRESHOLD_TOTAL" | bc -l) )); then
    echo -e "  ${RED}[FAIL]${NC} Average time exceeds threshold under load"
    ((FAIL_COUNT++))
else
    echo -e "  ${GREEN}[PASS]${NC} Performance acceptable under load"
    ((PASS_COUNT++))
fi

echo ""
echo "============================================"
echo "Summary"
echo "============================================"
echo -e "${GREEN}Passed:${NC} $PASS_COUNT"
echo -e "${RED}Failed:${NC} $FAIL_COUNT"

if [ $FAIL_COUNT -gt 0 ]; then
    echo ""
    echo -e "${RED}FAIL: Performance thresholds not met${NC}"
    exit 1
else
    echo ""
    echo -e "${GREEN}All performance checks passed!${NC}"
    exit 0
fi
