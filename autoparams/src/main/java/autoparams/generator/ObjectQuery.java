package autoparams.generator;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

@FunctionalInterface
public interface ObjectQuery {

    Type getType();

    static ObjectQuery fromParameter(Parameter parameter) {
        return new ParameterQuery(parameter);
    }

    static ObjectQuery fromType(Type type) {
        return new TypeQuery(type);
    }
}
