package autoparams.spring;

import autoparams.customization.CompositeCustomizer;

class SpringCustomizer extends CompositeCustomizer {

    public SpringCustomizer() {
        super(
            new AutowiredParameterExcluder(),
            new BeanGenerator()
        );
    }
}
