package autoparams.customization;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static java.util.Arrays.asList;

final class FreezingRecycler implements
    ArgumentRecycler,
    AnnotationConsumer<Freeze> {

    private static final Predicate<Type> NEGATIVE = type -> false;

    private boolean byExactType = true;
    private boolean byImplementedInterfaces = false;

    @Override
    public Customizer recycle(Object argument, ParameterContext context) {
        Parameter parameter = context.getParameter();
        return getGenerator(parameter, argument);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private ObjectGenerator getGenerator(Parameter parameter, Object argument) {
        final boolean byExactType = this.byExactType;
        final boolean byImplementedInterfaces = this.byImplementedInterfaces;

        Class<?> parameterType = parameter.getType();
        List<Class<?>> interfaces = asList(parameterType.getInterfaces());

        Predicate<Type> isTypeFrozen = NEGATIVE
            .or(type -> byExactType && type.equals(parameterType))
            .or(type -> byImplementedInterfaces && interfaces.contains(type));

        return (query, context) -> isTypeFrozen.test(query.getType())
            ? new ObjectContainer(argument)
            : ObjectContainer.EMPTY;
    }

    @Override
    public void accept(Freeze annotation) {
        byExactType = annotation.byExactType();
        byImplementedInterfaces = annotation.byImplementedInterfaces();
    }
}
