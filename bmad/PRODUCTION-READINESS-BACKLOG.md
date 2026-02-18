# Production Readiness Backlog

## Overview

This backlog contains prioritized tasks to bring the AEM BMAD Showcase from reference implementation to production-ready status. Tasks are organized into sprints with BEAD task IDs for tracking.

**Estimated Effort:** 3 Sprints (6 weeks)
**Current Test Coverage:** 31%
**Target Test Coverage:** 80%

---

## Sprint 1: Critical Security & Foundation (Week 1-2)

### PROD-001: Move API Keys to Cloud Manager Secrets
**Priority:** P1 - CRITICAL
**Effort:** 4 hours
**Type:** Security

```yaml
id: PROD-001
status: pending
assignee: TBD
blocked_by: []

tasks:
  - Remove API keys from OSGi configs
  - Create Cloud Manager secret variables
  - Update ClaudeServiceImpl to use $[secret:CLAUDE_API_KEY]
  - Update OpenAIServiceImpl to use $[secret:OPENAI_API_KEY]
  - Update EmailServiceImpl for SMTP credentials
  - Test in all environments

files_affected:
  - core/src/main/java/com/example/aem/bmad/core/services/impl/ClaudeServiceImpl.java
  - core/src/main/java/com/example/aem/bmad/core/services/impl/OpenAIServiceImpl.java
  - core/src/main/java/com/example/aem/bmad/core/services/impl/EmailServiceImpl.java
  - ui.config/src/main/content/jcr_root/apps/bmad-showcase/osgiconfig/

acceptance_criteria:
  - [ ] No API keys in source code
  - [ ] No API keys in OSGi console
  - [ ] Services work with Cloud Manager secrets
  - [ ] Documentation updated
```

---

### PROD-002: Fix Node/NPM Version Mismatch
**Priority:** P1 - CRITICAL
**Effort:** 1 hour
**Type:** Build

```yaml
id: PROD-002
status: pending
assignee: TBD

tasks:
  - Update pom.xml Node version from 16.14.2 to 18.19.0
  - Update pom.xml npm version from 8.5.0 to 9.8.1
  - Update frontend-maven-plugin to 1.15.0
  - Test build locally
  - Test in Cloud Manager

files_affected:
  - pom.xml

acceptance_criteria:
  - [ ] Local build succeeds
  - [ ] Cloud Manager build succeeds
  - [ ] No Node/npm warnings
```

---

### PROD-003: Add Service Layer Unit Tests
**Priority:** P1 - CRITICAL
**Effort:** 3 days
**Type:** Testing

```yaml
id: PROD-003
status: pending
assignee: TBD

tasks:
  - Create ClaudeServiceImplTest.java
  - Create OpenAIServiceImplTest.java
  - Create HttpClientServiceImplTest.java
  - Create EmailServiceImplTest.java
  - Create ContentCreationServiceImplTest.java
  - Mock external dependencies (HTTP, SMTP)
  - Test happy path scenarios
  - Test error scenarios
  - Test timeout scenarios
  - Test rate limiting scenarios

files_to_create:
  - core/src/test/java/.../services/impl/ClaudeServiceImplTest.java
  - core/src/test/java/.../services/impl/OpenAIServiceImplTest.java
  - core/src/test/java/.../services/impl/HttpClientServiceImplTest.java
  - core/src/test/java/.../services/impl/EmailServiceImplTest.java
  - core/src/test/java/.../services/impl/ContentCreationServiceImplTest.java

test_scenarios:
  ClaudeServiceImpl:
    - testGenerateContent_Success
    - testGenerateContent_ApiError
    - testGenerateContent_Timeout
    - testGenerateContent_RateLimited
    - testGenerateContent_InvalidResponse
    - testGenerateContent_EmptyPrompt

  HttpClientService:
    - testGet_Success
    - testGet_404
    - testGet_500
    - testGet_Timeout
    - testPost_Success
    - testPost_WithBody
    - testRetry_OnTransientError

  EmailService:
    - testSend_Success
    - testSend_InvalidEmail
    - testSend_SmtpError
    - testSend_WithAttachment

acceptance_criteria:
  - [ ] All services have unit tests
  - [ ] Coverage ≥ 80% per service
  - [ ] Error paths tested
  - [ ] Mocks properly isolated
```

