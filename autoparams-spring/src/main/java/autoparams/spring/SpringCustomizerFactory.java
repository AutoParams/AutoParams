package autoparams.spring;

import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;

class SpringCustomizerFactory implements CustomizerFactory {

    @Override
    public Customizer createCustomizer() {
        return new SpringCustomizer();
    }
}
