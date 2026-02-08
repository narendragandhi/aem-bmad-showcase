# BMAD, BEAD, and Gas Town: A Multi-Layered AI-Augmented Development Ecosystem

This document provides a comprehensive comparison and integration strategy for three key concepts in AI-augmented software development: BMAD (Breakthrough Method for Agile Development), BEAD (Beads) system, and Gas Town (AI Agent Orchestration System).

## 1. BMAD (Breakthrough Method for Agile Development) Overview

BMAD is an open-source, AI-driven agile development *framework* designed to enhance the entire software development lifecycle. It utilizes specialized AI agents and structured workflows to adapt to various project complexities, from simple tasks to enterprise-level systems.

**Key Characteristics of BMAD:**
-   **Nature**: A comprehensive agile development framework.
-   **Scope**: Covers the entire software development lifecycle (from business discovery to deployment).
-   **Primary User**: Human development teams, project managers, architects, and developers, augmented by AI assistance.
-   **Key Features**:
    -   AI-assisted help and scale-domain-adaptive intelligence.
    -   Structured workflows based on agile best practices (e.g., "Full Planning Path").
    -   Specialized roles for AI agents (e.g., PM, Architect, Developer agents).
    -   Extensible with official modules for specialized domains.
-   **Goal**: To accelerate and optimize software delivery by integrating AI into agile processes.

## 2. BEAD (Beads) System Overview

BEAD, specifically the "Beads" system (implemented via the `bd` CLI tool), is a distributed, Git-backed graph issue tracker designed *for individual AI agents*. Its primary purpose is to address the limitations of AI coding agents, such as finite memory and context loss, by providing a persistent, structured memory and task management system for a single agent.

**Key Characteristics of BEAD (Beads):**
-   **Nature**: A Git-friendly issue tracker and persistent memory system.
-   **Scope**: Primarily focused on managing tasks, context, and dependencies *for an individual AI agent*.
-   **Primary User**: An individual AI coding agent, though humans interact with the `bd` tool.
-   **Key Features**:
    -   Git as a version-controlled database for issues.
    -   Persistent memory and context window management for AI agents.
    -   Dependency-aware task graph.
    -   Hash-based IDs for issues to prevent conflicts.
    -   Local SQLite cache for performance.
    -   Optimized for AI agent's JSON output and dependency tracking.
-   **Goal**: To make individual AI agents more methodical, focused, and effective at managing complex, long-horizon coding tasks.

## 3. Gas Town (AI Agent Orchestration System) Overview

Gas Town is an AI agent orchestration system that builds upon the BEAD concept. It is designed to manage and coordinate *multiple* AI coding agents, persist their work state across sessions, and facilitate reliable multi-agent workflows. Gas Town addresses the challenge of orchestrating a team of specialized AI agents to work collaboratively on larger software development efforts.

**Key Characteristics of Gas Town:**
-   **Nature**: An AI agent orchestration system.
-   **Scope**: Manages and coordinates a team of AI agents, persisting their collective work state.
-   **Primary User**: A "Mayor" AI (or human orchestrator) that directs a team of specialized AI agents.
-   **Key Features**:
    -   Orchestration of multiple AI agents (e.g., coders, reviewers, testers).
    -   Persistence of collective work state using Git-backed mechanisms (leveraging Beads).
    -   Facilitates complex, multi-agent workflows.
    -   Addresses context loss across a team of AI agents.
-   **Goal**: To enable teams of AI agents to work together effectively on larger, more complex software development projects.

## 4. Comparison and Contrast

| Feature               | BMAD                                                                               | BEAD (Beads)                                                                     | Gas Town                                                                        |
| :-------------------- | :--------------------------------------------------------------------------------- | :------------------------------------------------------------------------------- | :------------------------------------------------------------------------------ |
| **Type**              | Comprehensive Agile Development *Framework*                                        | *Tool/System* for *Individual* AI Agent Task & Context Management              | *System* for *Multi-Agent* Orchestration                                        |
| **Primary Focus**     | End-to-end software delivery, human-AI team collaboration, AI-augmented workflows  | AI agent memory, context persistence, structured task execution, issue tracking  | Orchestration, collaboration, and state persistence for *teams* of AI agents    |
| **Target Audience**   | Human development teams (PMs, Architects, Devs) leveraging AI                      | An individual AI coding agent                                                    | A "Mayor" AI (or human orchestrator) directing a team of AI agents              |
| **Granularity**       | High-level project phases, epics, user stories, cross-functional tasks             | Fine-grained tasks, dependencies, issues, and contextual data for an AI agent    | Coordination of multiple AI agents, management of their collective workflow     |
| **Core Mechanism**    | Structured workflows, AI agent roles, agile practices                              | Git-backed issue graph, persistent memory, context querying, dependency tracking | "Mayor" AI orchestration, persistent state via Git, multi-agent workflows       |
| **AI Role**           | AI assists humans across various roles (PM, Architect, Developer)                   | AI agents directly interact with and are managed by the system (Beads)           | AI agents are directed and coordinated by an orchestrating "Mayor" AI           |
| **AEM Application**   | Guides the entire AEM project lifecycle                                            | Used by individual AI agents within AEM development tasks                        | Orchestrates multiple AI agents (e.g., AEM component coders, AEM test writers)  |