---

### PROD-004: Add Input Validation to Sling Models
**Priority:** P1 - HIGH
**Effort:** 1 day
**Type:** Security

```yaml
id: PROD-004
status: pending
assignee: TBD

tasks:
  - Add validation annotations to HeroModel
  - Add validation annotations to CardModel
  - Add validation annotations to CarouselModel
  - Add validation annotations to NavigationModel
  - Add URL validation for link fields
  - Add XSS sanitization for text fields
  - Add length limits for string fields
  - Update unit tests for validation

files_affected:
  - core/src/main/java/.../models/HeroModel.java
  - core/src/main/java/.../models/CardModel.java
  - core/src/main/java/.../models/CarouselModel.java
  - core/src/main/java/.../models/NavigationModel.java

validation_rules:
  URLs:
    - Must start with / or https://
    - No javascript: protocol
    - Max length 2048 chars

  Text:
    - Strip HTML tags (unless rich text)
    - Max length per field type
    - No null bytes

acceptance_criteria:
  - [ ] All user inputs validated
  - [ ] XSS vectors blocked
  - [ ] Malformed URLs rejected
  - [ ] Unit tests cover validation
```

---

### PROD-005: Implement Structured Logging
**Priority:** P2 - HIGH
**Effort:** 1 day
**Type:** Observability

```yaml
id: PROD-005
status: pending
assignee: TBD

tasks:
  - Create LoggingConstants class
  - Add MDC context for request tracing
  - Implement PII masking utility
  - Add structured log format
  - Update all services to use structured logging
  - Add correlation IDs
  - Configure log levels per environment

files_to_create:
  - core/src/main/java/.../util/LoggingUtils.java
  - core/src/main/java/.../util/PiiMasker.java

logging_standards:
  format: |
    {
      "timestamp": "ISO8601",
      "level": "INFO|WARN|ERROR",
      "correlationId": "uuid",
      "service": "service-name",
      "action": "action-name",
      "duration_ms": 123,
      "message": "Human readable",
      "context": {}
    }

  pii_fields_to_mask:
    - email
    - apiKey
    - password
    - token
    - ssn
    - creditCard

acceptance_criteria:
  - [ ] All logs use structured format
  - [ ] PII masked in logs
  - [ ] Correlation IDs in all requests
  - [ ] Log levels configurable
```

---

### PROD-006: Add Error Handling Patterns
**Priority:** P2 - HIGH
**Effort:** 2 days
**Type:** Reliability

```yaml
id: PROD-006
status: pending
assignee: TBD

tasks:
  - Create custom exception hierarchy
  - Create error response DTOs
  - Add global exception handler
  - Update services to throw typed exceptions
  - Add error codes enumeration
  - Create error mapping configuration

files_to_create:
  - core/src/main/java/.../exception/BmadException.java
  - core/src/main/java/.../exception/ServiceException.java
  - core/src/main/java/.../exception/ValidationException.java
  - core/src/main/java/.../exception/IntegrationException.java
  - core/src/main/java/.../exception/ErrorCodes.java
  - core/src/main/java/.../dto/ErrorResponse.java

exception_hierarchy:
  BmadException (base):
    - ServiceException
      - LLMServiceException
      - EmailServiceException
    - ValidationException
      - InvalidInputException
      - MissingFieldException
    - IntegrationException
      - HttpClientException
      - TimeoutException
      - RateLimitException

acceptance_criteria:
  - [ ] All exceptions extend BmadException
  - [ ] Error codes documented
  - [ ] Consistent error responses
  - [ ] No stack traces in prod responses
```

