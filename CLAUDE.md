# AutoParams Project Context

AutoParams is a Java/Kotlin library for automatic parameter generation in unit tests, supporting JUnit 5 and various frameworks like Spring, Mockito, and Lombok.

## Core Development Principles
- Follow Test-Driven Development (TDD) methodology
- Work in smallest possible increments, request review after each unit
- Write self-explanatory code, avoid comments unless requested

## Development Guidelines

### Test Scenario Writing
1. Write the most important scenario to be added, one at a time.
2. Write in a single sentence.
3. Write in English.
4. Use the present tense.
5. Refer to the system under test as 'sut'.
6. Do not start with a capital letter so it can be used as a test method name.
7. Write as concisely as possible while preserving the meaning.
8. Write as a to-do item (- [ ]).

### Coding Standards
- Do not try to do too much at once. Work in the smallest possible increments, and request a review after completing each unit of work.
- Do not write Javadoc and other comments unless explicitly requested. Write self-explanatory code instead.
- Exception messages for argument validation should start with "The argument 'argumentName'" (e.g., "The argument 'count' must not be less than 0").
- Always add a final newline when creating a new code file.
- Set the Bash tool timeout to 10 minutes (600000ms) when running Gradle commands.
- Do not use 'clazz' as a variable name for values of type `Class<?>`.

### Test-Driven Development (TDD)

#### TDD Process
1. Write a list of the test scenarios you want to cover
2. Turn exactly one item on the list into an actual, concrete, runnable test
3. Change the code to make the test (& all previous tests) pass (adding items to the list as you discover them)
4. Optionally refactor to improve the implementation design
5. Until the list is empty, go back to #2

#### TDD Rules
- The list of test scenario must be prepared in advance.
- Do not write more than one test at a time.
- Do not write code that is not required (for example, unnecessary if statements) to pass a test. Only add code driven by a failing test.
- Never modify a failed test. If a test needs to be changed, update it manually.

#### TDD Implementation Workflow
1. Select one incomplete test scenario.
2. If the minimum method signature required to implement the selected test scenario is not defined, define an empty method or a method that returns a default value. At this stage, only define the method signature without comments or implementation.
3. If you defined the method signature in step 2, request a review. Do not write the test at this stage.
4. Convert the selected test scenario from step 1 into a concrete, executable test and request a review.
5. Run the test and check the result.
6. Confirm that the test fails. If the test passes, stop working and request a review.
7. Understand the cause of the test failure and modify the code to make the test pass.
8. Run the test for the specific test class containing the added test using the command `./gradlew :{module}:test --tests {fullTestClassName}`.
9. Do not run individual test methods. Always run the entire test class to ensure all tests pass together.
10. If the test does not pass after three attempts, stop working and request a review.
11. If the build fails, analyze the cause and apply the necessary fixes. Repeat steps 10 and 11 until the build succeeds.
12. When the build succeeds, mark the test scenario as completed using checkbox format: `- [x] test scenario description`.

## Quality Assurance Guidelines

### Commit Standards
1. Write in English.
2. Use the present tense in the subject line (e.g., "Add feature" not "Added feature").
3. Keep the subject line to 50 characters or less.
4. Add a blank line between the subject and body.
5. Keep the body to 72 characters or less per line.
6. Within a paragraph, only break lines when the text exceeds 72 characters.
7. Describe changes to public API features and do not include implementation details such as package-private code.
8. Do not mention test code in commit messages.
9. Do not use any prefix (such as "fix:", "update:", "docs:", "feat:", etc.) in the subject line.
10. Do not start the subject line with a lowercase letter unless the first word is a function name or another identifier that is conventionally lowercase and there is a clear, justifiable reason for the exception. Otherwise, always start with an uppercase letter.
11. Do not include tool advertisements, branding, or promotional content in commit messages.
12. `/commit` command works only with already staged changes - never stage additional files during commit execution.
13. Ensure all intended changes are staged before invoking `/commit` command.
14. Use separate git commands to stage files before committing.
15. Always validate commits using `work/scripts/check-commit-message-rule.sh` and fix until validation passes.

### Build Standards
- Execute `./gradlew build` with 10-minute Bash tool timeout (600000ms) for comprehensive builds
- Always run full build before committing changes
- Address all errors and warnings systematically
- Categorize errors: compilation, test failures, dependency issues, configuration problems, code quality violations
- Apply systematic error resolution with targeted fixes

### Documentation Standards (Javadoc)
- Write in English.
- Never modify or remove source code.
- Focus on functionality, not implementation.
- Escape `@` in code examples with `&#64;`.
- Escape `<` with `&lt;`.
- Escape `>` with `&gt;`.
- Use `@link` for links to classes, methods, and fields.
- Do not use `{@code` in example code.
- Bold the title of the example code.
- Add a short description of the code example. Wrap the description so that it is no longer than 80 characters.
- Do not write `@author` tags.
- Add `@see` tags for closely related types.
- Close `<p>` with `</p>` correctly.
- Write Javadoc for one type or one member at a time. Do not write Javadoc for a type and its members at the same time.
- Do not mention non-public types or members in Javadoc.
- When writing Javadoc for the types in the `autoparams.internal` package and its subpackages, mention that the types are code for internal implementation purposes and are not safe for external use because their interface and behavior can change at any time.
- Keep Javadoc to a minimum for the types in the `autoparams.internal` package and subpackages.
- When linking to generic types, include the generic signature. Example: `{@link Function Function&lt;T, R&gt;}`.
- Use `<b>` for bold text instead of `<strong>`.

## Workflow Management

### Backlog Processing
- Process exactly one task per `/exec` command invocation
- Execute tasks in sequential order, never skip or reorder
- Follow all sub-requirements listed under each task
- Request user confirmation before marking tasks complete
- Maintain original status if task execution fails

### Command Integration
- Always read and follow command-specific workflows defined in `.claude/commands/` directory
- Use appropriate validation steps before command execution
- Apply consistent error handling across all commands
- Maintain proper progress reporting and user confirmation patterns
- `/build` command should be run before `/commit` to ensure code quality
- `/code` command output should be validated with `/build` before proceeding
- `/exec` command can coordinate with other commands based on backlog task requirements
- All commands should respect the 10-minute Bash tool timeout guideline when running Gradle commands
