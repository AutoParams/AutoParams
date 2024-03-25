package autoparams.customization;

@FunctionalInterface
public interface CustomizerFactory {

    Customizer createCustomizer();
}
