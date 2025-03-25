package autoparams.spring;

import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.ResolvableType;

import static org.springframework.test.context.junit.jupiter.SpringExtension.getApplicationContext;

final class BeanGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType().equals(ExtensionContext.class)) {
            return ObjectContainer.EMPTY;
        }

        BeanFactory beanFactory = getBeanFactory(context);
        Object bean = tryResolveBean(beanFactory, query.getType());
        return bean == null ? ObjectContainer.EMPTY : new ObjectContainer(bean);
    }

    private static BeanFactory getBeanFactory(ResolutionContext context) {
        return getApplicationContext(context.resolve(ExtensionContext.class));
    }

    private static Object tryResolveBean(BeanFactory factory, Type type) {
        ResolvableType resolvableType = ResolvableType.forType(type);
        return factory.getBeanProvider(resolvableType).getIfAvailable();
    }
}
