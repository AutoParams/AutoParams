package autoparams.lombok;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectGenerator;

public class BuilderCustomizer implements Customizer {

    private final BuilderInvoker invoker;

    public BuilderCustomizer() {
        this("builder", "build");
    }

    public BuilderCustomizer(String builderMethodName, String buildMethodName) {
        this.invoker = new BuilderInvoker(builderMethodName, buildMethodName);
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return invoker.customize(generator);
    }
}
