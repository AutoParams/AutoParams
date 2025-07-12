# Documentation Improvements

## Problem Statement
1. The project lacks sufficient documentation, making it difficult for new developers to understand how to use it.
2. All information is contained in a single README.md file, which reduces readability.
3. Not all features are documented, making it hard for new developers to use the available functionality.
4. Writing tests in the test-autoparams-springboot project to verify that example code is actually runnable does not align with the original purpose of the project.

## Proposed Solution

### 1. Documentation Architecture Transformation

**Current State**:
- Single monolithic README.md file (1,096 lines)
- Mixed concerns: overview, installation, features, examples all in one file
- Difficult to navigate and maintain

**Target Architecture**:
```
README.md (Overview & Quick Start)
├── docs/
│   ├── getting-started/        # New user onboarding
│   │   ├── installation.md
│   │   └── quick-start.md
│   ├── basics/                 # Essential AutoParams functionality
│   │   ├── logging.md
│   │   ├── annotations.md
│   │   ├── factory.md
│   │   ├── design.md
│   │   └── resolution-context.md
│   ├── advanced-features/      # Power user features
│   │   ├── customization.md
│   │   ├── freezing-strategies.md
│   │   └── parameterized-tests.md
│   ├── extensions/             # Framework integrations
│   │   ├── spring-integration.md
│   │   ├── mockito-integration.md
│   │   ├── lombok-integration.md
│   │   └── kotlin-support.md
│   ├── guides/                 # How-to and reference
│   │   ├── migration-guide.md
│   │   ├── best-practices.md
│   │   └── troubleshooting.md
│   └── api-reference/          # Technical reference
│       ├── annotations.md
│       ├── generators.md
│       └── customizers.md
└── examples/                   # Documentation Examples Subproject
    ├── build.gradle.kts
    └── src/
        ├── main/java/examples/     # Domain models (created incrementally)
        └── test/java/examples/     # Runnable examples
            ├── gettingstarted/
            └── basics/
```

### 2. Examples Subproject Solution

**Purpose**: Dedicated testing environment for documentation examples that solves Problem #4.

**Key Components**:
- Gradle subproject integrated with root project build system
- Domain models created incrementally as needed for examples
- Runnable test classes organized by feature category
- CI validation ensures documentation accuracy
- Clear separation from library's internal tests

**Benefits**:
- Removes documentation pollution from test-autoparams-springboot
- Provides single source of truth for all code examples
- Enables automated validation of documentation
- Makes examples easily discoverable and executable

### 3. Content Standards & Guidelines

**Writing Principles**:
- Progressive disclosure: basic → advanced concepts
- Practical examples with real-world domain models
- Clear next steps
- Consistent voice and terminology

**Structure Template**:
```markdown
# Feature Name
## Overview
## How to Use
## [Advanced Feature Topics as ## sections]
## Next Steps
```

**Documentation Structure Guidelines**:
- Use `## Overview` for introduction and key concepts
- Use `## How to Use` for essential functionality with examples
- Advanced content should be organized as separate `##` level sections (e.g., `## Customizations with DSL`, `## Stream Processing`, etc.) rather than grouped under `## Advanced Patterns`
- Use `## Next Steps` for navigation to related documentation

**Code Example Requirements**:
- All examples executable via examples subproject
- Include both Java and Kotlin where applicable
- Create domain models incrementally as needed for specific examples
- Provide expected output and explanations

**Domain Model Standards**:
- Keep domain models simple and focused on their purpose
- Avoid unnecessary JavaDoc comments in domain model classes
- Use clear, descriptive property names that explain their purpose
- Follow immutable object patterns with final fields and getter methods

**Quality Validation Rules**:
- After any modification to the examples subproject, run `./gradlew :examples:build` to ensure all examples compile and tests pass
- All files must end with a final newline
- Follow existing code style and formatting conventions

**Documentation Cross-Reference Rules**:
- Next Steps sections should only link to existing documents
- When a new document is created, analyze and update Next Steps in related documents to include appropriate links
- Maintain logical progression: getting-started → basics → advanced-features → extensions

### 4. Implementation Strategy

**Phase 1: Foundation**
- Set up directory structure and examples subproject
- Migrate core getting-started content with basic examples
- Create domain models incrementally as needed

**Phase 2: Basic Features**
- Document Factory, Designer, and ResolutionContext
- Create comprehensive annotation reference
- Build examples for fundamental features

**Phase 3: Advanced & Extensions**
- Document customization patterns
- Create extension-specific guides
- Add troubleshooting and best practices

### 5. Quality Assurance

