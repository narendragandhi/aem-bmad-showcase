# BEAD - Backlog for Execution by AI Developers

BEAD is a git-backed issue tracking system designed specifically for AI agent coordination within the GasTown framework. It provides persistent memory and context for individual AI agents across sessions.

## Purpose

Traditional issue trackers (JIRA, GitHub Issues) are optimized for human workflows. BEAD is optimized for:

1. **AI Agent Memory**: Persistent context that survives session boundaries
2. **Inter-Agent Communication**: Structured handoffs between specialized agents
3. **Progress Tracking**: Machine-readable status for workflow orchestration
4. **Audit Trail**: Git history provides complete decision/action log

## Directory Structure

```
bead/
├── README.md                    # This file
├── .issues/
│   ├── inbox/                   # New issues awaiting triage
│   ├── coder/                   # Issues assigned to Component Coder
│   ├── tester/                  # Issues assigned to Test Writer
│   ├── reviewer/                # Issues assigned to Code Reviewer
│   ├── dispatcher/              # Issues assigned to Dispatcher Config
│   ├── docs/                    # Issues assigned to Documentation Agent
│   ├── blocked/                 # Issues waiting on external input
│   └── completed/               # Archived completed issues
├── templates/
│   ├── component-task.md        # Template for component development
│   ├── bug-fix.md               # Template for bug fixes
│   ├── integration-task.md      # Template for integration work
│   ├── test-task.md             # Template for test creation
│   ├── review-task.md           # Template for code reviews
│   └── docs-task.md             # Template for documentation
└── workflows/
    └── active/                  # Currently executing workflow instances
```

## Issue Format

### File Naming Convention

```
{workflow-id}-{task-type}-{sequence}.md
```

Examples:
- `comp-001-impl-001.md` - Implementation task for component workflow 001
- `bug-123-fix-001.md` - Fix task for bug 123
- `int-adobe-analytics-test-002.md` - Second test task for Adobe Analytics integration

### Issue Structure

```markdown
---
id: {unique-issue-id}
workflow_id: {parent-workflow-id}
type: {implementation|test|review|docs|config}
agent: {assigned-agent-id}
status: {pending|in_progress|blocked|completed|cancelled}
priority: {critical|high|medium|low}
created: {ISO-8601-timestamp}
updated: {ISO-8601-timestamp}
depends_on: [{issue-ids}]
blocks: [{issue-ids}]
---

# {Issue Title}

## Context
{Background information and requirements}

## Acceptance Criteria
- [ ] Criterion 1
- [ ] Criterion 2

## Technical Details
{Implementation specifics, code references, etc.}

## Progress Log
### {timestamp}
{What was done, decisions made, files changed}

## Handoff Notes
{Information for dependent tasks or next agent}

## Files Changed
- `path/to/file.java` - {description}

## Related Issues
- #{related-issue-id}
```

## Status Lifecycle

```
pending → in_progress → completed
              ↓
           blocked → in_progress
              ↓
          cancelled
```

### Status Definitions

| Status | Description | Next Actions |
|--------|-------------|--------------|
| `pending` | Created, awaiting agent pickup | Agent claims and moves to `in_progress` |
| `in_progress` | Agent actively working | Complete or mark `blocked` |
| `blocked` | Waiting on external input/dependency | Move back to `in_progress` when unblocked |
| `completed` | Work finished successfully | Move to `completed/` directory |
| `cancelled` | No longer needed | Move to `completed/` with cancellation note |

## Agent Workflows

### Picking Up a Task

1. Agent checks their inbox directory
2. Reads issue frontmatter and content
3. Updates status to `in_progress`
4. Updates `updated` timestamp
5. Commits change: `[BEAD] Start: {issue-id} - {brief description}`

### Working on a Task

1. Perform required actions
2. Update Progress Log with each session
3. Update Files Changed as modifications occur
4. Commit regularly: `[BEAD] Progress: {issue-id} - {what was done}`

### Completing a Task

1. Verify all acceptance criteria met
2. Update status to `completed`
3. Write Handoff Notes for dependent tasks
4. Move file to `completed/` directory
5. Commit: `[BEAD] Complete: {issue-id} - {summary}`