## 5. Multi-Layered Synergy in an AEM Project

The true power emerges when BMAD, BEAD, and Gas Town are layered together within an AEM project, creating a sophisticated AI-augmented development ecosystem.

```mermaid
graph TD
    subgraph BMAD Layer (Strategic Oversight & Human-AI Collaboration)
        BMAD_PM[PM Agent: Define High-level Task (e.g., "Develop Hero Component")] --> BMAD_ARCH[Architect Agent: Design & Validate Component Specs]
        BMAD_ARCH --> BMAD_DEV_TASK(BMAD Task: "Develop Hero Component" with specs from component-design.md)
    end

    subgraph Gastown Layer (Multi-Agent Orchestration)
        GT_MAYOR[Mayor AI: Orchestrate Team for "Develop Hero Component"]
        BMAD_DEV_TASK --> GT_MAYOR

        GT_MAYOR -- Directs & Monitors --> AICoder(AI Coder Agent)
        GT_MAYOR -- Directs & Monitors --> AITest(AI Test Writer Agent)
        GT_MAYOR -- Directs & Monitors --> AICReview(AI Code Reviewer Agent)
    end

    subgraph BEAD Layer (Individual AI Agent Task Management & Persistent Context)
        AICoder -- Manages Tasks & Context via --> BEADCoder[BEAD: Coder's Tasks (e.g., Sling Model, HTL, CSS)]
        AITest -- Manages Tasks & Context via --> BEADTest[BEAD: Tester's Tasks (e.g., JUnit, UI Tests)]
        AICReview -- Manages Tasks & Context via --> BEADReview[BEAD: Reviewer's Tasks (e.g., Code Quality, Compliance)]

        BEADCoder --> SLING[Task: Create Sling Model (using component-design.md)]
        BEADCoder --> HTL[Task: Develop HTL Script (using design system, i18n)]
        BEADCoder --> CSS[Task: Style CSS (using design system tokens)]

        BEADTest --> JUNIT[Task: Write JUnit Test (for Sling Model)]
        BEADTest --> UI_TEST[Task: Write UI Test (for component rendering)]

        BEADReview --> REVIEW_CODE[Task: Review Coder's Work (for standards, accessibility)]
    end

    SLING --> HTL
    HTL --> CSS
    CSS -- Code Ready --> JUNIT
    JUNIT -- Tests Pass --> UI_TEST
    UI_TEST -- Tests Pass --> REVIEW_CODE

    REVIEW_CODE -- Reports Status to --> AICReview
    AICReview -- Reports Status to --> GT_MAYOR
    AITest -- Reports Status to --> GT_MAYOR
    AICoder -- Reports Status to --> GT_MAYOR

    GT_MAYOR -- Aggregates & Reports Completion --> BMAD_DEV_TASK
    BMAD_DEV_TASK --> AEM_DEPLOY(AEM Cloud Manager Deployment Trigger)
    AEM_DEPLOY --> FINAL_PRODUCT(Deployed AEM Site)
```

1.  **BMAD as the Strategic Layer**: BMAD defines the overarching strategy and process for the entire AEM project. It sets high-level goals, establishes phases like Business Discovery and Architecture Design, and assigns roles. This is where the human team collaborates with and directs the higher-level BMAD AI agents (e.g., a "BMAD PM Agent").

2.  **Gas Town as the Orchestration Layer**: When a complex task from the BMAD framework is delegated to AI (e.g., "Develop a new AEM component suite"), Gas Town comes into play. A "Mayor" AI, operating under the BMAD directive, would use Gas Town to orchestrate a team of specialized AI agents. This team might include:
    *   An "AEM Component Coder" AI.
    *   An "AEM Test Writer" AI.
    *   An "AEM Documentation Generator" AI.
    *   An "AEM Code Reviewer" AI.
    Gas Town manages their collective work state, directs their collaboration, and ensures the entire multi-agent workflow remains consistent and recoverable.

3.  **BEAD as the Agent Memory Layer**: Each individual AI agent orchestrated by Gas Town (e.g., the "AEM Component Coder" AI) would utilize the BEAD system. BEAD provides these individual agents with:
    *   **Persistent Memory**: To remember context, design decisions, and previous coding attempts for the specific component they are building.
    *   **Task Management**: To break down their coding assignment into granular steps, track dependencies (e.g., "finish Sling Model before HTL"), and manage their internal work graph.
    *   **Traceability**: All their individual actions and progress are recorded in a Git-backed BEAD issue, offering transparency into the AI's thought process.

**In an AEM project lifecycle**:
*   A **BMAD PM Agent** might identify the need for a new "Hero" component.
*   This task is then assigned to an **AI-driven development team orchestrated by Gas Town**.
*   Gas Town would then direct an **AEM Component Coder AI**, which uses **BEAD** to manage the Sling Model, HTL, and CSS implementation, while simultaneously directing an **AEM Test Writer AI**, also using **BEAD**, to write the corresponding unit and integration tests.
*   The results are then integrated back into the BMAD workflow, potentially reviewed by a **BMAD Code Reviewer Agent** before deployment through Cloud Manager.

This multi-layered approach ensures that high-level strategic goals are met (BMAD), complex multi-agent tasks are coordinated effectively (Gas Town), and individual AI agents remain focused and context-aware (BEAD).