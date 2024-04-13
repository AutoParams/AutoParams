package autoparams.generator;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public final class ParameterQuery implements ObjectQuery {

    private final Parameter parameter;
    private final int index;
    private final Type type;

    @Deprecated
    public ParameterQuery(Parameter parameter) {
        this.parameter = parameter;
        this.index = inferIndex(parameter);
        this.type = parameter.getAnnotatedType().getType();
    }

    public ParameterQuery(Parameter parameter, int index, Type type) {
        this.parameter = parameter;
        this.index = index;
        this.type = type;
    }

    @Deprecated
    private static int inferIndex(Parameter parameter) {
        Parameter[] parameters = parameter
            .getDeclaringExecutable()
            .getParameters();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].equals(parameter)) {
                return i;
            }
        }

        throw new IllegalArgumentException("Cannot infer index of parameter.");
    }

    public Parameter getParameter() {
        return parameter;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return parameter.toString();
    }
}
