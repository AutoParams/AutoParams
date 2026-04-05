---
name: tdd-pass
description: Write minimum code to pass the failing test. Use after tdd-test to make the current failing test pass.
---

# TDD Pass — Make the Test Pass

Change the code to make the failing test (and all previous tests) pass.

## Workflow

### 1. Implement

- Forget everything about design, requirements, and architecture
- Look only at the failing test and write the minimum code to make it pass
- Do NOT add code that is not required by the failing test
- Do NOT add unnecessary if statements, error handling, or features

### 2. Verify

- Run the entire test class (not just the new test) to ensure all tests pass
- Run code style checks (e.g., checkstyle, lint) if available
- If tests or style checks fail, fix and retry (up to 3 attempts)
- If still failing after 3 attempts, STOP and request review

### 3. Update the List

- Mark the completed scenario as done (`- [x]`) in the test list
- If new scenarios were discovered during implementation, propose them to the user before adding to the list
- Report what was completed and what the next incomplete scenario is

## Important

- Always STOP and request review after this step
- Do NOT proceed to the next scenario automatically
