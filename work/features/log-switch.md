# Log Switch

Both the log output target and whether to output logs are controlled through the `LogWriter` provided when creating the `ResolutionContext`. This design can lead to ambiguity and lacks flexibility. For example, if you want to output logs only for specific code while using a particular `LogWriter`, you have to create a new `ResolutionContext`.

## Proposed Solution
You can add a `void enableLogging()` method to the `ResolutionContext` class to activate log output. Logs will not be output until this method is called. After calling this method, logs will be output through the `LogWriter`.

The `@LogResolution` annotation internally calls the `enableLogging()` method.

Remove the `NoOpLogWriter` class, and always use `ConsoleLogWriter` by default in the `ResolutionContext` constructor unless a different `LogWriter` is explicitly specified.

```java
@Test
@AutoParams
@LogResolution
void testMethod1(String value) {
    // Logs for the String value parameter are output
}

@Test
void testMethod2() {
    var context = new ResolutionContext();
    context.enableLogging();
    String value = context.resolve();
    // Logs for the String value are output
}
```

## Implementation Steps

### 1. Add enableLogging Method to ResolutionContext

Add the `enableLogging()` method to ResolutionContext to activate log output. Logs will not be output until this method is called.

**Target Class:** `autoparams.ResolutionContext`

**New Method:**
- `void enableLogging()` - Enable logging with console output

**Test Class:** `test.autoparams.SpecsForResolutionContext`

**Test Scenarios:**
- [x] enableLogging activates console logging
- [x] logging is disabled by default
- [x] logging state persists across multiple resolve operations
- [x] enableLogging can be called multiple times safely

## Implementation History

### Development Process
1. **Initial Implementation**: Added `enableLogging()` method to ResolutionContext and basic logging state management in ResolutionLogger
2. **Test Development**: Implemented all 4 test scenarios using TDD methodology - each test was written first to fail, then implementation was added to make it pass
3. **Build Integration Issue**: Initial build failed due to existing logging tests expecting logs to be output by default
4. **Root Cause Analysis**: Discovered that `@LogResolution` annotation was not automatically calling `enableLogging()` method
5. **Critical Fix**: Modified `TestResolutionContext.create()` to automatically call `enableLogging()` when `@LogResolution` annotation is present
6. **Checkstyle Fixes**: Resolved import order and trailing space violations to meet code quality standards

### Key Technical Decisions
- **Backward Compatibility**: Ensured existing `@LogResolution` annotation behavior remains unchanged for users
- **Default Behavior Change**: Changed default constructor to use `ConsoleLogWriter` instead of `NoOpLogWriter` while keeping logging disabled by default
- **State Management**: Added `enabled` boolean flag to `ResolutionLogger` to control logging output separate from LogWriter selection

### Challenges Resolved
- **Test Failures**: 28 existing logging tests initially failed because logging was disabled by default
- **Annotation Integration**: Required understanding of how `@LogResolution` annotation is processed in `TestResolutionContext`
- **Build Quality**: Fixed checkstyle violations including import ordering and trailing whitespace

### Final Status
- ✅ All 4 new test scenarios implemented and passing
- ✅ Full build success with 344 tests passing
- ✅ Backward compatibility maintained with existing `@LogResolution` usage
- ✅ Code quality standards met (checkstyle passing)

## Backlogs

- [x] Remove NoOpLogWriter class after migration is complete
