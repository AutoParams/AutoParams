package autoparams.customization;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;

import static autoparams.internal.Folder.foldl;
import static java.util.Arrays.stream;

/**
 * A composite implementation of {@link Customizer} that combines multiple
 * customizers into a single unit.
 * <p>
 * This class allows you to aggregate several customizers and apply them as one,
 * creating a chain of customizations. When applied, each customizer in the chain
 * is invoked in the order they were provided to the constructor.
 * </p>
 * <p>
 * This is particularly useful when you have multiple customization needs for
 * your test data generation and want to encapsulate them in a single reusable
 * configuration.
 * </p>
 *
 * @see Customizer
 */
public class CompositeCustomizer implements Customizer {

    private final Customizer[] customizers;

    /**
     * Constructs a {@link CompositeCustomizer} with the specified customizers.
     * <p>
     * The customizers are applied in the order they are provided to this
     * constructor.
     * </p>
     *
     * @param customizers the array of customizers to be composed
     */
    public CompositeCustomizer(Customizer... customizers) {
        this.customizers = customizers;
    }

    /**
     * Customizes the given {@link ObjectGenerator} by applying each of the
     * composed customizers in sequence.
     * <p>
     * This method applies each customizer in the order they were provided to
     * the constructor, passing the result of each customization to the next
     * customizer in the chain.
     * </p>
     *
     * @param generator the object generator to customize
     * @return the customized object generator
     */
    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return foldl((g, c) -> c.customize(g), generator, stream(customizers));
    }

    /**
     * Customizes the given {@link ObjectProcessor} by applying each of the
     * composed customizers in sequence.
     * <p>
     * This method applies each customizer in the order they were provided to
     * the constructor, passing the result of each customization to the next
     * customizer in the chain.
     * </p>
     *
     * @param processor the object processor to customize
     * @return the customized object processor
     */
    @Override
    public ObjectProcessor customize(ObjectProcessor processor) {
        return foldl((p, c) -> c.customize(p), processor, stream(customizers));
    }
}
