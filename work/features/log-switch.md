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
- [ ] enableLogging activates console logging
- [ ] logging is disabled by default
- [ ] logging state persists across multiple resolve operations
- [ ] enableLogging can be called multiple times safely

## Backlogs

- [ ] Remove NoOpLogWriter class after migration is complete
