# Installation

This guide shows you how to add AutoParams to your Java project.

## Requirements

- JDK 1.8 or higher
- JUnit 5

## Maven

For Maven, add the following dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>io.github.autoparams</groupId>
  <artifactId>autoparams</artifactId>
  <version>11.1.0</version>
  <scope>test</scope>
</dependency>
```

## Gradle

For Gradle, add to your `build.gradle`:

```groovy
testImplementation 'io.github.autoparams:autoparams:11.1.0'
```

Or for Gradle with Kotlin DSL (`build.gradle.kts`):

```kotlin
testImplementation("io.github.autoparams:autoparams:11.1.0")
```

## Verify Installation

After adding the dependency, create a simple test to verify AutoParams is working:

```java
import org.junit.jupiter.api.Test;
import autoparams.AutoParams;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InstallationTest {

    @Test
    @AutoParams
    void autoParamsWorking(String value) {
        assertNotNull(value);
    }
}
```

If the test passes, AutoParams is successfully installed and ready to use.

## Next Steps

- [Quick Start Guide](quick-start.md) - Learn the basics with simple examples
