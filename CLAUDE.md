# AutoParams Project Context

AutoParams is a Java/Kotlin library for automatic parameter generation in unit tests, supporting JUnit 5 and various frameworks like Spring, Mockito, and Lombok.

## Development Guidelines

### Core Principles
- Follow Test-Driven Development (TDD) methodology
- Work in smallest possible increments, request review after each unit
- Write self-explanatory code, avoid comments unless requested

### Guidelines References
- **Coding Guidelines**: [work/contexts/coding-guidelines.md](work/contexts/coding-guidelines.md)
- **Test Scenarios**: [work/contexts/test-scenario-guidelines.md](work/contexts/test-scenario-guidelines.md)
- **Commit Guidelines**: [work/contexts/commit-guidelines.md](work/contexts/commit-guidelines.md)
- **JavaDoc Guidelines**: [work/contexts/javadoc-guidelines.md](work/contexts/javadoc-guidelines.md)

## Available Commands

### Code Implementation: `code`
Implements features using Test-Driven Development methodology.

**Usage:**
- `code feature: [feature]` - Focus on scenarios for specific feature

**Parameters:**
- **Required**: `feature: [feature]` - Target feature to implement

**Process:**
1. Validate parameters (error if no feature specified)
2. Follow TDD workflow from [coding guidelines](work/contexts/coding-guidelines.md)
3. Select first uncompleted test scenario from available list
4. Follow strict TDD compliance steps:
   - Request review approval before writing test code
   - Request review approval before running test
   - Use command: `./gradlew :{module}:test --tests {fullTestClassName}`
   - Request review approval after test passes
   - Mark scenario complete with `[x]`
5. Never modify failed tests, only implementation code

### Build & Quality: `build`
Runs full build process with quality checks and error resolution.

**Process:**
1. Execute `./gradlew build` for full build
2. Monitor output for errors, failures, warnings
3. Apply fixes for common issues:
   - Compilation errors and dependency issues
   - Test failures and assertion problems
   - Checkstyle violations and formatting issues
   - Static analysis warnings
4. Re-run build after fixes to verify resolution
5. Report final build status

**Quality Checks:**
- Compilation for all modules
- Complete test suite execution
- Code style checks (Checkstyle)
- Static analysis and linting

### Commit Changes: `commit`
Creates commits following project guidelines and workflow.

**Usage:**
- `commit` - Create commit for staged changes
- `commit topic: [topic]` - Focus commit message on specific topic

**Process:**
1. Review staged changes with `git diff --staged`
2. Apply guidelines from [commit guidelines](work/contexts/commit-guidelines.md)
3. Generate appropriate commit message based on staged changes only
4. Validate message format and length (≤50 character subject)
5. Present complete commit message for user approval
6. Execute commit only after user confirmation

### JavaDoc Documentation: `javadoc`
Writes JavaDoc documentation for Java classes and methods.

**Usage:**
- `javadoc [filepath]` - Document entire class
- `javadoc [filepath] method: [methodname]` - Document specific method

**Parameters:**
- **Required**: `[filepath]` - Target Java file path
- **Optional**: `method: [methodname]` - Specific method to document

**Process:**
1. Validate parameters (error if no file specified)
2. Follow guidelines from [JavaDoc guidelines](work/contexts/javadoc-guidelines.md)
3. Analyze file to understand purpose and functionality
4. Write documentation focusing on public API behavior
5. Apply proper JavaDoc formatting and escaping
6. Never modify existing source code, only add documentation

**Examples:**
```
javadoc src/main/java/autoparams/Generator.java
javadoc autoparams/ObjectGenerator.java method: generate
```

### Backlog Execution: `run`
Executes the next uncompleted task from a backlog file.

**Usage:**
- `run backlog: [filepath]` - Execute next uncompleted task from backlog

**Parameters:**
- **Required**: `backlog: [filepath]` - Target backlog file path

**Process:**
1. Validate parameters (error if no backlog specified)
2. Read the specified backlog file
3. Find the first uncompleted task marked with `- [ ]`
4. Execute exactly one task following its specific requirements
5. Request user confirmation before marking task as complete
6. Mark the completed task as `- [x]` in the file only after confirmation
7. Report completion status and next available task

**Task Execution Rules:**
- Execute only one task per command invocation
- Follow any sub-requirements listed under the main task
- Request user confirmation before updating task status
- Never skip tasks or execute out of order
- Provide clear progress updates during execution

**Examples:**
```
run backlog: work/backlogs/documentation.md
run backlog: work/backlogs/feature-requests.md
```
