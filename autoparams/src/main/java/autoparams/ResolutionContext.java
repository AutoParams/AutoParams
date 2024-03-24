package autoparams;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectQuery;
import autoparams.generator.UnwrapFailedException;
import autoparams.processor.ObjectProcessor;
import java.lang.reflect.Type;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class ResolutionContext {

    private final ExtensionContext extensionContext;
    private ObjectGenerator generator;
    private ObjectProcessor processor;

    public ResolutionContext(
        ExtensionContext extensionContext,
        ObjectGenerator generator,
        ObjectProcessor processor
    ) {
        this.extensionContext = extensionContext;
        this.generator = generator;
        this.processor = processor;
    }

    public ExtensionContext getExtensionContext() {
        return extensionContext;
    }

    @SuppressWarnings("unchecked")
    public <T> T generate(Class<T> type) {
        return (T) generate(ObjectQuery.fromType(type));
    }

    public Object generate(ObjectQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("The argument 'query' is null.");
        }

        Object value = generateValue(query);
        processor.process(query, value, this);
        return value;
    }

    private Object generateValue(ObjectQuery query) {
        final Type type = query.getType();
        if (ExtensionContext.class.equals(type)) {
            return extensionContext;
        } else if (ObjectGenerator.class.equals(type)) {
            return generator;
        } else if (ObjectProcessor.class.equals(type)) {
            return processor;
        } else {
            try {
                return generator.generate(query, this).unwrapOrElseThrow();
            } catch (UnwrapFailedException exception) {
                throw composeGenerationFailedException(query, exception);
            }
        }
    }

    private RuntimeException composeGenerationFailedException(ObjectQuery query, Throwable cause) {
        String messageFormat = "Object cannot be created with the given query '%s'."
            + " This can happen if the query represents an interface or abstract class.";
        String message = String.format(messageFormat, query);
        return new RuntimeException(message, cause);
    }

    // TODO: Rename to reflect the purpose of the method
    public void customizeGenerator(Customizer customizer) {
        if (customizer == null) {
            throw new IllegalArgumentException("The argument 'customizer' is null.");
        }

        generator = customizer.customize(generator);
        processor = customizer.customize(processor);
    }
}
