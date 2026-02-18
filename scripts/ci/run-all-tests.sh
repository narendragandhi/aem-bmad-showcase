#!/bin/bash
#
# AEM BMAD Showcase - Comprehensive Test Runner
# Executes all test types: unit, integration, e2e, security, performance
#
# Usage: ./run-all-tests.sh [options]
# Options:
#   --unit         Run unit tests only
#   --integration  Run integration tests only
#   --e2e          Run E2E tests only
#   --security     Run security scans only
#   --performance  Run performance tests only
#   --all          Run all tests (default)
#   --env          Target environment (local|dev|stage)
#   --report       Generate HTML report
#

set -e

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
REPORTS_DIR="$PROJECT_ROOT/test-reports"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

# Default settings
RUN_UNIT=false
RUN_INTEGRATION=false
RUN_E2E=false
RUN_SECURITY=false
RUN_PERFORMANCE=false
RUN_ALL=true
ENV="local"
GENERATE_REPORT=false

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --unit)
            RUN_UNIT=true
            RUN_ALL=false
            shift
            ;;
        --integration)
            RUN_INTEGRATION=true
            RUN_ALL=false
            shift
            ;;
        --e2e)
            RUN_E2E=true
            RUN_ALL=false
            shift
            ;;
        --security)
            RUN_SECURITY=true
            RUN_ALL=false
            shift
            ;;
        --performance)
            RUN_PERFORMANCE=true
            RUN_ALL=false
            shift
            ;;
        --all)
            RUN_ALL=true
            shift
            ;;
        --env)
            ENV="$2"
            shift 2
            ;;
        --report)
            GENERATE_REPORT=true
            shift
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done

# Set all flags if running all
if [ "$RUN_ALL" = true ]; then
    RUN_UNIT=true
    RUN_INTEGRATION=true
    RUN_E2E=true
    RUN_SECURITY=true
    RUN_PERFORMANCE=true
fi

# Environment URLs
case $ENV in
    local)
        AEM_AUTHOR_URL="http://localhost:4502"
        AEM_PUBLISH_URL="http://localhost:4503"
        DISPATCHER_URL="http://localhost:8080"
        ;;
    dev)
        AEM_AUTHOR_URL="${AEM_DEV_AUTHOR_URL:-https://author-dev.example.com}"
        AEM_PUBLISH_URL="${AEM_DEV_PUBLISH_URL:-https://publish-dev.example.com}"
        DISPATCHER_URL="${AEM_DEV_DISPATCHER_URL:-https://dev.example.com}"
        ;;
    stage)
        AEM_AUTHOR_URL="${AEM_STAGE_AUTHOR_URL:-https://author-stage.example.com}"
        AEM_PUBLISH_URL="${AEM_STAGE_PUBLISH_URL:-https://publish-stage.example.com}"
        DISPATCHER_URL="${AEM_STAGE_DISPATCHER_URL:-https://stage.example.com}"
        ;;
    *)
        echo "Unknown environment: $ENV"
        exit 1
        ;;
esac

# Create reports directory
mkdir -p "$REPORTS_DIR/$TIMESTAMP"

# Utility functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Test result tracking
UNIT_RESULT=0
INTEGRATION_RESULT=0
E2E_RESULT=0
SECURITY_RESULT=0
PERFORMANCE_RESULT=0

#############################################
# Unit Tests
#############################################
run_unit_tests() {
    log_info "Running Unit Tests..."

    cd "$PROJECT_ROOT"

    if mvn test -pl core -Dtest.report.dir="$REPORTS_DIR/$TIMESTAMP/unit"; then
        log_success "Unit tests passed"
        UNIT_RESULT=0
    else
        log_error "Unit tests failed"
        UNIT_RESULT=1
    fi

    # Generate coverage report
    mvn jacoco:report -pl core -Djacoco.destFile="$REPORTS_DIR/$TIMESTAMP/coverage"

    return $UNIT_RESULT
}

#############################################
# Integration Tests
#############################################
run_integration_tests() {
    log_info "Running Integration Tests..."

    cd "$PROJECT_ROOT"

    # Run integration tests with failsafe
    if mvn verify -pl it.tests \
        -Daem.author.url="$AEM_AUTHOR_URL" \
        -Daem.publish.url="$AEM_PUBLISH_URL" \
        -Dfailsafe.reportsDirectory="$REPORTS_DIR/$TIMESTAMP/integration"; then
        log_success "Integration tests passed"
        INTEGRATION_RESULT=0
    else
        log_error "Integration tests failed"
        INTEGRATION_RESULT=1
    fi

    return $INTEGRATION_RESULT
}

#############################################
# E2E Tests (Playwright)
#############################################
run_e2e_tests() {
    log_info "Running E2E Tests with Playwright..."

    cd "$PROJECT_ROOT/ui.tests"

    # Install dependencies if needed
    if [ ! -d "node_modules" ]; then
        npm ci
    fi

    # Install Playwright browsers
    npx playwright install --with-deps chromium firefox webkit

    # Run tests
    export BASE_URL="$DISPATCHER_URL"

    if npx playwright test \
        --reporter=html \
        --output="$REPORTS_DIR/$TIMESTAMP/e2e"; then
        log_success "E2E tests passed"
        E2E_RESULT=0
    else
        log_error "E2E tests failed"
        E2E_RESULT=1
    fi

    # Move HTML report
    if [ -d "playwright-report" ]; then
        mv playwright-report "$REPORTS_DIR/$TIMESTAMP/e2e-report"
    fi

    return $E2E_RESULT
}

