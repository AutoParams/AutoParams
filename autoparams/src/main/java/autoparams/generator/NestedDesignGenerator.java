package autoparams.generator;

import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.customization.dsl.ArgumentCustomizationDsl;
import autoparams.customization.dsl.FunctionDelegate;

final class NestedDesignGenerator<T, P> implements ArgumentGenerator {

    private final Predicate<ParameterQuery> predicate;
    private final DesignLanguage<P> designLanguage;

    NestedDesignGenerator(FunctionDelegate<T, P> getter, DesignLanguage<P> designLanguage) {
        this.predicate = createParameterPredicate(getter);
        this.designLanguage = designLanguage;
    }

    private Predicate<ParameterQuery> createParameterPredicate(FunctionDelegate<T, P> getter) {
        // Use ArgumentCustomizationDsl logic directly to avoid duplication
        try {
            ArgumentGenerator tempGenerator =
                (ArgumentGenerator) ArgumentCustomizationDsl.set(getter).to(null);
            return query -> {
                ObjectContainer result = tempGenerator.generate(query, null);
                return result != ObjectContainer.EMPTY;
            };
        } catch (Exception e) {
            return query -> false;
        }
    }

    @Override
    public ObjectContainer generate(ParameterQuery query, ResolutionContext context) {
        if (!predicate.test(query)) {
            return ObjectContainer.EMPTY;
        }

        // Create a factory for the nested object type and apply the design
        try {
            Class<?> nestedType = (Class<?>) query.getType();
            Factory<?> factory = Factory.create(nestedType);
            Object nestedObject = factory.get(designLanguage.getGenerators());
            return new ObjectContainer(nestedObject);
        } catch (Exception e) {
            return ObjectContainer.EMPTY;
        }
    }
}
