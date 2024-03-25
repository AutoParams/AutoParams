package autoparams.customization;

@Deprecated
public class FieldWriterFactory implements
    AnnotationVisitor<WriteInstanceFields>,
    CustomizerFactory {

    private Class<?> target;

    @Override
    public void visit(WriteInstanceFields annotation) {
        this.target = annotation.value();
    }

    @Override
    public Customizer createCustomizer() {
        return new FieldWriter(target);
    }
}
