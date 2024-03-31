package autoparams.customization;

import java.util.Arrays;

import autoparams.processor.InstanceFieldWriter;
import autoparams.processor.ObjectProcessor;

public class InstanceFieldWriterFactory implements
    AnnotationVisitor<WriteInstanceFields>,
    CustomizerFactory {

    private Class<?>[] target;

    @Override
    public void visit(WriteInstanceFields annotation) {
        this.target = annotation.value();
    }

    @Override
    public Customizer createCustomizer() {
        return new CompositeCustomizer(
            Arrays.stream(target)
                .map(InstanceFieldWriter::new)
                .map(ObjectProcessor::toCustomizer)
                .toArray(Customizer[]::new)
        );
    }
}
