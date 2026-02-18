# Feature Backlog

> **How to use:** Add features below, prioritize monthly, pull into sprints.

---

## Priority Legend

| Priority | Meaning | Timeline |
|----------|---------|----------|
| 🔴 P1 | Critical / Blocking | This sprint |
| 🟠 P2 | High / Important | Next sprint |
| 🟡 P3 | Medium / Nice to have | This quarter |
| 🟢 P4 | Low / Future | Backlog |

---

## Current Sprint

**Sprint:** [Sprint Name/Number]
**Dates:** [Start] - [End]
**Goal:** [Sprint goal]

| ID | Feature | Priority | Owner | Status |
|----|---------|----------|-------|--------|
| | | | | |

---

## Ready for Next Sprint

| ID | Feature | Priority | Effort | Notes |
|----|---------|----------|--------|-------|
| | | | | |

---

## Backlog (Unprioritized)

### Components
| ID | Feature | Requested By | Date | Notes |
|----|---------|--------------|------|-------|
| FEAT-001 | Form component | | | Basic contact form |
| FEAT-002 | Accordion component | | | FAQ sections |
| FEAT-003 | Tabs component | | | Tabbed content |
| FEAT-004 | Modal/Lightbox | | | Image galleries |
| FEAT-005 | Video component | | | YouTube/Vimeo embed |

### Integrations
| ID | Feature | Requested By | Date | Notes |
|----|---------|--------------|------|-------|
| FEAT-010 | Salesforce CRM | | | Lead capture |
| FEAT-011 | Marketo integration | | | Marketing automation |
| FEAT-012 | Google Analytics 4 | | | Alternative to Adobe |
| FEAT-013 | Contentful import | | | Content migration |

### Infrastructure
| ID | Feature | Requested By | Date | Notes |
|----|---------|--------------|------|-------|
| FEAT-020 | Multi-site support | | | Language copies |
| FEAT-021 | Headless preview | | | SPA preview mode |
| FEAT-022 | GraphQL persisted queries | | | Performance |

### Documentation
| ID | Feature | Requested By | Date | Notes |
|----|---------|--------------|------|-------|
| FEAT-030 | Video tutorials | | | YouTube series |
| FEAT-031 | Storybook integration | | | Component library |

---

## Completed (Archive)

| ID | Feature | Completed | Sprint |
|----|---------|-----------|--------|
| | | | |

---

## How to Add a Feature Request

### Option 1: Quick Add (Edit this file)

```markdown
| FEAT-XXX | [Feature name] | [Your name] | [Date] | [Brief notes] |
```

### Option 2: BEAD Issue (For tracking with AI agents)

```bash
# Create BEAD issue
cat > bmad/gastown/bead/.issues/inbox/FEAT-XXX.md << 'EOF'
---
id: FEAT-XXX
type: feature
status: inbox
priority: P3
requested_by: [Name]
requested_date: [Date]
---

# [Feature Name]

## Description
[What is needed]

## Use Case
[Why it's needed]

## Acceptance Criteria
- [ ] [Criterion 1]
- [ ] [Criterion 2]

## Notes
[Any additional context]
EOF
```

### Option 3: GitHub Issue

Create issue at: https://github.com/narendragandhi/aem-bmad-showcase/issues

Use template:
```
**Feature:** [Name]
**Priority:** P1/P2/P3/P4
**Description:** [What]
**Use Case:** [Why]
```

---

## Monthly Prioritization Meeting

**Frequency:** First Monday of each month
**Duration:** 1 hour
**Attendees:** Product Owner, Tech Lead, 1 Dev rep

**Agenda:**
1. Review new requests (15 min)
2. Prioritize backlog (30 min)
3. Capacity check for next sprints (15 min)

**Output:** Updated priorities in this file

---

## Sprint Planning

**Frequency:** Every 2 weeks
**Pull from:** "Ready for Next Sprint" section

**Process:**
1. Review sprint goal
2. Pull prioritized items
3. Break into tasks (use BEAD templates)
4. Assign owners
5. Update "Current Sprint" section

---

## Tracking Progress

### Simple (This File)
- Move items between sections
- Update status column
- Archive when done

### With BEAD (AI-assisted)
```
bmad/gastown/bead/.issues/
├── inbox/          ← New requests
├── coder/          ← In development
├── tester/         ← In QA
├── reviewer/       ← In review
├── completed/      ← Done
└── blocked/        ← Blocked items
```

### With GitHub Projects
- Create project board
- Link to this repo
- Use labels for priority

---

## Version Planning

| Version | Target | Theme | Key Features |
|---------|--------|-------|--------------|
| v1.1 | Q2 2024 | Forms & Engagement | Form component, Modal |
| v1.2 | Q3 2024 | Integrations | CRM, Marketing tools |
| v2.0 | Q4 2024 | Multi-site | Language copies, MSM |

---

## Quick Stats

| Metric | Count |
|--------|-------|
| Total Backlog | 0 |
| P1 (Critical) | 0 |
| P2 (High) | 0 |
| In Progress | 0 |
| Completed (All Time) | 0 |

*Last Updated: [Date]*
