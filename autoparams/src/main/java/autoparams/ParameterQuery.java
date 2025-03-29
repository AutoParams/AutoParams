package autoparams;

import java.beans.ConstructorProperties;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Optional;

public final class ParameterQuery implements ObjectQuery {

    private final Parameter parameter;
    private final int index;
    private final Type type;

    public ParameterQuery(Parameter parameter, int index, Type type) {
        this.parameter = parameter;
        this.index = index;
        this.type = type;
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
            String message = String.format(
                "Unable to determine the parameter name at index %d of executable %s."
                    + " Parameter names can be determined in the following ways:\n"
                    + "1. Use of the Record class.\n"
                    + "2. Compile the code with the javac command using the -parameters option.\n"
                    + "3. Apply the @ConstructorProperties annotation."
                    + " If using Lombok, the @ConstructorProperties annotation"
                    + " can be automatically generated with"
                    + " the lombok.anyConstructor.addConstructorProperties = true option."
                    + " For more information,"
                    + " refer to https://projectlombok.org/features/constructor.",
                index,
                parameter.getDeclaringExecutable()
            );
            return new RuntimeException(message);
        });
    }

    @Override
    public String toString() {
        return parameter.toString();
    }
}
