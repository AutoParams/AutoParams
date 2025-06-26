Implement features using TDD methodology - /code feature: <feature>

## Syntax

```
/code feature: <feature>
```

**Parameters:**
- `feature` (required): The name or file path of the feature to implement

## Examples

```
/code feature: Factory
/code feature: Designer
/code feature: ArgumentRecycler
```

## Execution Workflow

Execute these steps in order:

1. **Read Guidelines**: First read [coding guidelines](../../work/contexts/coding-guidelines.md) to review TDD methodology, guidelines, and workflow
2. **Select Scenario**: Choose one incomplete test scenario from the pre-prepared list
3. **Define Method Signature**: If needed, define minimal method signature without implementation (TDD Workflow step 2)
4. **Request Review**: Get confirmation if method signature was defined (TDD Workflow step 3)
5. **Write One Test**: Convert selected scenario into concrete, executable test (TDD Step 2, Workflow step 4)
6. **Request Review**: Get confirmation before running the test (TDD Workflow step 4)
7. **Run Test**: Execute test to confirm it fails appropriately (TDD Workflow step 5-6)
8. **Implement Code**: Write minimal code to make test pass (TDD Step 3, Workflow step 7)
9. **Run Specific Test**: Use `./gradlew :{module}:test --tests {fullTestClassName}` (TDD Workflow step 8)
10. **Mark Complete**: Mark scenario as `- [x]` when build succeeds (TDD Workflow step 12)
11. **Repeat**: Go back to step 2 until all scenarios complete (TDD Step 5)
