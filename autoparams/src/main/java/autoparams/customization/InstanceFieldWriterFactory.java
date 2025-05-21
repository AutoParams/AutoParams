package autoparams.customization;

import autoparams.processor.InstanceFieldWriter;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static java.util.Arrays.stream;

/**
 * Factory for creating customizers that write to instance fields.
 * <p>
 * This class implements {@link AnnotationConsumer} for the
 * {@link WriteInstanceFields} annotation and {@link CustomizerFactory}.
 * It creates a {@link CompositeCustomizer} that contains
 * {@link InstanceFieldWriter} instances for the target classes specified
 * in the annotation.
 * </p>
 *
 * @see WriteInstanceFields
 * @see CustomizerFactory
 * @see AnnotationConsumer
 * @see InstanceFieldWriter
 */
public class InstanceFieldWriterFactory implements
    AnnotationConsumer<WriteInstanceFields>,
    CustomizerFactory {

    private Class<?>[] target;

    /**
     * Creates an instance of {@link InstanceFieldWriterFactory}.
     */
    public InstanceFieldWriterFactory() {
    }

    /**
     * Creates a {@link Customizer} that enables writing to instance fields.
     * <p>
     * This implementation of {@link CustomizerFactory#createCustomizer()}
     * constructs a {@link CompositeCustomizer}. This customizer is populated
     * with {@link InstanceFieldWriter} instances, one for each target class
     * that was specified via the {@link WriteInstanceFields} annotation and
     * processed by the {@link #accept(WriteInstanceFields)} method.
     * </p>
     * <p>
     * The resulting customizer will attempt to write values to the instance
     * fields of the configured target types during the object processing phase.
     * </p>
     *
     * @return a {@link Customizer} configured to write to instance fields of
     *         the target classes.
     */
    @Override
    public Customizer createCustomizer() {
        return new CompositeCustomizer(
            stream(target)
                .map(InstanceFieldWriter::new)
                .toArray(Customizer[]::new)
        );
    }

    /**
     * Consumes the {@link WriteInstanceFields} annotation to configure the
     * target classes for field writing.
     * <p>
     * This method is called by the AutoParams framework when the
     * {@link WriteInstanceFields} annotation is present. It extracts the
     * array of classes specified in the annotation's {@code value} attribute
     * and stores them internally. These target classes are later used by
     * {@link #createCustomizer()} to create {@link InstanceFieldWriter}
     * instances.
     * </p>
     *
     * @param annotation the {@link WriteInstanceFields} annotation instance
     *                   containing the target classes.
     */
    @Override
    public void accept(WriteInstanceFields annotation) {
        target = annotation.value();
    }
}
