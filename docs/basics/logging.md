# Resolution Logging

## Overview

AutoParams provides comprehensive logging capabilities that help you understand how objects are resolved during test execution. Resolution logging shows you the complete object creation process, including constructor selection, parameter resolution, and timing information, displayed in a clear hierarchical tree format.

By default, logging is disabled for performance reasons. You can enable it using either the `@LogResolution` annotation for individual tests or programmatically through the `ResolutionContext.enableLogging()` method.

## How to Use

### Using @LogResolution Annotation

The simplest way to enable resolution logging is by adding the `@LogResolution` annotation to your test method:

```java
import autoparams.AutoParams;
import autoparams.LogResolution;
import org.junit.jupiter.api.Test;

@Test
@AutoParams
@LogResolution
void testWithLogging(User user) {
    assertNotNull(user);
}
```

When you run this test, AutoParams will output detailed resolution logs to the console:

```
User user (5ms)
 ├─ UUID id → fbdf7aa8-1af7-4308-bc64-ee9dbfeba8d2 (< 1ms)
 ├─ String email → 53bf56a3-8a42-47f3-a5c9-854862ea4a56@test.com (2ms)
 └─ String username → usernamec6962921-ab77-4dbc-a71d-6932a9faa5be (1ms)
```

### Understanding the Log Format

The hierarchical tree structure shows:

- **Root object**: `User user (5ms)` - the main object being resolved with total execution time
- **Dependencies**: Each line with `├─` or `└─` represents a dependency that was resolved
- **Values**: The `→` arrow shows the actual value that was generated for leaf nodes
- **Timing**: Numbers in parentheses show execution time in milliseconds

### Programmatic Logging Control

For more control over when logging is enabled, you can use `ResolutionContext` directly:

```java
@Test
void testWithProgrammaticLogging() {
    ResolutionContext context = new ResolutionContext();
    context.enableLogging();

    User user = context.resolve();

    assertNotNull(user);
}
```

### Default Behavior vs Enabled Logging

**Default behavior** (logging disabled):
- No console output
- Better performance for regular test execution
- Suitable for CI/CD environments

**Enabled logging**:
- Detailed resolution process output
- Performance timing information
- Helpful for debugging object resolution issues
- Understanding AutoParams behavior

## Advanced Features

### Controlling Log Visibility

Use the `@LogVisibility` annotation to control which types appear in resolution logs:

```java
@LogVisibility(verboseOnly = true)
public class InternalComponent {
    // This class will only appear in verbose logs
}
```

### Custom Log Formatting

Implement custom `toLog()` methods in your `ObjectQuery` implementations to customize how types appear in logs:

```java
public class CustomQuery implements ObjectQuery {
    @Override
    public String toLog(boolean verbose) {
        if (verbose) {
            return "Detailed: " + getType().getName();
        }
        return "Simple: " + getType().getSimpleName();
    }
}
```

**Note**: The `verbose` parameter in `toLog()` and `LogVisibility.verboseOnly()` are designed for future verbose logging functionality that is not yet implemented. Currently, all logging uses the non-verbose format.

## Next Steps

- Explore [Factory](factory.md) for object creation patterns
