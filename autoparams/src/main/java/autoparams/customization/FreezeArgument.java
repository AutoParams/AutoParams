package autoparams.customization;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

public final class FreezeArgument {

    private final Predicate<ParameterQuery> predicate;

    private FreezeArgument(Predicate<ParameterQuery> predicate) {
        this.predicate = predicate;
    }

    static FreezeArgument withParameterType(Type parameterType) {
        return new FreezeArgument(new ParameterTypePredicate(parameterType));
    }

    static FreezeArgument withParameterName(String parameterName) {
        return new FreezeArgument(new ParameterNamePredicate(parameterName));
    }

    static FreezeArgument withParameterTypeAndParameterName(
        Type parameterType,
        String parameterName
    ) {
        return new FreezeArgument(
            new ParameterTypePredicate(parameterType)
                .and(new ParameterNamePredicate(parameterName))
        );
    }

    public FreezeArgument in(Class<?> declaringClass) {
        return narrowScope(new DeclaringTypePredicate(declaringClass));
    }

    private FreezeArgument narrowScope(Predicate<ParameterQuery> predicate) {
        return new FreezeArgument(this.predicate.and(predicate));
    }

    public Customizer to(Object value) {
        return new ArgumentGenerator(predicate, value);
    }

    private static class ParameterTypePredicate
        implements Predicate<ParameterQuery> {

        private final Type parameterType;

        public ParameterTypePredicate(Type parameterType) {
            this.parameterType = parameterType;
        }

        @Override
        public boolean test(ParameterQuery query) {
            return new TypeSpokesman(query.getType()).match(parameterType);
        }
    }

    private static class ParameterNamePredicate
        implements Predicate<ParameterQuery> {

        private final String parameterName;

        public ParameterNamePredicate(String parameterName) {
            this.parameterName = parameterName;
        }

        @Override
        public boolean test(ParameterQuery query) {
            return query
                .getParameterName()
                .filter(parameterName::equals)
                .isPresent();
        }
    }

    private static class DeclaringTypePredicate
        implements Predicate<ParameterQuery> {

        private final Class<?> declaringClass;

        public DeclaringTypePredicate(Class<?> declaringClass) {
            this.declaringClass = declaringClass;
        }

        @Override
        public boolean test(ParameterQuery query) {
            Parameter parameter = query.getParameter();
            Executable executable = parameter.getDeclaringExecutable();
            return executable.getDeclaringClass().equals(declaringClass);
        }
    }

    private static class ArgumentGenerator implements ObjectGenerator {

        private final Predicate<ParameterQuery> predicate;
        private final Object argument;

        public ArgumentGenerator(
            Predicate<ParameterQuery> predicate,
            Object argument
        ) {
            this.predicate = predicate;
            this.argument = argument;
        }

        @Override
        public ObjectContainer generate(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return query instanceof ParameterQuery
                ? generate((ParameterQuery) query)
                : ObjectContainer.EMPTY;
        }

        private ObjectContainer generate(ParameterQuery query) {
            return predicate.test(query)
                ? new ObjectContainer(argument)
                : ObjectContainer.EMPTY;
        }
    }
}
