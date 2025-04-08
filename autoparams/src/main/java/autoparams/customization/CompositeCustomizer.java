package autoparams.customization;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;

import static autoparams.internal.Folder.foldl;
import static java.util.Arrays.stream;

public class CompositeCustomizer implements Customizer {

    private final Customizer[] customizers;

    public CompositeCustomizer(Customizer... customizers) {
        this.customizers = customizers;
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return foldl((g, c) -> c.customize(g), generator, stream(customizers));
    }

    @Override
    public ObjectProcessor customize(ObjectProcessor processor) {
        return foldl((p, c) -> c.customize(p), processor, stream(customizers));
    }
}
