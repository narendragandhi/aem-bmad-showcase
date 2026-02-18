# Learning Path

## Visual Overview

```
                            ┌─────────────────────┐
                            │   START HERE        │
                            │   QUICK-START.md    │
                            └──────────┬──────────┘
                                       │
                    ┌──────────────────┼──────────────────┐
                    │                  │                  │
                    ▼                  ▼                  ▼
            ┌───────────────┐  ┌───────────────┐  ┌───────────────┐
            │  DEVELOPER    │  │   ARCHITECT   │  │   QA / PM     │
            │               │  │               │  │               │
            │  Build        │  │  Design       │  │  Test/Plan    │
            │  Components   │  │  Systems      │  │  Quality      │
            └───────┬───────┘  └───────┬───────┘  └───────┬───────┘
                    │                  │                  │
                    ▼                  ▼                  ▼
            ┌───────────────┐  ┌───────────────┐  ┌───────────────┐
            │ Developer-    │  │ Architect-    │  │ QA-Engineer-  │
            │ Guide.md      │  │ Guide.md      │  │ Guide.md      │
            │               │  │               │  │ Product-      │
            │ (30 min)      │  │ (45 min)      │  │ Manager.md    │
            └───────┬───────┘  └───────┬───────┘  └───────┬───────┘
                    │                  │                  │
                    └──────────────────┼──────────────────┘
                                       │
                                       ▼
                            ┌─────────────────────┐
                            │   READY TO BUILD    │
                            │                     │
                            │   Pick your path:   │
                            └──────────┬──────────┘
                                       │
         ┌─────────────────────────────┼─────────────────────────────┐
         │                             │                             │
         ▼                             ▼                             ▼
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│  JUST BUILD     │         │  LEARN BMAD     │         │  USE AI AGENTS  │
│                 │         │                 │         │                 │
│  Clone & code   │         │  Full process   │         │  GasTown        │
│  Ignore docs    │         │  7 phases       │         │  automation     │
│                 │         │                 │         │                 │
│  Time: 1 hour   │         │  Time: 1 week   │         │  Time: 2 hours  │
└─────────────────┘         └─────────────────┘         └─────────────────┘
```

---

## Path 1: Just Build (Fastest)

**Time: 1 hour**

```
Step 1 (5 min)     Step 2 (10 min)      Step 3 (45 min)
─────────────      ──────────────       ──────────────
Clone repo    →    Build & deploy   →   Create component
                                        using existing
                                        patterns
```

**Read:**
1. `README.md`
2. Look at existing component in `ui.apps/`
3. Copy pattern, modify

---

## Path 2: Standard Adoption (Recommended)

**Time: 1 day**

```
Day 1 Morning              Day 1 Afternoon
─────────────              ────────────────
├── QUICK-START.md         ├── Create first component
├── Developer-Guide.md     ├── Write unit test
└── Build & run locally    └── Understand build process
```

**Read:**
1. `QUICK-START.md` (10 min)
2. `bmad/tutorials/Developer-Guide.md` (30 min)
3. `bmad/04-Development-Sprint/development-guidelines.md` (15 min)

---

## Path 3: Full BMAD Methodology (Complete)

**Time: 1-2 weeks**

```
Week 1                              Week 2
──────                              ──────
├── Day 1: BMAD overview            ├── Day 1: Testing
├── Day 2: Business Discovery       ├── Day 2: Deployment
├── Day 3: Model Definition         ├── Day 3: Operations
├── Day 4: Architecture             ├── Day 4: GasTown (optional)
└── Day 5: Development              └── Day 5: Practice project
```

**Reading order:**
```
1. bmad/methodologies/BMAD-BEAD-GasTown.md
2. bmad/00-Project-Initialization/README.md
3. bmad/01-Business-Discovery/README.md
4. bmad/02-Model-Definition/README.md
5. bmad/03-Architecture-Design/README.md
6. bmad/04-Development-Sprint/README.md
7. bmad/05-Testing-and-Deployment/README.md
8. bmad/06-Integrations/README.md
9. bmad/07-Operations/01-operational-runbooks.md
```

---

## What to Skip (Initially)

| Document | Skip If |
|----------|---------|
| `07-Operations/*` | Not deploying to production yet |
| `gastown/*` | Not using AI agents |
| `bead-examples/*` | Not using task tracking |
| `PRODUCTION-READINESS-BACKLOG.md` | Not hardening for production |
| Integration docs | Not using that integration |

---

## Learning Checkpoints

### Checkpoint 1: Can Build
- [ ] Successfully ran `mvn clean install`
- [ ] Deployed to local AEM
- [ ] Can see components in author

### Checkpoint 2: Can Develop
- [ ] Created a new Sling Model
- [ ] Created HTL template
- [ ] Created dialog
- [ ] Wrote unit test

### Checkpoint 3: Can Deploy
- [ ] Understand dispatcher config
- [ ] Know Cloud Manager basics
- [ ] Can run E2E tests

### Checkpoint 4: Can Architect
- [ ] Understand component patterns
- [ ] Know integration approaches
- [ ] Can design new features

---

## Mentorship Approach

### For Teams

**Week 1: Pair Programming**
```
Senior Dev + New Dev
├── Build together
├── Create component together
└── Review patterns together
```

**Week 2: Guided Independence**
```
New Dev solo + Code Review
├── Create component alone
├── Senior reviews PR
└── Discuss improvements
```

**Week 3+: Independence**
```
New Dev owns features
├── Uses docs for reference
├── Asks questions when stuck
└── Contributes back to docs
```

---

## Quick Reference Cards

### Card 1: Build Commands
```bash
mvn clean install                     # Build
mvn clean install -PautoInstallPackage # Deploy
mvn test                              # Run tests
npm run dev --prefix ui.frontend      # Frontend dev
```

### Card 2: File Locations
```
Models:      core/src/main/java/.../models/
Components:  ui.apps/.../components/
Dialogs:     ui.apps/.../components/[name]/_cq_dialog/
Tests:       core/src/test/java/.../models/
Styles:      ui.frontend/src/components/
```

### Card 3: Component Checklist
```
[ ] Model.java created
[ ] .html template created
[ ] _cq_dialog created
[ ] .content.xml created
[ ] Styles in ui.frontend
[ ] Unit test written
[ ] Manual test passed
```

---

## FAQ for New Team Members

**Q: This seems like a lot. Do I need it all?**

A: No. Start with code, add methodology as needed.

**Q: Which tutorial do I read first?**

A: Your role's tutorial in `bmad/tutorials/`

**Q: Can I ignore GasTown?**

A: Yes. It's optional AI tooling.

**Q: What if I get stuck?**

A: Check the relevant phase folder in `bmad/`

**Q: How long to be productive?**

A: Day 1 for simple changes, Week 1 for new components

---

## Simplification Summary

| Total Docs | Must Read | Can Skip Initially |
|------------|-----------|-------------------|
| 72 | 3-5 | 65+ |

**Must Read:**
1. `QUICK-START.md`
2. Your role tutorial
3. `development-guidelines.md`

**Everything else is reference material.**
