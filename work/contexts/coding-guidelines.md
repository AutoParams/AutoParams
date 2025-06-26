# Coding Guidelines

## General Guidelines

- Do not try to do too much at once. Work in the smallest possible increments, and request a review after completing each unit of work.
- Do not write Javadoc and other comments unless explicitly requested. Write self-explanatory code instead.
- Exception messages for argument validation should start with "The argument 'argumentName'" (e.g., "The argument 'count' must not be less than 0").

## Test-Driven Development

Use Test-Driven Development (TDD) methodology when implementing new features or fixing bugs.

### TDD Steps

1. Write a list of the test scenarios you want to cover
2. Turn exactly one item on the list into an actual, concrete, runnable test
3. Change the code to make the test (& all previous tests) pass (adding items to the list as you discover them)
4. Optionally refactor to improve the implementation design
5. Until the list is empty, go back to #2

### TDD Guidelines

- The list of test scenario must be prepared in advance.
- Never modify a failed test. If a test needs to be changed, update it manually.
- Do not write code that is not required to pass a test. Only add code driven by a failing test.
- Set the timeout to 10 minutes when running Gradle commands.

### TDD Workflow

1. Select one incomplete test scenario.
2. If the minimum method signature required to implement the selected test scenario is not defined, define an empty method or a method that returns a default value. At this stage, only define the method signature without comments or implementation.
3. If you defined the method signature in step 2, request a review. Do not write the test at this stage.
4. Convert the selected test scenario from step 1 into a concrete, executable test and request a review.
5. Run the test and check the result.
6. Confirm that the test fails. If the test passes, stop working and request a review.
7. Understand the cause of the test failure and modify the code to make the test pass.
8. Run the test for the specific test class containing the added test using the command `./gradlew :{module}:test --tests {fullTestClassName}`.
9. If the test does not pass after three attempts, stop working and request a review.
10. If the test passes, run the full build including compilation, tests, and style checks using `./gradlew :{module}:build`.
11. If the build fails, analyze the cause and apply the necessary fixes. Repeat steps 10 and 11 until the build succeeds.
12. When the build succeeds, mark the test scenario as completed using checkbox format: `- [x] test scenario description`.
