# AEM as a Cloud Service Greenfield Implementation using BMAD Method

This project showcases the **BMAD (Breakthrough Method for Agile Development)** for a greenfield implementation of Adobe Experience Manager (AEM) as a Cloud Service.

## Overview

The BMAD method is an AI-driven agile development framework that provides a structured, agent-based approach to software development. This project demonstrates how to apply BMAD methodology specifically to AEM projects, guiding teams from initial business discovery through production deployment.

### What is BMAD?

BMAD organizes development work around specialized AI agents, each responsible for different phases of the project lifecycle:

- **PM Agent**: Drives business discovery, requirements gathering, and user story creation
- **Architect Agent**: Designs system architecture, component specifications, and technical standards
- **Developer Agent**: Implements features following established patterns and best practices
- **QA Agent**: Ensures quality through comprehensive testing strategies

## Project Structure

```
aem-bmad-showcase/
├── README.md                          # This file
├── bmad/                              # BMAD process documentation
│   ├── 00-Project-Initialization/     # Project setup and environment
│   ├── 01-Business-Discovery/         # Requirements and user stories
│   ├── 02-Model-Definition/           # Content models and IA
│   ├── 03-Architecture-Design/        # System architecture
│   ├── 04-Development-Sprint/         # Sprint planning and guidelines
│   ├── 05-Testing-and-Deployment/     # QA and deployment plans
│   ├── bead-examples/                 # BEAD issue examples for AI agents
│   ├── methodologies/                 # BMAD, BEAD, GasTown integration docs
│   ├── tooling/                       # Getting started guides for tools
│   ├── tutorials/                     # Step-by-step migration tutorials
│   └── traceability-matrix.md         # Requirements to implementation mapping
└── src/                               # AEM project source code (generated)
```

## The BMAD Phases for AEM

### Phase 00: Project Initialization

**Agent**: DevOps/Setup Agent

Setting up the AEM project foundation:
- AEM Project Archetype configuration and generation
- Local development environment setup (AEM SDK)
- Git repository initialization
- Cloud Manager project configuration

**Key Artifacts**:
- [AEM Project Archetype Config](bmad/00-Project-Initialization/aem-project-archetype-config.md)

### Phase 01: Business Discovery

**Agent**: PM Agent

Understanding business goals and user needs:
- Stakeholder interviews and requirements gathering
- User story creation with acceptance criteria
- Feature prioritization and backlog creation
- Success metrics definition

**Key Artifacts**:
- [Business Requirements](bmad/01-Business-Discovery/requirements.md)
- [User Stories](bmad/01-Business-Discovery/user-stories.md)

### Phase 02: Model Definition

**Agent**: Architect Agent + PM Agent

Defining content structure and information architecture:
- Content model design (templates, components)
- Information architecture and sitemap
- Design system integration
- Multi-lingual content strategy

**Key Artifacts**:
- [Content Models](bmad/02-Model-Definition/content-models.md)
- [Information Architecture](bmad/02-Model-Definition/information-architecture.md)
- [Design System](bmad/02-Model-Definition/design-system.md)

### Phase 03: Architecture Design

**Agent**: Architect Agent

Technical architecture and system design:
- AEM as a Cloud Service architecture
- Component technical specifications
- Dispatcher configuration
- Integration patterns (CRM, Analytics, Translation)

**Key Artifacts**:
- [System Architecture](bmad/03-Architecture-Design/system-architecture.md)
- [Component Design](bmad/03-Architecture-Design/component-design.md)
- [Dispatcher Rules](bmad/03-Architecture-Design/dispatcher-rules.md)

### Phase 04: Development Sprint

**Agent**: Developer Agent

Implementing features in iterative sprints:
- Sprint planning and task breakdown
- Component development (Sling Models, HTL, CSS)
- Code reviews and pair programming
- Continuous integration with Cloud Manager

**Key Artifacts**:
- [Sprint 1 Plan](bmad/04-Development-Sprint/sprint-1-plan.md)
- [Development Guidelines](bmad/04-Development-Sprint/development-guidelines.md)

### Phase 05: Testing and Deployment