### Blocking a Task

1. Update status to `blocked`
2. Document blocker in Progress Log
3. Create issue for blocker if needed
4. Commit: `[BEAD] Blocked: {issue-id} - {blocker description}`

## Inter-Agent Communication

### Mayor → Specialist Agent

```yaml
# Issue assignment
assignee: coder
context:
  workflow: component-development
  phase: implementation
  inputs:
    - component_name: accordion
    - component_type: content
  dependencies:
    - planning-001: completed
```

### Specialist → Mayor

```yaml
# Completion report
status: completed
outputs:
  files_created:
    - core/src/main/java/.../AccordionModel.java
    - ui.apps/.../accordion/accordion.html
  metrics:
    lines_of_code: 245
    test_coverage: 92%
issues_found: []
next_recommended: testing
```

### Specialist → Specialist

```yaml
# Handoff notes (e.g., Coder → Tester)
for_agent: tester
context:
  implementation_complete: true
  key_files:
    - AccordionModel.java:45-120  # Business logic
    - accordion.html:1-50          # Template rendering
  edge_cases_to_test:
    - Empty items array
    - Maximum 50 items
    - Special characters in labels
  mocking_required:
    - ResourceResolver (use AemContext)
```

## Git Conventions

### Commit Message Format

```
[BEAD] {Action}: {issue-id} - {description}

{Optional body with details}
```

Actions:
- `Create` - New issue created
- `Start` - Work begun on issue
- `Progress` - Incremental progress update
- `Complete` - Issue finished
- `Blocked` - Issue blocked
- `Unblocked` - Issue unblocked
- `Cancel` - Issue cancelled

### Branch Strategy

For larger tasks, agents may create feature branches:

```
bead/{issue-id}/{brief-description}
```

Example: `bead/comp-001-impl/accordion-model`

## Query Patterns

### Find All Issues for an Agent

```bash
ls bead/.issues/coder/
```

### Find All In-Progress Issues

```bash
grep -l "status: in_progress" bead/.issues/*/*.md
```

### Find Blocked Issues

```bash
grep -l "status: blocked" bead/.issues/*/*.md
```

### Find Issues by Workflow

```bash
grep -l "workflow_id: comp-001" bead/.issues/*/*.md
```

## Integration with GasTown

### Mayor's Perspective

The Mayor uses BEAD to:
1. Create issues for each workflow phase
2. Assign issues to appropriate agents
3. Monitor progress across all agents
4. Identify blockers and reassign work
5. Aggregate completion status

### Workflow Engine Integration

Workflows reference BEAD issues:

```yaml
phases:
  - id: implementation
    agent: coder
    bead_issue_template: templates/component-task.md
    on_complete:
      update_bead: true
      notify_mayor: true
```

## Best Practices

1. **Atomic Commits**: One logical change per commit
2. **Frequent Updates**: Update Progress Log each session
3. **Clear Handoffs**: Write detailed Handoff Notes
4. **Link Everything**: Reference related issues and files
5. **Clean Up**: Move completed issues promptly
6. **Don't Edit History**: Append to Progress Log, don't rewrite

## Example Session

```
[Mayor] Received: Develop accordion component
[Mayor] Creating BEAD issues...
  → bead/.issues/coder/comp-001-impl-001.md
  → bead/.issues/tester/comp-001-test-001.md
  → bead/.issues/reviewer/comp-001-review-001.md

[Coder] Checking inbox: 1 new issue
[Coder] Reading comp-001-impl-001.md
[Coder] Status: pending → in_progress
[Coder] Implementing AccordionModel.java...
[Coder] Progress: Model created, HTL next
[Coder] Implementing accordion.html...
[Coder] All acceptance criteria met
[Coder] Status: in_progress → completed
[Coder] Handoff: Key files listed for Tester
[Coder] Moving to completed/

[Tester] Checking inbox: 1 new issue
[Tester] Dependency comp-001-impl-001 completed
[Tester] Reading handoff notes...
[Tester] Status: pending → in_progress
...
```
