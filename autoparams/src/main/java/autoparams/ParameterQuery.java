package autoparams;

import java.beans.ConstructorProperties;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Optional;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.lang.System.lineSeparator;

/**
 * Represents a query for a constructor or method parameter.
 * <p>
 * This class provides access to the parameter, its index, and its type. It also
 * offers methods to retrieve the parameter name, handling cases where the name
 * is not available at runtime.
 * </p>
 *
 * @see ObjectQuery
 */
public final class ParameterQuery implements ObjectQuery {

    private final Parameter parameter;
    private final int index;
    private final Type type;

    /**
     * Creates a new {@link ParameterQuery} for the given parameter, index, and
     * type.
     *
     * @param parameter the {@link java.lang.reflect.Parameter} to query
     * @param index     the index of the parameter in the declaring executable
     * @param type      the type of the parameter
     */
    public ParameterQuery(Parameter parameter, int index, Type type) {
        this.parameter = parameter;
        this.index = index;
        this.type = type;
    }

    /**
     * Returns the underlying {@link java.lang.reflect.Parameter}.
     *
     * @return the parameter
     */
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * Returns the index of the parameter in the declaring executable.
     *
     * @return the parameter index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the type of the parameter.
     *
     * @return the parameter type
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * Returns an {@link Optional} containing the parameter name if available.
     * <p>
     * If the parameter name is not present at runtime, this method attempts to
     * resolve it using the {@link ConstructorProperties} annotation if
     * available.
     * </p>
     *
     * @return an {@link Optional} with the parameter name, or empty if not
     *         available
     * @see ConstructorProperties
     * @see #getRequiredParameterName()
     */
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

    /**
     * Returns the parameter name, or throws an exception if it cannot be
     * determined.
     * <p>
     * If the parameter name is not available at runtime, this method throws a
     * {@link RuntimeException} with a detailed message describing how to enable
     * parameter name retention.
     * </p>
     *
     * @return the parameter name
     * @throws RuntimeException if the parameter name cannot be determined
     * @see #getParameterName()
     */
    public String getRequiredParameterName() {
        return getParameterName().orElseThrow(() -> {
            String message = String.format(
                "Unable to determine the parameter name at index %d of executable %s."
                    + lineSeparator()
                    + lineSeparator()
                    + "Some features require parameter names to be available at runtime. "
                    + "However, Java does not include parameter names in bytecode by default. "
                    + "To ensure this works correctly, you can:"
                    + lineSeparator()
                    + lineSeparator()
                    + "1. Use a record class, which preserves parameter names by design."
                    + lineSeparator()
                    + "2. Compile the code with the -parameters option using javac, "
                    + "or the -java-parameters option when using kotlinc."
                    + lineSeparator()
                    + "   If you're using Spring Boot, this option is automatically enabled when "
                    + "using the Spring Boot Gradle plugin"
                    + "(https://docs.spring.io/spring-boot/gradle-plugin/"
                    + "reacting.html#reacting-to-other-plugins.java) "
                    + "or the Spring Boot Maven plugin"
                    + "(https://docs.spring.io/spring-boot/maven-plugin/using.html)."
                    + lineSeparator()
                    + "3. Apply the @ConstructorProperties annotation to constructors to "
                    + "explicitly declare parameter names. This annotation does not work for "
                    + "regular methods."
                    + lineSeparator()
                    + "   If you're using Lombok and the constructor is generated by a Lombok "
                    + "annotation such as @AllArgsConstructor, the annotation can be added "
                    + "automatically by enabling "
                    + "the 'lombok.anyConstructor.addConstructorProperties = true' option."
                    + lineSeparator()
                    + "   For more details, see: https://projectlombok.org/features/constructor",
                index,
                parameter.getDeclaringExecutable()
            );
            return new RuntimeException(message);
        });
    }

    /**
     * Returns a string representation of this parameter query.
     *
     * @return a string representation of the parameter query
     */
    @Override
    public String toString() {
        return "Parameter " + parameter;
    }

    @Override
    public String toLog(boolean verbose) {
        String typeName = TypeFormatter.format(getType(), verbose);
        String propertyName = getPropertyNameForLogging();
        return typeName + " " + propertyName;
    }

    /**
     * Returns the property name for logging purposes.
     * <p>
     * If the parameter name is available and not a synthetic name (like "arg0"),
     * it returns the parameter name. Otherwise, if the parameter belongs to a
     * setter method, it infers the property name from the method name.
     * </p>
     *
     * @return the property name for logging
     */
    private String getPropertyNameForLogging() {
        String parameterName = parameter.getName();

        // If parameter name is present and not synthetic, use it
        if (parameter.isNamePresent() && !isSyntheticParameterName(parameterName)) {
            return parameterName;
        }

        // If this is a setter method, infer property name from method name
        if (isSetterMethod()) {
            return inferPropertyNameFromSetter(parameter.getDeclaringExecutable().getName());
        }

        // Fallback to parameter name (may be synthetic like "arg0")
        return parameterName;
    }

    /**
     * Checks if the parameter belongs to a setter method.
     *
     * @return true if the parameter belongs to a setter method
     */
    private boolean isSetterMethod() {
        if (!(parameter.getDeclaringExecutable() instanceof Method)) {
            return false;
        }

        Method method = (Method) parameter.getDeclaringExecutable();
        String methodName = method.getName();

        return methodName.startsWith("set") &&
            methodName.length() > 3 &&
            isUpperCase(methodName.charAt(3)) &&
            method.getParameterCount() == 1;
    }

    /**
     * Infers the property name from a setter method name.
     * <p>
     * For example, "setName" -> "name", "setUserName" -> "userName"
     * </p>
     *
     * @param setterName the setter method name (e.g., "setName")
     * @return the inferred property name (e.g., "name")
     */
    private static String inferPropertyNameFromSetter(String setterName) {
        String withoutPrefix = setterName.substring(3); // Remove "set" prefix
        return decapitalizeHead(withoutPrefix);
    }

    /**
     * Checks if the parameter name is a synthetic name (like "arg0", "arg1").
     *
     * @param parameterName the parameter name to check
     * @return true if the parameter name is synthetic
     */
    private static boolean isSyntheticParameterName(String parameterName) {
        return parameterName != null &&
            parameterName.startsWith("arg") &&
            parameterName.length() > 3 &&
            isNumeric(parameterName.substring(3));
    }

    /**
     * Checks if a string contains only numeric characters.
     *
     * @param s the string to check
     * @return true if the string contains only numeric characters
     */
    private static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts the first character of a string to lowercase.
     * <p>
     * For example, "Name" -> "name", "UserName" -> "userName"
     * </p>
     *
     * @param s the string to decapitalize
     * @return the string with the first character in lowercase
     */
    private static String decapitalizeHead(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        char head = s.charAt(0);
        return isUpperCase(head) ? toLowerCase(head) + s.substring(1) : s;
    }
}
