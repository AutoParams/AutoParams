package autoparams.customization;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static java.util.Arrays.asList;

final class FreezingRecycler implements
    ArgumentRecycler,
    AnnotationConsumer<Freeze> {

    private static final Predicate<Type> NEGATIVE = type -> false;

    private boolean byExactType = true;
    private boolean byImplementedInterfaces = false;

    @Override
    public Customizer recycle(Object argument, Parameter parameter) {
        return getGenerator(parameter, argument);
    }

    private ObjectGenerator getGenerator(Parameter parameter, Object argument) {
        Predicate<Type> predicate = NEGATIVE
            .or(type -> matchExactType(parameter, type))
            .or(type -> matchInterfaces(parameter, type));

        return (query, context) -> predicate.test(query.getType())
            ? new ObjectContainer(argument)
            : ObjectContainer.EMPTY;
    }

    private boolean matchExactType(Parameter parameter, Type requestedType) {
        if (byExactType == false) {
            return false;
        }

        Type parameterType = parameter.getParameterizedType();
        return requestedType.equals(parameterType);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private boolean matchInterfaces(Parameter parameter, Type type) {
        if (byImplementedInterfaces == false) {
            return false;
        }

        return asList(parameter.getType().getInterfaces()).contains(type);
    }

    @Override
    public void accept(Freeze annotation) {
        byExactType = annotation.byExactType();
        byImplementedInterfaces = annotation.byImplementedInterfaces();
    }
}
