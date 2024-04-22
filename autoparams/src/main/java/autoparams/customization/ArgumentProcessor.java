package autoparams.customization;

import java.lang.reflect.Parameter;

@Deprecated
public interface ArgumentProcessor {

    Customizer process(Parameter parameter, Object argument);
}
