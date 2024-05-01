package autoparams.generator;

import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class URIGenerationOptionsProviderFactory implements
    CustomizerFactory,
    AnnotationConsumer<UseURIGenerationOptions> {

    private String[] schemes = null;
    private String[] hosts = null;
    private int[] ports = null;

    @Override
    public Customizer createCustomizer() {
        return new URIGenerationOptionsProvider(
            new URIGenerationOptions(schemes, hosts, ports)
        );
    }

    @Override
    public void accept(UseURIGenerationOptions annotation) {
        schemes = annotation.schemes().clone();
        hosts = annotation.hosts().clone();
        ports = annotation.ports().clone();
    }
}
