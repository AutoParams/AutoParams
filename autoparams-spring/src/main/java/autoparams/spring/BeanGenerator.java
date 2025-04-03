package autoparams.spring;

import java.lang.reflect.Type;
import java.util.function.Function;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.ResolvableType;

import static org.springframework.test.context.junit.jupiter.SpringExtension.getApplicationContext;

public final class BeanGenerator implements ObjectGenerator {

    private record ConstantFunction<T, R>(R value) implements Function<T, R> {

        @Override
        public R apply(T t) {
            return value;
        }
    }

    private final Function<ResolutionContext, BeanFactory> beanFactoryResolver;

    private BeanGenerator(
        Function<ResolutionContext, BeanFactory> beanFactoryResolver
    ) {
        this.beanFactoryResolver = beanFactoryResolver;
    }

    public BeanGenerator(BeanFactory beanFactory) {
        this(new ConstantFunction<>(beanFactory));
    }

    public BeanGenerator() {
        this(BeanGenerator::resolveBeanFactory);
    }

    private static BeanFactory resolveBeanFactory(ResolutionContext context) {
        return getApplicationContext(context.resolve(ExtensionContext.class));
    }

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return generate(context, query.getType());
    }

    private ObjectContainer generate(ResolutionContext context, Type type) {
        if (type.equals(ExtensionContext.class)) {
            return ObjectContainer.EMPTY;
        }

        BeanFactory factory = beanFactoryResolver.apply(context);
        Object bean = getBeanIfAvailable(factory, type);
        return bean == null ? ObjectContainer.EMPTY : new ObjectContainer(bean);
    }

    private static Object getBeanIfAvailable(BeanFactory factory, Type type) {
        ResolvableType resolvableType = ResolvableType.forType(type);
        return factory.getBeanProvider(resolvableType).getIfAvailable();
    }
}
