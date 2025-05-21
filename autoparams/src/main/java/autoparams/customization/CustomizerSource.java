package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that identifies a factory for creating customizers.
 * <p>
 * This meta-annotation is designed to be applied to other annotations
 * to designate them as sources of customizers for AutoParams. It specifies
 * which {@link CustomizerFactory} implementation should be used to create
 * the customizer instances.
 * </p>
 * <p>
 * When AutoParams processes annotations on test methods or parameters,
 * it looks for annotations marked with {@code @CustomizerSource} and uses
 * the specified factory to create customizers that will affect the test
 * data generation process.
 * </p>
 *
 * @see CustomizerFactory
 * @see Customizer
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomizerSource {

    /**
     * Specifies the factory class that creates customizer instances.
     * <p>
     * The factory class must implement the {@link CustomizerFactory} interface
     * and provide a way to instantiate customizers for the annotated element.
     * </p>
     *
     * @return the customizer factory class
     */
    Class<? extends CustomizerFactory> value();
}
