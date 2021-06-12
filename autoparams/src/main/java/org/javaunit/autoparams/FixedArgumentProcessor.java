package org.javaunit.autoparams;

import java.lang.reflect.Parameter;
import org.javaunit.autoparams.customization.ArgumentProcessor;
import org.javaunit.autoparams.customization.Customizer;

@Deprecated
final class FixedArgumentProcessor implements ArgumentProcessor {

    @Override
    public Customizer process(Parameter parameter, Object argument) {
        return new FixCustomization(parameter.getType(), argument);
    }

}
