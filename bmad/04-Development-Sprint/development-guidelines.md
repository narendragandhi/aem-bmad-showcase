# Development Guidelines

This document contains the coding standards, branching strategy, and other development guidelines for the project.

## Coding Standards

- **Java**: Follow the Google Java Style Guide.
- **HTL**: Use the official AEM HTL style guide.
- **CSS**: Use the BEM naming convention.

## Branching Strategy

- **main**: This branch is for production releases and is protected.
- **develop**: This is the main development branch. All feature branches are merged into this branch.
- **feature/{ticket-number}**: Each new feature or bug fix should be developed in its own feature branch.

## Code Reviews

- All code must be reviewed by at least one other developer before being merged into the `develop` branch.
- The reviewer should check for correctness, adherence to coding standards, and test coverage.

## Local Development Environment

For local development, developers should use the AEM as a Cloud Service SDK. This allows developers to emulate the cloud environment on their local machines.

### Setup

1.  **Download the AEM SDK**: Download the AEM SDK from the Adobe Software Distribution portal.
2.  **Install the SDK**: Follow the instructions to install the SDK on your local machine.
3.  **Start the SDK**: Start the AEM author and publish instances.
4.  **Install the code**: Deploy the project code to the local SDK using the following command:

    ```bash
    mvn clean install -PautoInstallPackage
    ```

### Local vs. Cloud Environment

While the local SDK provides a good emulation of the cloud environment, there are some differences. Developers should be aware of these differences and should regularly deploy their code to a cloud development environment to ensure that it works as expected.

## Unit Testing

- All new Java code must have corresponding JUnit tests.
- The test coverage should be at least 80%.
