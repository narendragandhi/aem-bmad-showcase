# Getting Started with AEM BMAD Showcase

This guide walks you through setting up and running the AEM BMAD Showcase project locally.

## Prerequisites

Before you begin, ensure you have:

| Requirement | Version | Verify Command |
|-------------|---------|----------------|
| Java JDK | 21 | `java -version` |
| Maven | 3.6+ | `mvn -version` |
| Node.js | 18+ | `node -version` |
| npm | 9+ | `npm -version` |
| AEM SDK | Latest | Download from [Software Distribution](https://experience.adobe.com/#/downloads/content/software-distribution/en/aemcloud.html) |

## Quick Start (15 minutes)

### Step 1: Clone the Repository

```bash
git clone https://github.com/narendragandhi/aem-bmad-showcase.git
cd aem-bmad-showcase
```

### Step 2: Start AEM SDK

1. **Download AEM SDK** from Adobe Software Distribution (requires Adobe ID)

2. **Extract and start the Author instance:**
   ```bash
   # Create a directory for AEM
   mkdir -p ~/aem-sdk/author
   cd ~/aem-sdk/author

   # Copy the SDK JAR (rename for convenience)
   cp ~/Downloads/aem-sdk-*.jar aem-author-p4502.jar

   # Start AEM (first run takes 5-10 minutes)
   java -jar aem-author-p4502.jar
   ```

3. **Wait for startup** - AEM is ready when you see:
   ```
   *INFO* [main] Startup completed
   ```

4. **Access AEM**: Open http://localhost:4502 (admin/admin)

### Step 3: Build and Deploy

```bash
# From the project root directory
cd ~/path/to/aem-bmad-showcase

# Build and deploy to local AEM
mvn clean install -PautoInstallSinglePackage
```

**Build Options:**

| Command | Description |
|---------|-------------|
| `mvn clean install -PautoInstallSinglePackage` | Deploy all to Author |
| `mvn clean install -PautoInstallSinglePackagePublish` | Deploy to Publish (port 4503) |

### Step 4: Verify Installation

1. Open http://localhost:4502/crx/de (CRXDE Lite)
2. Navigate to `/apps/aem-bmad-showcase/components`
3. You should see: `hero`, `card`, `carousel`, `navigation`, etc.

## Project Structure

```
aem-bmad-showcase/
├── bmad/                    # BMAD methodology documentation
│   ├── 00-Project-Initialization/
│   ├── 01-Business-Discovery/
│   ├── 02-Model-Definition/
│   ├── 03-Architecture-Design/
│   ├── 04-Development-Sprint/
│   ├── 05-Testing-and-Deployment/
│   └── 06-Integrations/
├── core/                    # Java backend (Sling Models, Services)
│   └── src/main/java/
├── ui.apps/                 # AEM components (HTL, dialogs)
│   └── src/main/content/jcr_root/apps/
├── ui.content/              # Sample content
├── ui.frontend/             # Frontend assets (CSS, JS)
├── ui.config/               # OSGi configurations
├── all/                     # Aggregate package for deployment
└── dispatcher/              # Dispatcher configuration
```

## Development Workflow

### Making Code Changes

1. **Edit Java code** in `core/src/main/java/`
2. **Edit components** in `ui.apps/src/main/content/jcr_root/apps/aem-bmad-showcase/`
3. **Redeploy:**
   ```bash
   # Quick redeploy (core + ui.apps only)
   mvn clean install -PautoInstallBundle -pl core
   mvn clean install -PautoInstallSinglePackage -pl ui.apps
   ```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=HeroModelTest

# Run with coverage report
mvn test jacoco:report
# View report at core/target/site/jacoco/index.html
```

### Frontend Development

```bash
cd ui.frontend

# Install dependencies
npm install

# Development build with watch
npm run dev

# Production build
npm run build

# Deploy to AEM
cd .. && mvn clean install -PautoInstallSinglePackage -pl ui.frontend,ui.apps
```

## Creating Your First Page

1. Open http://localhost:4502/sites.html/content
2. Click **Create** → **Page**
3. Select a template (e.g., "Content Page")
4. Enter title: "My First Page"
5. Click **Create** → **Open**
6. Add components from the side panel:
   - Drag "Hero" component onto the page
   - Configure it via the dialog (wrench icon)
7. Click **Preview** to see the result

## Common Issues & Solutions

### Build Fails with "Connection refused"

**Cause:** AEM is not running on port 4502

**Solution:**
```bash
# Check if AEM is running
curl -u admin:admin http://localhost:4502/system/console/bundles.json

# If not, start AEM SDK
cd ~/aem-sdk/author && java -jar aem-author-p4502.jar
```

### "Package installation failed"

**Cause:** Missing dependencies or AEM not fully started

**Solution:**
```bash
# Wait for AEM to fully start, then retry
mvn clean install -PautoInstallSinglePackage

# If still failing, check bundle status
open http://localhost:4502/system/console/bundles
# Look for bundles in "Installed" state (should be "Active")
```

### Java Version Mismatch

**Cause:** Wrong Java version (AEM requires Java 21)

**Solution:**
```bash
# Check current Java version
java -version

# On macOS with multiple JDKs, switch to Java 21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Verify
java -version  # Should show 21.x.x
```

### Frontend Build Fails

**Cause:** Node.js version mismatch or missing dependencies

**Solution:**
```bash
# Use Node 18+
node -version

# Clear npm cache and reinstall
cd ui.frontend
rm -rf node_modules package-lock.json
npm install
```

## IDE Setup

### IntelliJ IDEA

1. **Import Project:** File → Open → Select `pom.xml`
2. **Enable Maven Auto-Import:** When prompted
3. **Set Project SDK:** File → Project Structure → SDK → Java 21
4. **Install AEM Plugins:** (Optional)
   - AEM IDE Tooling (IntelliJ Marketplace)

### VS Code

1. **Open Folder:** File → Open Folder → Select project root
2. **Install Extensions:**
   - Extension Pack for Java
   - XML Tools
   - HTL (Sightly) Support

3. **Configure settings.json:**
   ```json
   {
     "java.configuration.updateBuildConfiguration": "automatic",
     "java.jdt.ls.java.home": "/path/to/jdk-21"
   }
   ```

## Next Steps

1. **Understand the BMAD Methodology:**
   - Read [bmad/methodologies/BMAD-BEAD-GasTown.md](bmad/methodologies/BMAD-BEAD-GasTown.md)
   - Review the traceability matrix: [bmad/traceability-matrix.md](bmad/traceability-matrix.md)

2. **Explore Components:**
   - Start with Hero: `ui.apps/src/main/content/jcr_root/apps/aem-bmad-showcase/components/content/hero/`
   - Read the component design docs: [bmad/03-Architecture-Design/component-design.md](bmad/03-Architecture-Design/component-design.md)

3. **Review Integration Patterns:**
   - [bmad/06-Integrations/README.md](bmad/06-Integrations/README.md)

4. **Run the Full Test Suite:**
   ```bash
   mvn verify
   ```

## Deploying to AEM as a Cloud Service

See [bmad/05-Testing-and-Deployment/deployment-plan.md](bmad/05-Testing-and-Deployment/deployment-plan.md) for Cloud Manager setup and deployment procedures.

## Getting Help

- **AEM Documentation:** https://experienceleague.adobe.com/docs/experience-manager-cloud-service.html
- **Project Issues:** https://github.com/narendragandhi/aem-bmad-showcase/issues
- **BMAD Methodology:** See `/bmad` directory documentation
