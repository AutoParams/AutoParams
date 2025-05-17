package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Annotation to enable automatic argument generation for JUnit 5 parameterized
 * tests.
 * <p>
 * When this annotation is applied to a test method,
 * {@link AutoArgumentsProvider} automatically generates and injects suitable
 * values for each parameter.
 * </p>
 *
 * <pre>
 * &#64;ParameterizedTest
 * &#64;AutoSource
 * void testMethod(String arg1, int arg2) {
 *     // arg1 and arg2 are automatically generated and injected.
 * }
 * </pre>
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(AutoArgumentsProvider.class)
public @interface AutoSource {
}