---

## Sprint 2: Testing & Resilience (Week 3-4)

### PROD-007: Add Circuit Breaker to HTTP Service
**Priority:** P2 - HIGH
**Effort:** 2 days
**Type:** Reliability

```yaml
id: PROD-007
status: pending
assignee: TBD
depends_on: [PROD-006]

tasks:
  - Add Resilience4j dependency
  - Implement circuit breaker for LLM calls
  - Implement circuit breaker for Email calls
  - Add retry with exponential backoff
  - Add bulkhead pattern
  - Add timeout configuration
  - Add fallback responses
  - Create circuit breaker dashboard

configuration:
  circuit_breaker:
    failure_rate_threshold: 50
    slow_call_rate_threshold: 80
    slow_call_duration_threshold: 3s
    permitted_calls_in_half_open: 5
    wait_duration_in_open_state: 30s

  retry:
    max_attempts: 3
    wait_duration: 1s
    exponential_backoff_multiplier: 2

  bulkhead:
    max_concurrent_calls: 10
    max_wait_duration: 500ms

acceptance_criteria:
  - [ ] Circuit opens on failures
  - [ ] Requests fail fast when open
  - [ ] Graceful degradation works
  - [ ] Metrics available
```

---

### PROD-008: Add Integration Tests
**Priority:** P2 - HIGH
**Effort:** 3 days
**Type:** Testing

```yaml
id: PROD-008
status: pending
assignee: TBD
depends_on: [PROD-003]

tasks:
  - Create integration test module
  - Add Testcontainers for AEM mock
  - Create component rendering tests
  - Create servlet endpoint tests
  - Create workflow tests
  - Add to CI pipeline

test_suites:
  ComponentIntegration:
    - HeroComponent renders with model data
    - CardGrid renders correct number of cards
    - Carousel initializes with slides
    - Navigation builds from page tree

  ServletIntegration:
    - Search servlet returns results
    - Content API returns JSON
    - Error responses formatted correctly

  ServiceIntegration:
    - LLM service with mock provider
    - Email service with mock SMTP

acceptance_criteria:
  - [ ] All components have integration tests
  - [ ] Tests run in CI pipeline
  - [ ] Tests isolated with containers
  - [ ] Coverage report generated
```

---

### PROD-009: Expand E2E Test Coverage
**Priority:** P2 - MEDIUM
**Effort:** 2 days
**Type:** Testing

```yaml
id: PROD-009
status: pending
assignee: TBD

tasks:
  - Add author environment tests
  - Add publish environment tests
  - Add content authoring flow tests
  - Add form submission tests
  - Add search functionality tests
  - Add multi-language tests
  - Configure visual regression

test_suites:
  AuthorTests:
    - Login to author
    - Create new page
    - Add component to page
    - Configure component dialog
    - Preview page
    - Publish page

  PublishTests:
    - Page loads correctly
    - Navigation works
    - Search returns results
    - Forms submit successfully
    - Error pages display

  VisualRegression:
    - Homepage baseline
    - Component variants
    - Responsive breakpoints
    - Dark mode (if applicable)

acceptance_criteria:
  - [ ] Author workflow tested
  - [ ] Publish functionality tested
  - [ ] Visual regression baseline set
  - [ ] Tests in CI pipeline
```

---

### PROD-010: Add Frontend Testing
**Priority:** P2 - MEDIUM
**Effort:** 2 days
**Type:** Testing

```yaml
id: PROD-010
status: pending
assignee: TBD

tasks:
  - Configure Jest properly
  - Add ESLint configuration
  - Create component unit tests
  - Create utility function tests
  - Add accessibility tests
  - Configure test coverage reporting

files_to_create:
  - ui.frontend/jest.config.js
  - ui.frontend/.eslintrc.js
  - ui.frontend/src/**/*.test.js

test_coverage_targets:
  utilities: 90%
  components: 70%
  overall: 75%

acceptance_criteria:
  - [ ] Jest configured and running
  - [ ] ESLint passing
  - [ ] Component tests exist
  - [ ] Coverage ≥ 75%
```

