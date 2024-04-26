package autoparams.generator;

import java.beans.ConstructorProperties;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Optional;

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

    public Optional<String> getParameterName() {
        if (parameter.isNamePresent()) {
            return Optional.of(parameter.getName());
        } else {
            return Optional
                .ofNullable(parameter
                    .getDeclaringExecutable()
                    .getDeclaredAnnotation(ConstructorProperties.class))
                .map(ConstructorProperties::value)
                .map(names -> names[index]);
        }
    }

    public String getRequiredParameterName() {
        return getParameterName().orElseThrow(() -> {
            String message = "Cannot resolve parameter name.";
            return new RuntimeException(message);
        });
    }

    @Override
    public String toString() {
        return parameter.toString();
    }
}
