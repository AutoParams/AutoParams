package autoparams.customization;

import java.util.Arrays;

import autoparams.processor.InstanceFieldWriter;

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
                .toArray(Customizer[]::new)
        );
    }
}
