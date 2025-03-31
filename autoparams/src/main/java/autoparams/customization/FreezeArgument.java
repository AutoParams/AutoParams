package autoparams.customization;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
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

    static FreezeArgument withName(String name) {
        return new FreezeArgument(new NamePredicate(name));
    }

    static FreezeArgument withParameterType(Class<?> parameterType) {
        return new FreezeArgument(new ParameterTypePredicate(parameterType));
    }

    static FreezeArgument withParameterTypeAndName(
        Class<?> parameterType,
        String name
    ) {
        return new FreezeArgument(
            new ParameterTypePredicate(parameterType)
                .and(new NamePredicate(name))
        );
    }

    public FreezeArgument in(Class<?> declaringType) {
        return narrowScope(new DeclaringTypePredicate(declaringType));
    }

    private FreezeArgument narrowScope(Predicate<ParameterQuery> predicate) {
        return new FreezeArgument(this.predicate.and(predicate));
    }

    public Customizer to(Object value) {
        return new ArgumentGenerator(predicate, value);
    }

    private static class NamePredicate implements Predicate<ParameterQuery> {

        private final String name;

        public NamePredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean test(ParameterQuery query) {
            return query.getParameterName().filter(name::equals).isPresent();
        }
    }

    private static class DeclaringTypePredicate
        implements Predicate<ParameterQuery> {

        private final Class<?> declaringType;

        public DeclaringTypePredicate(Class<?> declaringType) {
            this.declaringType = declaringType;
        }

        @Override
        public boolean test(ParameterQuery query) {
            Parameter parameter = query.getParameter();
            Executable executable = parameter.getDeclaringExecutable();
            return executable.getDeclaringClass().equals(declaringType);
        }
    }

    private static class ParameterTypePredicate
        implements Predicate<ParameterQuery> {

        private final Class<?> parameterType;

        public ParameterTypePredicate(Class<?> parameterType) {
            this.parameterType = parameterType;
        }

        @Override
        public boolean test(ParameterQuery query) {
            return query.getParameter().getType().equals(parameterType);
        }
    }

    private static class ArgumentGenerator implements ObjectGenerator {

        private final Predicate<ParameterQuery> predicate;
        private final Object value;

        public ArgumentGenerator(
            Predicate<ParameterQuery> predicate,
            Object value
        ) {
            this.predicate = predicate;
            this.value = value;
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
                ? new ObjectContainer(value)
                : ObjectContainer.EMPTY;
        }
    }
}
