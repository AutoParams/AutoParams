package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Annotation to enable automatic parameter injection for JUnit 5 test methods.
 * <p>
 * When this annotation is applied to a test method, {@link AutoParamsExtension}
 * automatically provides suitable values for the method parameters.
 * </p>
 *
 * <pre>
 * &#64;Test
 * &#64;AutoParams
 * void testMethod(String arg1, UUID arg2) {
 *     // arg1 and arg2 are automatically generated and injected.
 * }
 * </pre>
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(AutoParamsExtension.class)
public @interface AutoParams {
}
