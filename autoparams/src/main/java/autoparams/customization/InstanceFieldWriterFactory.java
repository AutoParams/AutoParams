package autoparams.customization;

import autoparams.processor.InstanceFieldWriter;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static java.util.Arrays.stream;

public class InstanceFieldWriterFactory implements
    AnnotationConsumer<WriteInstanceFields>,
    CustomizerFactory {

    private Class<?>[] target;

    @Override
    public Customizer createCustomizer() {
        return new CompositeCustomizer(
            stream(target)
                .map(InstanceFieldWriter::new)
                .toArray(Customizer[]::new)
        );
    }

    @Override
    public void accept(WriteInstanceFields annotation) {
        target = annotation.value();
    }
}