#############################################
# Security Tests
#############################################
run_security_tests() {
    log_info "Running Security Tests..."

    cd "$PROJECT_ROOT"

    # 1. Dependency Check (SCA)
    log_info "Running OWASP Dependency Check..."
    mvn org.owasp:dependency-check-maven:check \
        -DfailBuildOnCVSS=7 \
        -Dformat=ALL \
        -DoutputDirectory="$REPORTS_DIR/$TIMESTAMP/security/dependency-check" || true

    # 2. SpotBugs with FindSecBugs
    log_info "Running SpotBugs Security Scan..."
    mvn spotbugs:check -pl core \
        -Dspotbugs.outputDirectory="$REPORTS_DIR/$TIMESTAMP/security/spotbugs" || true

    # 3. OWASP ZAP Baseline Scan (if ZAP is available)
    if command -v zap-baseline.py &> /dev/null; then
        log_info "Running OWASP ZAP Baseline Scan..."
        zap-baseline.py -t "$DISPATCHER_URL" \
            -r "$REPORTS_DIR/$TIMESTAMP/security/zap-report.html" \
            -w "$REPORTS_DIR/$TIMESTAMP/security/zap-report.md" || true
    else
        log_warning "OWASP ZAP not found, skipping DAST scan"
    fi

    # 4. Dispatcher Security Check
    log_info "Running Dispatcher Security Checks..."
    "$SCRIPT_DIR/../testing/check-dispatcher-security.sh" "$DISPATCHER_URL" \
        > "$REPORTS_DIR/$TIMESTAMP/security/dispatcher-check.txt" 2>&1 || true

    # Check if any critical vulnerabilities found
    if grep -q "CRITICAL" "$REPORTS_DIR/$TIMESTAMP/security/"* 2>/dev/null; then
        log_error "Critical security vulnerabilities found"
        SECURITY_RESULT=1
    else
        log_success "No critical security vulnerabilities found"
        SECURITY_RESULT=0
    fi

    return $SECURITY_RESULT
}

#############################################
# Performance Tests
#############################################
run_performance_tests() {
    log_info "Running Performance Tests..."

    cd "$PROJECT_ROOT"

    # Create performance test directory
    mkdir -p "$REPORTS_DIR/$TIMESTAMP/performance"

    # 1. Lighthouse Audit
    log_info "Running Lighthouse Audit..."
    if command -v lighthouse &> /dev/null; then
        lighthouse "$DISPATCHER_URL/content/bmad-showcase/en/home.html" \
            --output=json,html \
            --output-path="$REPORTS_DIR/$TIMESTAMP/performance/lighthouse" \
            --chrome-flags="--headless" \
            --only-categories=performance,accessibility,best-practices,seo || true
    else
        log_warning "Lighthouse not found, skipping audit"
    fi

    # 2. k6 Load Test (if k6 is available)
    if command -v k6 &> /dev/null; then
        log_info "Running k6 Load Test..."
        k6 run "$SCRIPT_DIR/../testing/k6-load-test.js" \
            --env BASE_URL="$DISPATCHER_URL" \
            --out json="$REPORTS_DIR/$TIMESTAMP/performance/k6-results.json" || true
    else
        log_warning "k6 not found, skipping load test"
    fi

    # 3. Response Time Check
    log_info "Checking Response Times..."
    "$SCRIPT_DIR/../testing/check-response-times.sh" "$DISPATCHER_URL" \
        > "$REPORTS_DIR/$TIMESTAMP/performance/response-times.txt" 2>&1 || true

    # Check if performance thresholds met
    if grep -q "FAIL" "$REPORTS_DIR/$TIMESTAMP/performance/"* 2>/dev/null; then
        log_warning "Some performance thresholds not met"
        PERFORMANCE_RESULT=1
    else
        log_success "Performance tests passed"
        PERFORMANCE_RESULT=0
    fi

    return $PERFORMANCE_RESULT
}

