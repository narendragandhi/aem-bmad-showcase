# Contributing to AEM BMAD Showcase

We welcome contributions to improve this showcase project and the BMAD methodology documentation. Please follow these guidelines to ensure a smooth collaboration process.

## How to Contribute

1.  **Reporting Issues**: If you find a bug, a typo, or an inconsistency, please [create an issue](https://github.com/narendragandhi/aem-bmad-showcase/issues) on GitHub. Provide as much detail as possible, including steps to reproduce the issue.
2.  **Feature Requests**: If you have an idea for a new feature or an improvement to the methodology, create an issue and label it as an "enhancement".
3.  **Pull Requests**: We welcome pull requests for bug fixes and approved features.

## Pull Request Process

1.  **Fork the repository** and create your branch from `main`.
2.  **Make your changes**. Ensure you follow the coding standards and update documentation where necessary.
3.  **Ensure all tests pass**. Run `mvn clean verify` to execute the full test suite.
4.  **Submit a pull request** to the `main` branch of the original repository.
5.  **Provide a clear description** of your changes in the pull request. Reference any related issues.

## Development Setup

Please refer to the `GETTING-STARTED.md` file for instructions on how to set up your local development environment.

## Coding Standards

-   **Java**: Follow standard AEM best practices. Use the `com.adobe.granite.workflow` APIs, Sling Models, and OSGi annotations.
-   **HTL**: Keep logic minimal. Use `data-sly-use` for Sling Models and keep markup clean and semantic.
-   **Documentation**: Write clear and concise documentation. All new features should be documented.

Thank you for contributing!
