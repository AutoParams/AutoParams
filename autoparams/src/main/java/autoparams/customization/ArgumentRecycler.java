package autoparams.customization;

import java.util.function.BiFunction;

import org.junit.jupiter.api.extension.ParameterContext;

@FunctionalInterface
public interface ArgumentRecycler
    extends BiFunction<Object, ParameterContext, Customizer> {

    Customizer recycle(Object argument, ParameterContext context);

    @Override
    default Customizer apply(Object argument, ParameterContext context) {
        return recycle(argument, context);
    }
}