#############################################
# Generate Combined Report
#############################################
generate_report() {
    log_info "Generating Combined Test Report..."

    cat > "$REPORTS_DIR/$TIMESTAMP/index.html" << EOF
<!DOCTYPE html>
<html>
<head>
    <title>Test Report - $TIMESTAMP</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        .summary { display: flex; gap: 20px; margin: 20px 0; }
        .card { padding: 20px; border-radius: 8px; min-width: 150px; }
        .pass { background: #d4edda; color: #155724; }
        .fail { background: #f8d7da; color: #721c24; }
        .skip { background: #fff3cd; color: #856404; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background: #f5f5f5; }
        a { color: #007bff; }
    </style>
</head>
<body>
    <h1>AEM BMAD Showcase - Test Report</h1>
    <p><strong>Generated:</strong> $(date)</p>
    <p><strong>Environment:</strong> $ENV</p>

    <div class="summary">
        <div class="card $([ $UNIT_RESULT -eq 0 ] && echo 'pass' || echo 'fail')">
            <h3>Unit Tests</h3>
            <p>$([ $UNIT_RESULT -eq 0 ] && echo 'PASSED' || echo 'FAILED')</p>
        </div>
        <div class="card $([ $INTEGRATION_RESULT -eq 0 ] && echo 'pass' || echo 'fail')">
            <h3>Integration</h3>
            <p>$([ $INTEGRATION_RESULT -eq 0 ] && echo 'PASSED' || echo 'FAILED')</p>
        </div>
        <div class="card $([ $E2E_RESULT -eq 0 ] && echo 'pass' || echo 'fail')">
            <h3>E2E Tests</h3>
            <p>$([ $E2E_RESULT -eq 0 ] && echo 'PASSED' || echo 'FAILED')</p>
        </div>
        <div class="card $([ $SECURITY_RESULT -eq 0 ] && echo 'pass' || echo 'fail')">
            <h3>Security</h3>
            <p>$([ $SECURITY_RESULT -eq 0 ] && echo 'PASSED' || echo 'FAILED')</p>
        </div>
        <div class="card $([ $PERFORMANCE_RESULT -eq 0 ] && echo 'pass' || echo 'fail')">
            <h3>Performance</h3>
            <p>$([ $PERFORMANCE_RESULT -eq 0 ] && echo 'PASSED' || echo 'FAILED')</p>
        </div>
    </div>

    <h2>Detailed Reports</h2>
    <table>
        <tr><th>Test Type</th><th>Report</th></tr>
        <tr><td>Unit Tests</td><td><a href="unit/index.html">View Report</a></td></tr>
        <tr><td>Integration Tests</td><td><a href="integration/index.html">View Report</a></td></tr>
        <tr><td>E2E Tests</td><td><a href="e2e-report/index.html">View Report</a></td></tr>
        <tr><td>Security - Dependency Check</td><td><a href="security/dependency-check/dependency-check-report.html">View Report</a></td></tr>
        <tr><td>Security - ZAP</td><td><a href="security/zap-report.html">View Report</a></td></tr>
        <tr><td>Performance - Lighthouse</td><td><a href="performance/lighthouse.report.html">View Report</a></td></tr>
    </table>
</body>
</html>
EOF

    log_success "Report generated: $REPORTS_DIR/$TIMESTAMP/index.html"
}

#############################################
# Main Execution
#############################################
main() {
    log_info "============================================"
    log_info "AEM BMAD Showcase - Test Runner"
    log_info "============================================"
    log_info "Environment: $ENV"
    log_info "Report Directory: $REPORTS_DIR/$TIMESTAMP"
    log_info "============================================"

    # Run requested tests
    if [ "$RUN_UNIT" = true ]; then
        run_unit_tests || true
    fi

    if [ "$RUN_INTEGRATION" = true ]; then
        run_integration_tests || true
    fi

    if [ "$RUN_E2E" = true ]; then
        run_e2e_tests || true
    fi

    if [ "$RUN_SECURITY" = true ]; then
        run_security_tests || true
    fi

    if [ "$RUN_PERFORMANCE" = true ]; then
        run_performance_tests || true
    fi

    # Generate report
    if [ "$GENERATE_REPORT" = true ]; then
        generate_report
    fi

    # Summary
    log_info "============================================"
    log_info "Test Summary"
    log_info "============================================"

    TOTAL_RESULT=0

    if [ "$RUN_UNIT" = true ]; then
        if [ $UNIT_RESULT -eq 0 ]; then
            log_success "Unit Tests: PASSED"
        else
            log_error "Unit Tests: FAILED"
            TOTAL_RESULT=1
        fi
    fi

    if [ "$RUN_INTEGRATION" = true ]; then
        if [ $INTEGRATION_RESULT -eq 0 ]; then
            log_success "Integration Tests: PASSED"
        else
            log_error "Integration Tests: FAILED"
            TOTAL_RESULT=1
        fi
    fi

    if [ "$RUN_E2E" = true ]; then
        if [ $E2E_RESULT -eq 0 ]; then
            log_success "E2E Tests: PASSED"
        else
            log_error "E2E Tests: FAILED"
            TOTAL_RESULT=1
        fi
    fi

    if [ "$RUN_SECURITY" = true ]; then
        if [ $SECURITY_RESULT -eq 0 ]; then
            log_success "Security Tests: PASSED"
        else
            log_error "Security Tests: FAILED"
            TOTAL_RESULT=1
        fi
    fi

    if [ "$RUN_PERFORMANCE" = true ]; then
        if [ $PERFORMANCE_RESULT -eq 0 ]; then
            log_success "Performance Tests: PASSED"
        else
            log_warning "Performance Tests: WARNINGS"
        fi
    fi

    log_info "============================================"

    if [ $TOTAL_RESULT -eq 0 ]; then
        log_success "All tests passed!"
    else
        log_error "Some tests failed. Check reports for details."
    fi

    exit $TOTAL_RESULT
}

# Run main
main