---

### PROD-011: Restrict Dispatcher JSON Caching
**Priority:** P2 - MEDIUM
**Effort:** 4 hours
**Type:** Security

```yaml
id: PROD-011
status: pending
assignee: TBD

tasks:
  - Review current JSON caching rules
  - Restrict to specific model.json paths
  - Block sensitive JSON endpoints
  - Add cache headers for API responses
  - Test cache invalidation

current_rule: |
  /0061 { /type "allow" /extension "json" /path "/content/*" }

updated_rules: |
  # Only allow specific JSON exports
  /0061 { /type "allow" /extension "json" /path "/content/*/jcr:content.model.json" }
  /0062 { /type "allow" /extension "json" /path "/api/*" }

  # Block sensitive paths
  /0063 { /type "deny" /extension "json" /path "/content/*/jcr:content/*/config*" }
  /0064 { /type "deny" /extension "json" /path "*.infinity.json" }

acceptance_criteria:
  - [ ] Only approved JSON paths cached
  - [ ] Sensitive data not cached
  - [ ] Model.json exports work
  - [ ] API responses cached appropriately
```

---

## Sprint 3: Production Hardening (Week 5-6)

### PROD-012: Add Health Check Endpoints
**Priority:** P2 - MEDIUM
**Effort:** 1 day
**Type:** Observability

```yaml
id: PROD-012
status: pending
assignee: TBD

tasks:
  - Create HealthCheckServlet
  - Add dependency checks (LLM, Email, Repository)
  - Create /health endpoint
  - Create /ready endpoint
  - Add to dispatcher allow rules
  - Configure monitoring integration

endpoints:
  /api/health:
    response:
      status: UP|DOWN
      checks:
        repository: UP|DOWN
        llm_service: UP|DOWN
        email_service: UP|DOWN
      timestamp: ISO8601

  /api/ready:
    response:
      ready: true|false
      reason: string (if not ready)

acceptance_criteria:
  - [ ] Health endpoint accessible
  - [ ] All dependencies checked
  - [ ] Monitoring can poll endpoints
  - [ ] Dispatcher allows endpoints
```

---

### PROD-013: Add Performance Testing
**Priority:** P2 - MEDIUM
**Effort:** 2 days
**Type:** Testing

```yaml
id: PROD-013
status: pending
assignee: TBD

tasks:
  - Create k6 test scenarios
  - Define performance baselines
  - Create load test script
  - Create stress test script
  - Create spike test script
  - Add to CI pipeline (stage only)
  - Document performance SLAs

test_scenarios:
  baseline:
    vus: 50
    duration: 5m
    thresholds:
      p95_response: <2s
      error_rate: <1%

  load:
    vus: 200
    duration: 15m
    thresholds:
      p95_response: <3s
      error_rate: <2%

  stress:
    vus: 500
    duration: 10m
    thresholds:
      p95_response: <5s
      error_rate: <5%

  spike:
    vus: 1000
    duration: 2m
    thresholds:
      recovery_time: <30s

acceptance_criteria:
  - [ ] Baseline established
  - [ ] SLAs documented
  - [ ] Tests run on stage
  - [ ] Results tracked over time
```

---

### PROD-014: Security Scanning Integration
**Priority:** P2 - MEDIUM
**Effort:** 1 day
**Type:** Security

