package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controls the visibility of resolution logging for annotated types.
 * <p>
 * This annotation can be applied to types to configure when their resolution
 * process should be logged during AutoParams object generation. By default,
 * all types are eligible for logging when resolution logging is enabled.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * // Infrastructure type logged only in verbose mode
 * &#64;LogVisibility(verboseOnly = true)
 * public class DatabaseConnection {
 *     // class implementation
 * }
 * </pre>
 *
 * @see LogResolution
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogVisibility {

    /**
     * Specifies whether logging for this type should only occur in verbose mode.
     * <p>
     * When set to {@code true}, the annotated type will only be logged when
     * verbose logging is specifically enabled. When set to {@code false}
     * (the default), the type will be logged in all logging modes.
     * </p>
     *
     * @return {@code true} if logging should only occur in verbose mode;
     *         {@code false} if logging should occur in all modes
     */
    boolean verboseOnly() default false;
}
