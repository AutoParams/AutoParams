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

## Pre-Execution Validation
1. **Feature Scope**: Confirm feature name/path is valid and well-defined
2. **Test Scenarios**: Verify test scenario list exists and follows CLAUDE.md writing guidelines
3. **Module Structure**: Identify correct module and package structure
4. **Dependencies**: Check existing code dependencies and imports

## Execution Workflow
1. **Initialize**: Prepare test scenario list following CLAUDE.md test scenario guidelines
2. **Select Scenario**: Choose one incomplete test scenario from the pre-prepared list
3. **Validate Scenario**: Ensure scenario follows CLAUDE.md guidelines
4. **Follow TDD Workflow**: Execute CLAUDE.md TDD workflow steps 2-11 precisely
5. **Mark Complete**: Mark scenario as `- [x]` when build succeeds
6. **Log Progress**: Add or update Implementation History section with major events chronologically
7. **Continue**: Return to step 2 until all scenarios complete

## Error Handling
- **Test Failure Limit**: Stop after 3 attempts and request review (per CLAUDE.md TDD workflow)
- **Build Failure Recovery**: Apply CLAUDE.md build guidelines systematically
- **Review Gates**: Always request review at designated checkpoints

## Success Criteria
- All test scenarios converted to passing tests
- Code follows incremental development principles from CLAUDE.md
- Test scenario list properly maintained with completion status