```yaml
id: PROD-014
status: pending
assignee: TBD

tasks:
  - Add OWASP Dependency Check to POM
  - Configure fail threshold (CVSS 7+)
  - Add SpotBugs with FindSecBugs
  - Add to Cloud Manager pipeline
  - Create suppression file for false positives
  - Document remediation process

tools:
  dependency_check:
    fail_on_cvss: 7
    scan_frequency: every_build

  spotbugs:
    effort: max
    threshold: low
    plugins: [findsecbugs]

  zap_baseline:
    frequency: weekly
    environment: stage

acceptance_criteria:
  - [ ] Dependency scan in CI
  - [ ] SAST scan in CI
  - [ ] No critical vulnerabilities
  - [ ] Suppression file documented
```

---

### PROD-015: Add Accessibility Testing
**Priority:** P3 - MEDIUM
**Effort:** 1 day
**Type:** Testing

```yaml
id: PROD-015
status: pending
assignee: TBD

tasks:
  - Configure Axe-core in Playwright
  - Add accessibility tests for all pages
  - Create accessibility report
  - Add to CI pipeline
  - Document accessibility standards

test_coverage:
  pages:
    - Homepage
    - About
    - Products
    - Contact
    - Search Results

  standards:
    - WCAG 2.1 AA
    - Section 508

acceptance_criteria:
  - [ ] All pages pass WCAG 2.1 AA
  - [ ] No critical accessibility issues
  - [ ] Report generated per build
  - [ ] Issues tracked in backlog
```

---

### PROD-016: Create Operations Runbook Testing
**Priority:** P3 - LOW
**Effort:** 2 days
**Type:** Operations

```yaml
id: PROD-016
status: pending
assignee: TBD

tasks:
  - Review all runbook procedures
  - Create test scenarios for each runbook
  - Execute runbooks in stage environment
  - Document gaps found
  - Update runbooks with learnings
  - Schedule quarterly runbook drills

runbooks_to_test:
  - Deployment rollback
  - Incident response
  - Cache invalidation
  - Log analysis
  - Performance troubleshooting
  - DR failover

acceptance_criteria:
  - [ ] All runbooks executed successfully
  - [ ] Gaps documented and fixed
  - [ ] Team trained on procedures
  - [ ] Drill schedule established
```

---

## Backlog Summary

| Sprint | Tasks | Effort | Focus |
|--------|-------|--------|-------|
| Sprint 1 | PROD-001 to PROD-006 | ~8 days | Security & Foundation |
| Sprint 2 | PROD-007 to PROD-011 | ~11 days | Testing & Resilience |
| Sprint 3 | PROD-012 to PROD-016 | ~7 days | Production Hardening |

### Coverage Targets by Sprint End

| Metric | Current | Sprint 1 | Sprint 2 | Sprint 3 |
|--------|---------|----------|----------|----------|
| Unit Test Coverage | 31% | 50% | 70% | 80% |
| Integration Tests | 0 | 0 | 10+ | 15+ |
| E2E Tests | 3 | 3 | 10+ | 15+ |
| Security Scans | 0 | 1 | 2 | 3 |

---

## Definition of Done

Each task must meet:
- [ ] Code complete and reviewed
- [ ] Unit tests written (≥80% coverage for new code)
- [ ] Integration tests if applicable
- [ ] Documentation updated
- [ ] Security review passed
- [ ] Deployed to Stage
- [ ] QA verified
- [ ] Product Owner accepted

---

## Risk Register

| Risk | Impact | Mitigation |
|------|--------|------------|
| API key exposure before PROD-001 | Critical | Prioritize first, limit access |
| Build failures from Node fix | Medium | Test locally before merge |
| Circuit breaker complexity | Medium | Start with simple config |
| Test maintenance burden | Low | Invest in test infrastructure |

---

## Success Criteria

Production-ready when:
- [ ] All P1 tasks complete
- [ ] All P2 tasks complete
- [ ] Test coverage ≥ 80%
- [ ] Zero critical security findings
- [ ] Performance baselines met
- [ ] Runbooks tested
- [ ] Team trained

---

*Last Updated: February 2024*
*Owner: Technical Lead*
*Review: Weekly*
