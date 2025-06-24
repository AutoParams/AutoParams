---
mode: agent
---

# Write Code to Implement a Test Scenario

Before writing code, read the [Coding Guidelines](../../docs/contexts/coding-guidelines.md) and explain which steps you will follow. Then, check that your explanation meets the following criteria:

- Do not implement multiple scenarios
- Follow TDD Guidelines from coding guidelines for reviews and process

If all criteria are satisfied, ask whether to proceed.

## TDD Compliance Reminders

**CRITICAL: Follow these steps in order and DO NOT skip any step:**

1. **Before writing ANY test code**: Request review approval
2. **Before running ANY test**: Request review approval
3. **Use correct test command**: `./gradlew :{module}:test --tests {fullTestClassName}`
4. **After test passes**: Request review approval
5. **Mark scenario complete**: Update the scenario list with `[x]`

**Never:**
- Write tests without prior review approval
- Run tests without prior review approval
- Modify failed tests (they will be updated manually)
- Write implementation without corresponding failing tests

Select the first uncompleted test scenario from the available list.

{{#if feature}}
Focus on the test scenario most relevant to: **{{feature}}**
{{/if}}
