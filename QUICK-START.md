# Quick Start Guide

## TL;DR - What Is This?

A **ready-to-use AEM project template** with:
- 5 production-ready components
- AI content generation built-in
- Complete documentation
- Testing setup
- CI/CD ready

---

## For Different Roles

### "I just want to build an AEM site"

**Start here → Use the code, ignore the methodology**

```bash
# 1. Clone
git clone https://github.com/narendragandhi/aem-bmad-showcase.git my-project

# 2. Rename
find . -name "pom.xml" -exec sed -i 's/bmad-showcase/my-project/g' {} \;

# 3. Build & Deploy
mvn clean install -PautoInstallPackage
```

**What you get:**
- Working Hero, Card, Carousel, Navigation components
- LLM service for AI content (optional)
- Email service (optional)
- Unit tests included

---

### "I want to learn BMAD methodology"

**Start here → Read 3 documents**

| Order | Document | Time |
|-------|----------|------|
| 1 | `bmad/methodologies/BMAD-BEAD-GasTown.md` | 15 min |
| 2 | `bmad/tutorials/Developer-Guide.md` | 30 min |
| 3 | `bmad/00-Project-Initialization/README.md` | 10 min |

**Then explore phases as needed:**
```
bmad/
├── 00-Project-Initialization/  ← Start here
├── 01-Business-Discovery/      ← Requirements
├── 02-Model-Definition/        ← Content models
├── 03-Architecture-Design/     ← Technical design
├── 04-Development-Sprint/      ← Coding guidelines
├── 05-Testing-and-Deployment/  ← QA & deploy
├── 06-Integrations/            ← APIs & services
└── 07-Operations/              ← Production ops
```

---

### "I want to use AI agents (GasTown)"

**Start here → One file**

Read: `bmad/gastown/README.md`

**Basic usage:**
```
@workspace Use the Mayor AI from bmad/gastown/agents/mayor.md
to create a new Card component
```

**That's it.** The Mayor delegates to specialist agents automatically.

---

## Adoption Levels

### Level 1: Just the Code (Day 1)

```
Use:
├── core/           ← Java models
├── ui.apps/        ← Components
├── ui.frontend/    ← CSS/JS
└── dispatcher/     ← Cache rules

Ignore everything else.
```

### Level 2: Add Testing (Week 1)

```
Add:
├── core/src/test/              ← Unit tests
├── ui.tests/playwright/        ← E2E tests
└── scripts/testing/            ← Test scripts
```

### Level 3: Use Documentation (Week 2-4)

```
Read as needed:
├── bmad/tutorials/             ← Role guides
├── bmad/03-Architecture/       ← Design docs
└── bmad/04-Development/        ← Coding standards
```

### Level 4: Full BMAD (Month 2+)

```
Adopt methodology:
├── bmad/00-07 phases           ← Full process
├── bmad/gastown/               ← AI agents
└── bmad/bead-examples/         ← Task tracking
```

---

## Common Questions

### "Do I need all 72 documents?"

**No.** Most are reference material. Start with:
- `README.md` (this repo)
- `QUICK-START.md` (this file)
- One tutorial for your role

### "Do I need GasTown/BEAD?"

**No.** They're optional AI workflow tools. The code works without them.

### "What's the minimum to get started?"

```bash
git clone [repo]
mvn clean install -PautoInstallPackage
# Done. You have working components.
```

### "How do I learn more gradually?"

| Week | Focus | Documents |
|------|-------|-----------|
| 1 | Build & deploy | README, pom.xml |
| 2 | Components | Developer-Guide.md |
| 3 | Testing | testing-strategy.md |
| 4 | Architecture | system-architecture.md |
| 5+ | Advanced | Pick what you need |

---

## File Structure (Simplified)

```
aem-bmad-showcase/
│
├── 📦 CODE (Use immediately)
│   ├── core/                 ← Java backend
│   ├── ui.apps/              ← AEM components
│   ├── ui.frontend/          ← CSS/JS
│   └── dispatcher/           ← Apache config
│
├── 📚 DOCS (Read as needed)
│   └── bmad/
│       ├── tutorials/        ← START HERE
│       └── 00-07 phases/     ← Reference
│
├── 🤖 AI TOOLS (Optional)
│   └── bmad/gastown/         ← AI agents
│
└── 🧪 TESTING (Add when ready)
    ├── scripts/              ← Test runners
    └── ui.tests/             ← E2E tests
```

---

## One-Page Cheat Sheet

### Build Commands
```bash
mvn clean install                    # Build all
mvn clean install -PautoInstallPackage  # Build + deploy
npm run dev --prefix ui.frontend     # Frontend dev mode
```

### Key Locations
```
Components:  ui.apps/src/main/content/jcr_root/apps/bmad-showcase/components/
Models:      core/src/main/java/com/example/aem/bmad/core/models/
Tests:       core/src/test/java/
Styles:      ui.frontend/src/components/
```

### Add a Component
1. Create Model: `core/.../models/MyModel.java`
2. Create HTL: `ui.apps/.../components/mycomponent/mycomponent.html`
3. Create Dialog: `ui.apps/.../components/mycomponent/_cq_dialog/.content.xml`
4. Add Styles: `ui.frontend/src/components/mycomponent/`
5. Write Test: `core/.../models/MyModelTest.java`

---

## Getting Help

| Question | Where to Look |
|----------|---------------|
| How to build? | `README.md` |
| How to create component? | `bmad/tutorials/Developer-Guide.md` |
| Architecture questions? | `bmad/tutorials/Architect-Guide.md` |
| Testing questions? | `bmad/05-Testing-and-Deployment/` |
| Production deployment? | `bmad/07-Operations/` |

---

## Summary

| Complexity | What to Use | Time to Start |
|------------|-------------|---------------|
| **Minimal** | Just clone & build | 5 minutes |
| **Basic** | Code + 1 tutorial | 1 hour |
| **Standard** | Code + testing + docs | 1 day |
| **Full** | Everything | 1-2 weeks |

**Start simple. Add complexity only when needed.**
