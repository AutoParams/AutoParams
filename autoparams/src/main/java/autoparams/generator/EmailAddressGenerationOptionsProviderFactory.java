package autoparams.generator;

import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class EmailAddressGenerationOptionsProviderFactory implements
    CustomizerFactory,
    AnnotationConsumer<UseEmailAddressGenerationOptions> {

    private String[] domains = null;

    @Override
    public Customizer createCustomizer() {
        return new EmailAddressGenerationOptionsProvider(
            new EmailAddressGenerationOptions(this.domains)
        );
    }

    @Override
    public void accept(UseEmailAddressGenerationOptions annotation) {
        domains = annotation.domains().clone();
    }
}
