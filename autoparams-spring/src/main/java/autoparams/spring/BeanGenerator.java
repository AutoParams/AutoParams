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

public final class BeanGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return generate(context, query.getType());
    }

    private static ObjectContainer generate(
        ResolutionContext context,
        Type type
    ) {
        if (type.equals(ExtensionContext.class)) {
            return ObjectContainer.EMPTY;
        }

        BeanFactory factory = getBeanFactory(context);
        Object bean = getBeanIfAvailable(factory, type);
        return bean == null ? ObjectContainer.EMPTY : new ObjectContainer(bean);
    }

    private static BeanFactory getBeanFactory(ResolutionContext context) {
        return getApplicationContext(context.resolve(ExtensionContext.class));
    }

    private static Object getBeanIfAvailable(BeanFactory factory, Type type) {
        ResolvableType resolvableType = ResolvableType.forType(type);
        return factory.getBeanProvider(resolvableType).getIfAvailable();
    }
}
