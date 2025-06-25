# Build Guidelines

## Build Process

### Full Build Execution

1. **Primary Build Command**: Execute `./gradlew build` for comprehensive project build
2. **Output Monitoring**: Continuously monitor build output for errors, failures, and warnings
3. **Error Resolution**: Apply systematic error resolution when issues are detected
4. **Status Reporting**: Provide clear final build status to user

### Error Resolution Workflow

When build errors occur:

1. **Error Identification**: Carefully analyze error messages and context
2. **Root Cause Analysis**: Determine the underlying cause of the failure
3. **Targeted Fixes**: Apply specific fixes based on error type and context
4. **Verification**: Re-run build to confirm fixes resolve the issues
5. **Iteration**: Repeat process if additional errors are discovered

### Common Error Categories

- **Compilation Errors**: Syntax issues, missing imports, type mismatches
- **Test Failures**: Unit test failures, integration test issues
- **Dependency Issues**: Missing dependencies, version conflicts
- **Configuration Problems**: Build script issues, plugin configuration
- **Code Quality Violations**: Checkstyle, static analysis warnings

### Build Output Analysis

- **Success Indicators**: Clean compilation, all tests passing, successful artifact generation
- **Warning Management**: Address warnings that could indicate potential issues
- **Performance Monitoring**: Track build time and resource usage patterns
- **Artifact Verification**: Ensure all expected outputs are generated correctly

## Best Practices

- Always run full build before committing changes
- Address all errors and warnings systematically
- Maintain clean build output with minimal noise
- Document any known build issues or workarounds
- Keep build scripts and configuration up to date
