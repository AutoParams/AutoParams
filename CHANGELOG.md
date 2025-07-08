# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [11.2.1] - 2025-07-09

### Fixed
- Improved support for wildcard generic types (e.g., `List<? extends Number>`) in parameter generation

## [11.2.0] - 2025-06-29

### Added
- Added `enableLogging()` method to `ResolutionContext` to control resolution logging
- Added `toLog(boolean)` method to `ObjectQuery` interface for customizable logging format
- Added `LogVisibility` annotation to control which types appear in resolution logs

### Changed
- Enhanced resolution logging with cleaner hierarchical format and improved readability:
  ```
  // Before (11.1.x):
  > Resolving: for Parameter your.app.User user
  |-- > Resolving: for Parameter final java.util.UUID id
  |   < Resolved(<1 ms): fbdf7aa8-1af7-4308... for Parameter final java.util.UUID id
  |-- > Resolving: for Parameter final java.lang.String email
  |   < Resolved(2 ms): 53bf56a3-8a42-47f3...@test.com for Parameter final java.lang.String email
  < Resolved(5 ms): User(id=fbdf7aa8..., email=53bf56a3...@test.com) for Parameter your.app.User user
  
  // After (11.2.0):
  User user (5ms)
   ├─ UUID id → fbdf7aa8-1af7-4308... (< 1ms)
   └─ String email → 53bf56a3-8a42-47f3...@test.com (2ms)
  ```
- Modified `ResolutionContext` constructor to disable logging by default - call `enableLogging()` to enable

## Previous Releases

For changes in previous versions, please refer to the [GitHub Releases](https://github.com/AutoParams/AutoParams/releases) page.
