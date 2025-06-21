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
 * &gt; Resolving: for class com.example.MyClass
 * |-- &gt; Resolving: for Parameter java.lang.String value1
 * |   &lt; Resolved(2 ms): "generated string" for Parameter java.lang.String value1
 * |-- &gt; Resolving: for Parameter java.lang.Integer value2
 * |   &lt; Resolved(&lt;1 ms): 42 for Parameter java.lang.Integer value2
 * &lt; Resolved(3 ms): MyClass{...} for Parameter class com.example.MyClass
 * </pre>
 *
 * @see autoparams.AutoParams
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogResolution {
}
