package autoparams.customization;

import java.util.Arrays;

import autoparams.processor.InstanceFieldWriter;
import org.junit.jupiter.params.support.AnnotationConsumer;

public class InstanceFieldWriterFactory implements
    AnnotationConsumer<WriteInstanceFields>,
    CustomizerFactory {

    private Class<?>[] target;

    @Override
    public Customizer createCustomizer() {
        return new CompositeCustomizer(
            Arrays.stream(target)
                .map(InstanceFieldWriter::new)
                .toArray(Customizer[]::new)
        );
    }

    @Override
    public void accept(WriteInstanceFields annotation) {
        target = annotation.value();
    }
}