**Automated Validation**:
- CI pipeline runs all examples in docs/examples/ subproject
- Link checking for internal documentation references
- Version alignment between docs and library releases

**Manual Review Process**:
- Peer review for new documentation
- User testing with actual developers
- Regular audits for accuracy and completeness

This structured approach will transform the current single-file documentation into a comprehensive, maintainable documentation system that scales with the project's growth and makes AutoParams accessible to developers of all experience levels.

## Next Actions (Immediate Implementation Tasks)

The following tasks represent the first 5 priority items to begin implementation:

- [x] **Task 1: Initialize basic docs/ directory structure**
  - Create docs/ directory if it doesn't exist
  - Set up getting-started/ directory only
  - Other directories will be created as needed during implementation

- [x] **Task 2: Set up docs/examples/ subproject with build.gradle.kts**
  - Create subproject as part of root project (not independent)
  - Add to root settings.gradle.kts as included subproject
  - Configure Java 17 as source and target compatibility
  - Configure with Spring Boot 3.2.5 plugin (org.springframework.boot)
  - Configure with Spring Dependency Management 1.1.4 plugin (io.spring.dependency-management)
  - Disable javadoc task (not needed for examples project)
  - Configure dependencies for AutoParams and extensions
  - Set up proper source and test directory structure
  - Ensure `./gradlew build` command succeeds for entire project

- [x] **Task 3: Create installation and quick-start documentation**
  - Copy installation instructions from README.md to docs/getting-started/installation.md
  - Create focused quick-start guide with minimal viable example
  - Write runnable example test class for quick-start guide
  - Create domain models as needed for examples (incremental approach)
  - Note: README.md will remain unchanged until all docs/ work is complete

- [x] **Task 4: Create basic Factory documentation with examples**
  - [x] Establish structure for docs/basics/factory.md (including how to use and advanced patterns sections)
  - [x] Create domain model as needed for Factory examples (Product only)
  - [x] Write basic Factory creation examples:
    - [x] Factory creation: `Factory.create(Class<T>)` basic usage
    - [x] Generic Factory creation: `Factory.create()` with TypeReference for `List<T>`
    - [x] Type inference example: `Factory.create()` with implicit types
    - [x] Diamond operator example: `TypeReference<>` with inferred generic types
  - [x] Write automatic injection examples:
    - [x] Test parameter injection: `@AutoSource` with Factory parameters
    - [x] Generic Factory parameter injection: `Factory<List<T>>` as test parameters
  - [x] Write object generation examples:
    - [x] Single object generation: `factory.get()` method
    - [x] Multiple object generation: `factory.getRange(int size)` method
    - [x] Stream generation: `factory.stream().limit(n)` pattern
  - [x] Write customization examples:
    - [x] Using ArgumentCustomizationDsl.set method with Factory

- [x] **Task 5: Create basic logging documentation with examples**
  - [x] Establish structure for docs/basics/logging.md (including how to use sections)
  - [x] Write basic logging setup examples:
    - [x] Simplest approach: `@LogResolution` annotation on test methods (including hierarchical tree structure and timing information explanation)
    - [x] Programmatic approach: `ResolutionContext.enableLogging()` usage
    - [x] Default vs enabled logging behavior comparison
  - [x] Write advanced logging features:
    - [x] LogVisibility annotation usage for controlling log output
    - [x] Custom ObjectQuery.toLog() implementations

- [ ] **Task 6: Create basic Design documentation with examples**
  - [ ] Establish structure for docs/basics/design.md (including how to use and advanced patterns sections)
  - [ ] Write basic Design creation examples:
    - [ ] Design creation: `Design.of(Class<T>)` basic usage
    - [ ] Property configuration: `.set()` method for fixed values (including type-safe method references and `.instantiate()` usage)
    - [ ] Property configuration: `.supply()` method for dynamic values
  - [ ] Write advanced Design features:
    - [ ] Nested object configuration: `.design()` method for complex hierarchies
    - [ ] Multiple instance creation: `.instantiate(int count)` method
    - [ ] ResolutionContext integration: `.instantiate(ResolutionContext)` usage
    - [ ] Creating reusable Customizers: Design as ObjectGenerator for integration with AutoParams framework
  - [ ] Create domain models as needed for Design examples (if not already available)
  - [ ] Write comprehensive example test classes demonstrating all Design capabilities

These tasks can be adjusted or reordered based on project priorities and resource availability.

**Important Note**: README.md modifications will be performed as the final step after all docs/ directory work is completed. During the initial documentation creation phase, content will be copied from README.md without modifying the original file. README.md restructuring will occur only after the new documentation system is fully established and validated.
