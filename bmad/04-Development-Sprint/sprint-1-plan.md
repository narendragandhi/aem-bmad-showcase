# Sprint 1 Plan

This document outlines the goals, user stories, and tasks for the first development sprint.

## Sprint Goal

- To build the core components and templates needed for the homepage and standard pages.

## User Stories for this Sprint

- As a Content Author, I want to be able to create a homepage using the "Home Page Template".
- As a Content Author, I want to be able to add a "Hero Component" to the homepage.
- As a Content Author, I want to be able to add a "Text with Image Component" to a standard page.

## Detailed Tasks (Stories) for Sprint 1

### Component: Hero

- **Story**: As a developer, I need to build the Hero component.
    - **Task (Backend)**: Create a Sling Model `HeroModel.java` that exposes `heading`, `subheading`, `backgroundImage`, `ctaButtonText`, and `ctaButtonLink` properties.
    - **Task (Backend)**: Write a JUnit test `HeroModelTest.java` to verify the logic of the Sling Model.
    - **Task (Content)**: Create the component dialog with fields for `heading` (textfield), `subheading` (textfield), `backgroundImage` (pathfield), `ctaButtonText` (textfield), and `ctaButtonLink` (pathfield).
    - **Task (Frontend)**: Develop the `hero.html` HTL script to render the component markup using the data from the Sling Model.
    - **Task (Frontend)**: Develop the CSS to style the Hero component and ensure it is responsive.

### Component: Text with Image

- **Story**: As a developer, I need to build the Text with Image component.
    - **Task (Backend)**: Create a Sling Model `TextWithImageModel.java` that exposes `heading`, `bodyText`, `image`, and `imagePosition` properties.
    - **Task (Backend)**: Write a JUnit test `TextWithImageModelTest.java` to verify the logic of the Sling Model.
    - **Task (Content)**: Create the component dialog with fields for `heading` (textfield), `bodyText` (richtext), `image` (pathfield), and `imagePosition` (select).
    - **Task (Frontend)**: Develop the `textwithimage.html` HTL script to render the component markup.
    - **Task (Frontend)**: Develop the CSS to style the Text with Image component and apply the correct layout based on the `imagePosition` property.

### Testing

- **Story**: As a QA Engineer, I need to create and execute test cases for the Sprint 1 components.
    - **Task**: Write a test plan for the Hero and Text with Image components.
    - **Task**: Manually test the components on author and publish environments.
    - **Task**: Write an automated UI test script using the AEM `ui.tests` framework for the Hero component's basic rendering.
    - **Task**: Report any bugs found in the project's bug tracking system.

### Dispatcher Rules

- **Story**: As a DevOps Engineer, I need to configure the dispatcher to cache the content for the new components.
    - **Task**: Add a rule to the `dispatcher.any` file to allow requests to the `/content/aem-bmad-showcase` path.
    - **Task**: Configure caching rules to cache the HTML content of the pages.
    - **Task**: Configure invalidation rules to invalidate the cache when a page is published.
- **Reference**: For more details on the dispatcher configuration, see the [Dispatcher Rules](../03-Architecture-Design/dispatcher-rules.md) document.

### Cloud Manager

- **Story**: As a Developer, I need to ensure the project is compliant with Cloud Manager quality gates.
    - **Task**: Configure the `pom.xml` to include the AEM Optimizer plugin.
    - **Task**: Run the local AEM SDK and deploy the code to it for initial validation.
    - **Task**: Trigger a non-production pipeline in Cloud Manager to verify the build, code quality, and deployment process.
    - **Task**: Investigate and fix any issues reported by the Cloud Manager pipeline.