**Agent**: QA Agent + DevOps Agent

Quality assurance and production deployment:
- Unit, integration, and UI testing
- Accessibility and performance testing
- Cloud Manager pipeline execution
- Production deployment with rollback plans

**Key Artifacts**:
- [Testing Strategy](bmad/05-Testing-and-Deployment/testing-strategy.md)
- [Deployment Plan](bmad/05-Testing-and-Deployment/deployment-plan.md)
- [Cloud Manager Best Practices](bmad/05-Testing-and-Deployment/cloud-manager-best-practices.md)

## Technology Stack

| Layer | Technology |
|-------|------------|
| CMS | Adobe Experience Manager as a Cloud Service |
| Backend | Java 11+, OSGi, Sling Models |
| Frontend | HTL, React (SPA Editor), SCSS |
| CI/CD | Adobe Cloud Manager |
| CDN | Adobe Managed CDN (Fastly) |
| Analytics | Adobe Analytics |
| Personalization | Adobe Target |

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Node.js 18+ (for frontend build)
- AEM as a Cloud Service SDK
- Adobe Cloud Manager access

### Local Development Setup

1. **Generate the AEM project** using the archetype configuration:
   ```bash
   mvn -B archetype:generate \
     -D archetypeGroupId=com.adobe.aem \
     -D archetypeArtifactId=aem-project-archetype \
     -D archetypeVersion=35 \
     -D aemVersion=cloud \
     -D appTitle="AEM BMAD Showcase" \
     -D appId="aem-bmad-showcase" \
     -D groupId="com.example"
   ```

2. **Start the AEM SDK** Author instance:
   ```bash
   java -jar aem-sdk-quickstart-*.jar
   ```

3. **Build and deploy** to local AEM:
   ```bash
   mvn clean install -PautoInstallPackage
   ```

### Cloud Deployment

1. Push code to the Git repository connected to Cloud Manager
2. Trigger the appropriate pipeline (Dev, Stage, or Production)
3. Monitor pipeline execution and quality gates
4. Verify deployment in target environment

## How to Use This Showcase

This repository serves as a **reference implementation** and **template** for AEM projects using the BMAD methodology:

1. **As a Learning Resource**: Study the documentation in each phase to understand BMAD applied to AEM
2. **As a Template**: Fork this repository and adapt the artifacts to your specific project needs
3. **As a Starting Point**: Use the documented patterns and guidelines as a foundation for your implementation

### Adapting for Your Project

1. Review all phase documentation and customize for your requirements
2. Update content models to match your content strategy
3. Modify component designs based on your design system
4. Adjust the information architecture for your sitemap
5. Configure integrations specific to your tech stack

## Advanced Topics

### Multi-Agent Orchestration with BEAD and GasTown

This showcase includes documentation on advanced AI agent orchestration:

- **[BMAD, BEAD, and GasTown Integration](bmad/methodologies/BMAD-BEAD-GasTown.md)**: Comprehensive guide on how these three systems work together for multi-agent AEM development
- **[BEAD Examples](bmad/bead-examples/README.md)**: Sample BEAD issues demonstrating AI agent task management
- **[GasTown Getting Started](bmad/tooling/GasTown-GettingStarted.md)**: Conceptual guide for setting up multi-agent orchestration

### Requirements Traceability

- **[Traceability Matrix](bmad/traceability-matrix.md)**: End-to-end mapping from business requirements through implementation and testing

### Migration Tutorials

- **[WordPress to AEM Migration Tutorial](bmad/tutorials/WordPress-to-AEMaaCS-Migration-Tutorial.md)**: Step-by-step guide for migrating WordPress sites to AEM as a Cloud Service
- **[WordPress Migration Level of Effort](bmad/tutorials/WordPress-Migration-LoE.md)**: Estimation guide for WordPress migration projects

## Contributing

Contributions to improve this showcase are welcome. Please follow these guidelines:

1. Create a feature branch from `develop`
2. Follow the coding standards in the Development Guidelines
3. Ensure all documentation is updated
4. Submit a pull request with a clear description

## License

This project is provided as an example and educational resource. Adapt and use according to your organization's needs.
