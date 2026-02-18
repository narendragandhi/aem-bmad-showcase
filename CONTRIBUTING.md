# Contributing to AEM BMAD Showcase

## Quick Reference

| Action | How |
|--------|-----|
| Request a feature | Add to `BACKLOG.md` or GitHub Issue |
| Report a bug | GitHub Issue |
| Submit code | PR to `main` branch |
| Update docs | Edit directly, submit PR |

---

## Adding Feature Requests

### Option 1: Edit BACKLOG.md (Simplest)

Add a row to the backlog:

```markdown
| FEAT-XXX | My Feature | Your Name | 2024-02-18 | Brief description |
```

### Option 2: GitHub Issue

Create issue at: https://github.com/narendragandhi/aem-bmad-showcase/issues

### Option 3: BEAD Issue (For AI agent tracking)

```bash
cat > bmad/gastown/bead/.issues/inbox/FEAT-XXX.md << 'EOF'
---
id: FEAT-XXX
type: feature
priority: P3
---

# Feature Name

## Description
[What is needed]

## Acceptance Criteria
- [ ] Criterion 1
- [ ] Criterion 2
EOF
```

---

## Priority Levels

| Priority | Meaning | Timeline |
|----------|---------|----------|
| 🔴 P1 | Critical / Blocking | This sprint |
| 🟠 P2 | High / Important | Next sprint |
| 🟡 P3 | Medium / Nice to have | This quarter |
| 🟢 P4 | Low / Future | Backlog |

---

## Development Workflow

### 1. Pick a Task

Check `BACKLOG.md` → "Current Sprint" or "Ready for Next Sprint"

### 2. Create Branch

```bash
git checkout -b feature/FEAT-XXX-short-description
# or
git checkout -b fix/BUG-XXX-short-description
```

### 3. Develop

Follow patterns in:
- `bmad/tutorials/Developer-Guide.md`
- `bmad/04-Development-Sprint/development-guidelines.md`

### 4. Test

```bash
mvn clean verify                      # Full build + tests
mvn test                              # Unit tests only
./scripts/ci/run-all-tests.sh --unit  # All test types
```

### 5. Submit PR

```bash
git push origin feature/FEAT-XXX-short-description
```

Then create PR via GitHub.

### 6. Update Tracking

After merge, move item in `BACKLOG.md` to "Completed" section.

---

## PR Checklist

Before submitting:

- [ ] Code compiles: `mvn clean install`
- [ ] Tests pass: `mvn test`
- [ ] No new warnings
- [ ] Documentation updated (if applicable)
- [ ] `BACKLOG.md` updated (if applicable)

---

## Coding Standards

### Java
- Sling Model pattern (see existing models)
- Unit test required (80% coverage target)
- Javadoc for public methods
- Use `@Optional` annotations

### HTL
- Semantic HTML5
- Accessibility attributes (ARIA, roles)
- Context-aware escaping (`@ context='uri'`)

### CSS/SCSS
- BEM naming convention
- Use design tokens from `ui.frontend/src/variables/`
- Mobile-first responsive

### Commits

```
feat: Add new feature
fix: Fix bug
docs: Update documentation
test: Add tests
refactor: Code refactoring
chore: Maintenance tasks
```

---

## Review Process

1. **Author** submits PR with description
2. **CI** runs automated tests
3. **Reviewer** checks code quality
4. **Author** addresses feedback
5. **Merge** when approved

---

## Release Process

### Versioning (SemVer)

```
v1.0.0 → Major (breaking changes)
v1.1.0 → Minor (new features)
v1.1.1 → Patch (bug fixes)
```

### Creating a Release

1. Update version in all `pom.xml` files
2. Update `CHANGELOG.md`
3. Commit: `git commit -m "chore: Release v1.1.0"`
4. Tag: `git tag v1.1.0`
5. Push: `git push && git push --tags`

---

## Iteration Cadence

| Activity | Frequency | Owner |
|----------|-----------|-------|
| Sprint planning | Every 2 weeks | Team |
| Backlog grooming | Weekly | Product Owner |
| Priority review | Monthly | Stakeholders |
| Release | As needed | Tech Lead |

---

## Getting Help

| Question | Resource |
|----------|----------|
| How to build? | `README.md` |
| Code patterns? | `bmad/tutorials/Developer-Guide.md` |
| Architecture? | `bmad/03-Architecture-Design/` |
| Stuck? | Create GitHub Issue |

---

## Local Development Setup

See `GETTING-STARTED.md` for full setup instructions.

Quick start:
```bash
git clone https://github.com/narendragandhi/aem-bmad-showcase.git
cd aem-bmad-showcase
mvn clean install -PautoInstallPackage
```

---

Thank you for contributing!
