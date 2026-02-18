# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-02-17

### Added
- Created `LICENSE` file with MIT License.
- Created `CHANGELOG.md` to track project changes.
- Created `CONTRIBUTING.md` with initial guidelines.

### Changed
- **BREAKING CHANGE**: Updated project to be built with Java 21 instead of Java 11.
- Updated AEM SDK API version to `2026.2.24288.20260204T121510Z-260100` and Core Components to `2.25.0`.
- Updated project version from `1.0.0-SNAPSHOT` to `1.0.0`.
- Renamed Maven profile `autoInstallPackage` to `autoInstallSinglePackage` to align with documentation.
- Updated `GETTING-STARTED.md` to reflect Java 21 requirement and corrected build commands.

### Fixed
- Added missing `ui.content.sample`, `ui.tests`, and `dispatcher` modules to the main `pom.xml` to ensure a complete project build.

### Removed
- Removed ambiguous `src` directory from the project root.
