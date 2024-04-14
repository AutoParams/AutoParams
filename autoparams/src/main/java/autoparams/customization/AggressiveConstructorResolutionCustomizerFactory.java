package autoparams.customization;

public class AggressiveConstructorResolutionCustomizerFactory implements
    AnnotationVisitor<ResolveConstructorAggressively>,
    CustomizerFactory {

    private Class<?> target;

    @Override
    public void visit(ResolveConstructorAggressively annotation) {
        this.target = annotation.value();
    }

    @Override
    public Customizer createCustomizer() {
        return new AggressiveConstructorResolutionCustomizer(target);
    }
}
