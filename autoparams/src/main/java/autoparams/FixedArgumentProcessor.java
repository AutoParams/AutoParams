package autoparams;

import autoparams.customization.ArgumentProcessor;
import autoparams.customization.Customizer;
import java.lang.reflect.Parameter;

@Deprecated
final class FixedArgumentProcessor implements ArgumentProcessor {

    @Override
    public Customizer process(Parameter parameter, Object argument) {
        return new FixCustomization(parameter.getType(), argument);
    }

}
