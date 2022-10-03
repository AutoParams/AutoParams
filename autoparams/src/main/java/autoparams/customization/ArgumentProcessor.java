package autoparams.customization;

import java.lang.reflect.Parameter;

public interface ArgumentProcessor {

    Customizer process(Parameter parameter, Object argument);

}
