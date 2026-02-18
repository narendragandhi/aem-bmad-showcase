#!/bin/bash
#
# Dispatcher Security Check Script
# Validates that sensitive AEM paths are properly blocked
#
# Usage: ./check-dispatcher-security.sh <base-url>
#

set -e

BASE_URL="${1:-http://localhost:8080}"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

PASS_COUNT=0
FAIL_COUNT=0
WARN_COUNT=0

check_blocked() {
    local path="$1"
    local description="$2"

    status=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}${path}" 2>/dev/null || echo "000")

    if [ "$status" = "403" ] || [ "$status" = "404" ]; then
        echo -e "${GREEN}[PASS]${NC} $description ($path) - Blocked ($status)"
        ((PASS_COUNT++))
    else
        echo -e "${RED}[FAIL]${NC} $description ($path) - Accessible ($status)"
        ((FAIL_COUNT++))
    fi
}

check_header() {
    local header="$1"
    local path="${2:-/content/bmad-showcase/en/home.html}"

    response=$(curl -sI "${BASE_URL}${path}" 2>/dev/null)

    if echo "$response" | grep -qi "$header"; then
        value=$(echo "$response" | grep -i "$header" | head -1 | cut -d: -f2- | tr -d '\r')
        echo -e "${GREEN}[PASS]${NC} Header: $header =$value"
        ((PASS_COUNT++))
    else
        echo -e "${RED}[FAIL]${NC} Header: $header - Missing"
        ((FAIL_COUNT++))
    fi
}

echo "============================================"
echo "Dispatcher Security Check"
echo "Target: $BASE_URL"
echo "============================================"
echo ""

# Sensitive Path Checks
echo "--- Sensitive Path Blocking ---"
check_blocked "/crx/de/index.jsp" "CRXDE Lite"
check_blocked "/crx/explorer/browser/index.jsp" "CRX Explorer"
check_blocked "/crx/packmgr/index.jsp" "Package Manager"
check_blocked "/system/console" "OSGi Console"
check_blocked "/system/console/bundles" "Bundle Console"
check_blocked "/system/console/configMgr" "Config Manager"
check_blocked "/bin/querybuilder.json" "Query Builder"
check_blocked "/bin/querybuilder.feed" "Query Builder Feed"
check_blocked "/etc/replication/agents.author.html" "Replication Agents"
check_blocked "/etc/packages.html" "Packages Console"
check_blocked "/libs/granite/security/currentuser.json" "Current User Info"
check_blocked "/libs/granite/security/content/userproperties.html" "User Properties"
check_blocked "/libs/cq/security/content/aclsetup.html" "ACL Setup"
check_blocked "/admin" "Admin Path"
check_blocked "/apps" "Apps Folder"
check_blocked "/libs" "Libs Folder"
check_blocked "/.json" "Root JSON"

echo ""
echo "--- Dangerous Selector/Extension Blocking ---"
check_blocked "/content/bmad-showcase.infinity.json" "Infinity JSON"
check_blocked "/content/bmad-showcase.-1.json" "Negative Depth JSON"
check_blocked "/content/bmad-showcase.tidy.json" "Tidy JSON"
check_blocked "/content/bmad-showcase.sysview.xml" "Sysview XML"
check_blocked "/content/bmad-showcase.docview.json" "Docview JSON"
check_blocked "/content/bmad-showcase.query.json" "Query JSON"
check_blocked "/content/bmad-showcase.children.json" "Children JSON"
check_blocked "/content/bmad-showcase/jcr:content.json" "JCR Content Direct"

echo ""
echo "--- Security Headers ---"
check_header "X-Frame-Options"
check_header "X-Content-Type-Options"
check_header "X-XSS-Protection"
check_header "Strict-Transport-Security"
check_header "Content-Security-Policy"
check_header "Referrer-Policy"

echo ""
echo "--- Server Information Disclosure ---"
response=$(curl -sI "${BASE_URL}/content/bmad-showcase/en/home.html" 2>/dev/null)

if echo "$response" | grep -qi "Server:.*AEM\|Server:.*Adobe\|Server:.*Communique"; then
    echo -e "${YELLOW}[WARN]${NC} Server header reveals AEM"
    ((WARN_COUNT++))
else
    echo -e "${GREEN}[PASS]${NC} Server header does not reveal AEM"
    ((PASS_COUNT++))
fi

if echo "$response" | grep -qi "X-Powered-By"; then
    echo -e "${YELLOW}[WARN]${NC} X-Powered-By header present"
    ((WARN_COUNT++))
else
    echo -e "${GREEN}[PASS]${NC} X-Powered-By header not present"
    ((PASS_COUNT++))
fi

echo ""
echo "--- Dispatcher Cache Headers ---"
if echo "$response" | grep -qi "X-Dispatcher"; then
    echo -e "${GREEN}[INFO]${NC} X-Dispatcher header present (Dispatcher is active)"
else
    echo -e "${YELLOW}[WARN]${NC} X-Dispatcher header not present"
    ((WARN_COUNT++))
fi

echo ""
echo "============================================"
echo "Summary"
echo "============================================"
echo -e "${GREEN}Passed:${NC} $PASS_COUNT"
echo -e "${RED}Failed:${NC} $FAIL_COUNT"
echo -e "${YELLOW}Warnings:${NC} $WARN_COUNT"

if [ $FAIL_COUNT -gt 0 ]; then
    echo ""
    echo -e "${RED}CRITICAL: Security vulnerabilities found!${NC}"
    exit 1
elif [ $WARN_COUNT -gt 0 ]; then
    echo ""
    echo -e "${YELLOW}WARNING: Some security improvements recommended${NC}"
    exit 0
else
    echo ""
    echo -e "${GREEN}All security checks passed!${NC}"
    exit 0
fi
