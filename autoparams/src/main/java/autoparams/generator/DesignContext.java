package autoparams.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import autoparams.customization.dsl.ArgumentCustomizationDsl;
import autoparams.customization.dsl.FunctionDelegate;
import lombok.AllArgsConstructor;

abstract class DesignContext<T, Context extends DesignContext<T, Context>> {

    final List<ObjectGenerator> generators = new ArrayList<>();
    final List<Consumer<T>> processors = new ArrayList<>();

    abstract Context context();

    public <P> ParameterBinding<P> set(FunctionDelegate<T, P> getterDelegate) {
        if (getterDelegate == null) {
            throw new IllegalArgumentException("The argument 'getterDelegate' is null.");
        }

        return new ParameterBinding<>(getterDelegate);
    }

    public Context process(Consumer<T> processor) {
        if (processor == null) {
            throw new IllegalArgumentException("The argument 'processor' is null.");
        }

        this.processors.add(processor);
        return this.context();
    }

    @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    public class ParameterBinding<P> {

        private final FunctionDelegate<T, P> getter;

        public Context to(P value) {
            generators.add(ArgumentCustomizationDsl.set(getter).to(value));
            return context();
        }

        public Context withDesign(
            Function<DesignLanguage<P>, DesignLanguage<P>> design
        ) {
            if (design == null) {
                throw new IllegalArgumentException("The argument 'design' is null.");
            }

            DesignLanguage<P> designLanguage = new DesignLanguage<>();
            DesignLanguage<P> result = design.apply(designLanguage);

            if (result != designLanguage) {
                throw new IllegalArgumentException("The design function must return its argument.");
            }

            generators.add(new NestedDesignGenerator<>(getter, designLanguage));
            return context();
        }
    }
}
