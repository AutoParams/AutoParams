package autoparams.generator;

import java.util.function.Consumer;

import autoparams.customization.Customizer;

public class Designer<T> extends DesignContext<T, Designer<T>> {

    private Factory<T> factory;

    Designer(Factory<T> factory) {
        this.factory = factory;
    }

    @Override
    Designer<T> context() {
        return this;
    }

    public T create() {
        T object = generate();        
        process(object);        
        return object;
    }

    private T generate() {
        return factory.get(this.generators.toArray(Customizer[]::new));
    }

    private void process(T object) {
        for (Consumer<T> processor : processors) {
            processor.accept(object);
        }
    }
}
