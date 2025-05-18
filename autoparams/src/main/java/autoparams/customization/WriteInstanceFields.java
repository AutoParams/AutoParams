package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import autoparams.processor.InstanceFieldWriter;

/**
 * Indicates that the instance fields of the specified classes should be written
 * with resolved values when AutoParams processes an object.
 * <p>
 * This annotation is intended to be used on test methods to automatically
 * populate instance fields of the given classes when resolving test data
 * objects. You can specify one or more target classes whose instance fields
 * will be populated.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>
 * &#64;Test
 * &#64;AutoParams
 * &#64;WriteInstanceFields(MyClass.class)
 * public void testMethod(MyClass instance) {
 *     // Instance fields of MyClass are automatically populated by AutoParams
 * }
 * </pre>
 *
 * @see CustomizerSource
 * @see InstanceFieldWriterFactory
 * @see InstanceFieldWriter
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(InstanceFieldWriterFactory.class)
public @interface WriteInstanceFields {

    /**
     * Specifies the target class(es) whose instance fields should be populated
     * with generated values.
     * <p>
     * When this annotation is applied, AutoParams will attempt to set the
     * instance fields of each class listed in this array. The values for the
     * fields will be resolved by AutoParams.
     * </p>
     *
     * @return an array of {@link Class} objects representing the target types
     *         for instance field population.
     */
    Class<?>[] value();
}
