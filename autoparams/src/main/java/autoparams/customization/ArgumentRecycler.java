package autoparams.customization;

import java.lang.reflect.Parameter;
import java.util.function.BiFunction;

@FunctionalInterface
public interface ArgumentRecycler
    extends BiFunction<Object, Parameter, Customizer> {

    Customizer recycle(Object argument, Parameter parameter);

    @Override
    default Customizer apply(Object argument, Parameter parameter) {
        return recycle(argument, parameter);
    }
}
