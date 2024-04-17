package autoparams.customization;

import java.util.Arrays;

import autoparams.processor.InstanceFieldWriter;
import org.junit.jupiter.params.support.AnnotationConsumer;

@SuppressWarnings("deprecation")
public class InstanceFieldWriterFactory implements
    AnnotationConsumer<WriteInstanceFields>,
    AnnotationVisitor<WriteInstanceFields>,
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
        this.target = annotation.value();
    }
}
