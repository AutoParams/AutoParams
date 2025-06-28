package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables detailed logging of object resolution processes for debugging purposes.
 * <p>
 * When applied to a test method, this annotation instructs AutoParams to output
 * a hierarchical view of object creation and resolution timing. This is useful
 * for understanding how objects are resolved, debugging complex object graphs,
 * and analyzing performance of the resolution process.
 * </p>
 *
 * <p>The logging output includes:</p>
 * <ul>
 * <li>Hierarchical structure showing the order of object resolution</li>
 * <li>Information for each resolved object</li>
 * <li>Timing information showing how long each resolution took</li>
 * <li>Indentation to show the depth of nested object resolution</li>
 * </ul>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * &#64;Test
 * &#64;AutoParams
 * &#64;LogResolution
 * void testWithLogging(MyClass obj) {
 *     // Test implementation
 *     // Resolution logging will be output to console
 * }
 * </pre>
 *
 * <p><b>Example output:</b></p>
 * <pre>
 * MyClass obj (3ms)
 *  ├─ String value1 → "generated string" (2ms)
 *  └─ Integer value2 → 42 (&lt;1ms)
 * </pre>
 *
 * @see autoparams.AutoParams
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogResolution {
}
