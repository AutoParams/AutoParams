# Coding Guidelines

## General Guidelines

- Do not try to do too much at once. Work in the smallest possible increments, and request a review after completing each unit of work.
- Do not write Javadoc and other comments unless explicitly requested. Write self-explanatory code instead.

## Test-Driven Development

Use Test-Driven Development (TDD) methodology when implementing new features or fixing bugs.

### TDD Steps

1. Write a list of the test scenarios you want to cover
2. Turn exactly one item on the list into an actual, concrete, runnable test
3. Change the code to make the test (& all previous tests) pass (adding items to the list as you discover them)
4. Optionally refactor to improve the implementation design
5. Until the list is empty, go back to #2

### TDD Guidelines

- Use the test scenario phrase as the test method name, replacing spaces with underscores (_).
- Before writing tests, define minimal method signatures (empty methods, methods returning null, or methods returning default values) to prevent compilation errors.
- Request a review before writing and running tests.
- Use the command `./gradlew :{module}:test --tests {fullTestClassName}` to run tests within a specific test class.
- If a test fails, understand the reason for the failure and modify the code to make the test pass.
- If you fail to make the test pass after two attempts, stop working and get confirmation before making further attempts.
- Request a review after the test passes.
- Never modify a failed test. If a test needs to be changed, it will be updated manually.
- When a test passes, mark the corresponding test scenario as completed. For example: `- [x] test scenario description`.
- Never write implementation without corresponding tests. Only add code that is driven by failing tests.

## Code Style Guidelines

All code must follow consistent formatting standards to ensure readability and maintainability across the project.

### Core Formatting Rules

- **Method and Class Member Separation**: Use exactly one blank line to separate methods, constructors, and other class members
- **Annotation Placement**: Place all annotations (`@Test`, `@Override`, etc.) on their own line, never on the same line as method signatures or class declarations
- **EditorConfig Compliance**: Follow all formatting rules defined in the project's `.editorconfig` file

### EditorConfig Standards

The project enforces consistent coding standards through `.editorconfig`. Key formatting requirements for Java files:

**File Structure:**
- **Indentation**: 4 spaces (never tabs)
- **Character encoding**: UTF-8
- **Max line length**: 100 characters
- **Final newline**: Always required

**Code Layout:**
- **Method spacing**: 1 blank line around methods (`ij_java_blank_lines_around_method = 1`)
- **Class spacing**: 1 blank line around classes (`ij_java_blank_lines_around_class = 1`)
- **Annotation wrapping**: Split annotations into separate lines (`ij_java_method_annotation_wrap = split_into_lines`)

### Pre-Edit Checklist

Before making any code changes, ensure you understand:

1. The existing code structure and formatting patterns
2. Which methods or classes will be affected
3. The proper spacing requirements for the code section

### Post-Edit Verification

After completing any code changes, verify the following:

1. **Method Separation**: Every method is separated from other methods/class members by exactly one blank line
2. **Annotation Placement**: All annotations are on their own line, separate from method signatures or class declarations
3. **Class Member Spacing**: Fields, constructors, and methods have appropriate blank line separation
4. **Import Organization**: Imports are properly organized and unused imports are removed
5. **Indentation Consistency**: All Java code uses 4 spaces for indentation (not tabs or 2 spaces)
6. **Pattern Consistency**: New code follows the same formatting pattern as existing code in the file
7. **Line Integrity**: No lines were accidentally merged or had spacing removed during editing
