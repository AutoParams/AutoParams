Run full build process with quality checks

## Syntax

```
/build
```

## Examples

```
/build
```

## Execution Workflow

Execute these steps in order:

1. **Read Guidelines**: First read [build guidelines](../../work/contexts/build-guidelines.md) to review build process
2. **Execute Build**: Run `./gradlew build` for comprehensive project build
3. **Monitor Output**: Continuously watch for errors, failures, and warnings
4. **Analyze Errors**: If errors occur, identify and analyze error messages and context
5. **Apply Fixes**: Implement targeted fixes based on error type (compilation, tests, dependencies, etc.)
6. **Re-run Build**: Execute build again to verify fixes resolve the issues
7. **Repeat Resolution**: Continue steps 4-6 until build succeeds
8. **Report Status**: Provide final build status and any remaining issues
