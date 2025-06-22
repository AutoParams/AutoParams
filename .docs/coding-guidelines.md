# Coding Guidelines

Use Test-Driven Development (TDD) methodology when implementing new features or fixing bugs.

## TDD Steps

1. Write a list of the test scenarios you want to cover
2. Turn exactly one item on the list into an actual, concrete, runnable test
3. Change the code to make the test (& all previous tests) pass (adding items to the list as you discover them)
4. Optionally refactor to improve the implementation design
5. Until the list is empty, go back to #2

## Guidelines Details

- Use the test scenario phrase as the test method name, replacing spaces with underscores (_).
- Request a review before writing and running tests.
- Use the command `./gradlew :{module}:test --tests {fullTestClassName}` to run tests within a specific test class.
- If a test fails, understand the reason for the failure and modify the code to make the test pass.
- If you fail to make the test pass after two attempts, stop working and get confirmation before making further attempts.
- Request a review after the test passes.
