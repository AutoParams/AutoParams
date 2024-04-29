package autoparams;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

@FunctionalInterface
public interface ObjectQuery {

    Type getType();

    @Deprecated
    static ObjectQuery fromParameter(Parameter parameter) {
        return new ParameterQuery(parameter);
    }

    @Deprecated
    static ObjectQuery fromType(Type type) {
        return new TypeQuery(type);
    }
}
