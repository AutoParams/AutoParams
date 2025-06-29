Run full build process with quality checks and error resolution

## Syntax

```
/build
```

## Examples

```
/build
```

## Execution Workflow
1. **Execute Build**: Run `./gradlew build` with 10-minute Bash timeout (applies CLAUDE.md build guidelines)
2. **Monitor Output**: Continuously watch for errors, failures, and warnings
3. **Apply Error Resolution**: Follow systematic error resolution from CLAUDE.md guidelines
4. **Verification**: Re-run build to confirm fixes resolve issues
5. **Report Status**: Provide comprehensive build status

## Error Handling
- **Maximum Retries**: Limit to 3 build attempts to prevent infinite loops
- **Clean Recovery**: Reset build state if necessary between retry attempts
- **Failure Documentation**: Document persistent issues requiring manual intervention

## Success Criteria
- All compilation succeeds without errors
- All tests pass successfully
- No code quality violations
- All expected artifacts generated
