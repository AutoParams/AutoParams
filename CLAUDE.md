# AutoParams Project Context

AutoParams is a Java/Kotlin library for automatic parameter generation in unit tests, supporting JUnit 5 and various frameworks like Spring, Mockito, and Lombok.

## Commit Guidelines

### Commit Message Format
Follow the guidelines in [docs/contexts/commit-message-guidelines.md](docs/contexts/commit-message-guidelines.md)

### Commit Workflow
Follow the structured workflow in [.github/prompts/commit.prompt.md](.github/prompts/commit.prompt.md):
1. Review staged changes with `git diff --staged`
2. Draft commit message following guidelines
3. Validate subject line length (≤50 characters)
4. Review and commit

### Commit Command
When you say "commit", Claude will:
1. Follow the structured workflow from `.github/prompts/commit.prompt.md`
2. Apply guidelines from `docs/contexts/commit-message-guidelines.md`
3. Review **only staged changes** with `git diff --staged`
4. Generate appropriate commit message based on staged changes only
5. Present complete commit message (subject + body) for user approval
6. Validate message format and length
7. Execute the commit only after user confirmation
8. **Never include Claude Code promotional content or co-authorship attribution**

For topic-focused commits, use: **"commit topic: [topic]"**
- Example: "commit topic: Designer API"
- Example: "commit topic: Kotlin support"
- This will focus the commit message on changes related to the specified topic

## Code Style
- Use IntelliJ formatter: `config/formatter/intellij-java-google-style.xml`
- Checkstyle configuration: `config/checkstyle/checkstyle.xml`

## Coding Guidelines
Follow the guidelines in [docs/contexts/coding-guidelines.md](docs/contexts/coding-guidelines.md):

### General Principles
- Work in smallest possible increments, request review after each unit
- Write self-explanatory code, avoid comments unless requested

### TDD Methodology
- Write test scenarios list → implement one test → make test pass → refactor
- Use test scenario phrase as method name (spaces → underscores)
- Define minimal method signatures before writing tests
- Run tests with: `./gradlew :{module}:test --tests {fullTestClassName}`
- Never modify failed tests, only implementation code
- Mark completed scenarios: `- [x] test scenario description`

### Code Formatting
- Method separation: exactly one blank line between methods/class members
- Annotations: always on separate line from method signatures
- Indentation: 4 spaces (never tabs), UTF-8 encoding, 100 char line limit
- Follow `.editorconfig` standards for consistent formatting

## Test Scenario Guidelines
Follow the guidelines in [docs/contexts/test-scenario-guidelines.md](docs/contexts/test-scenario-guidelines.md):
- Write one scenario at a time (most important first)
- Write in a single sentence using present tense
- Write in English, refer to system under test as 'sut'
- Do not start with capital letter (for test method names)
- Write concisely while preserving meaning
- Format as to-do item (- [ ])

## Code Implementation Command
When you say "code", Claude will:
1. Follow the TDD workflow from [.github/prompts/code.prompt.md](.github/prompts/code.prompt.md)
2. Read coding guidelines and explain implementation steps
3. Select first uncompleted test scenario from available list
4. Follow strict TDD compliance steps in order:
   - Request review approval before writing test code
   - Request review approval before running test
   - Use correct test command: `./gradlew :{module}:test --tests {fullTestClassName}`
   - Request review approval after test passes
   - Mark scenario complete with `[x]`
5. Never write tests or run tests without prior review approval
6. Never modify failed tests, only implementation code

For feature-focused implementation, use: **"code feature: [feature]"**
- This will focus on test scenarios most relevant to the specified feature

## Build Command
When you say "build", Claude will:
1. Execute `./gradlew build` to run the full build process
2. Monitor build output for errors, failures, or warnings
3. If build fails, analyze error messages and identify root causes
4. Apply appropriate fixes for common build issues:
   - Compilation errors: Resolve syntax and dependency issues
   - Test failures: Analyze and fix failing tests
   - Checkstyle violations: Fix code formatting and style issues
   - Lint warnings: Address code quality warnings
5. Re-run build after fixes to verify resolution
6. Report build status and any remaining issues

The build command ensures code quality by running:
- Compilation for all modules
- All test suites
- Code style checks (Checkstyle)
- Static analysis and linting
