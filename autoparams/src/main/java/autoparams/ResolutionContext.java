package autoparams;

import java.lang.reflect.Type;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.UnwrapFailedException;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ResolutionContext {

    private ObjectGenerator generator;
    private ObjectProcessor processor;

    @Deprecated
    public ResolutionContext(
        ExtensionContext extensionContext,
        ObjectGenerator generator,
        ObjectProcessor processor
    ) {
        this(
            generator.customize(new ExtensionContextProvider(extensionContext)),
            processor
        );
    }

    public ResolutionContext(
        ObjectGenerator generator,
        ObjectProcessor processor
    ) {
        this.generator = generator;
        this.processor = processor;
    }

    ResolutionContext() {
        this(ObjectGenerator.DEFAULT, ObjectProcessor.DEFAULT);
    }

    @Deprecated
    public ExtensionContext getExtensionContext() {
        return resolve(ExtensionContext.class);
    }

    @Deprecated
    public <T> T generate(Class<T> type) {
        return resolve(type);
    }

    @Deprecated
    public Object generate(ObjectQuery query) {
        return resolve(query);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        return (T) resolve(new TypeQuery(type));
    }

    public Object resolve(ObjectQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("The argument 'query' is null.");
        }

        Object value = generateValue(query);
        processValue(query, value);
        return value;
    }

    private Object generateValue(ObjectQuery query) {
        final Type type = query.getType();
        if (ResolutionContext.class.equals(type)) {
            return this;
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

    private RuntimeException composeGenerationFailedException(
        ObjectQuery query,
        Throwable cause
    ) {
        String messageFormat = "Object cannot be created with the given query '%s'."
            + " This can happen if the query represents an interface or abstract class.";
        String message = String.format(messageFormat, query);
        return new RuntimeException(message, cause);
    }

    private void processValue(ObjectQuery query, Object value) {
        processor.process(query, value, this);
    }

    public void applyCustomizer(Customizer customizer) {
        if (customizer == null) {
            throw new IllegalArgumentException("The argument 'customizer' is null.");
        }

        generator = customizer.customize(generator);
        processor = customizer.customize(processor);
    }

    public void applyCustomizer(ObjectGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("The argument 'generator' is null.");
        }

        this.generator = generator.customize(this.generator);
    }

    public void applyCustomizer(ObjectProcessor processor) {
        if (processor == null) {
            throw new IllegalArgumentException("The argument 'processor' is null.");
        }

        this.processor = processor.customize(this.processor);
    }

    public ResolutionContext branch() {
        return new ResolutionContext(generator, processor);
    }
}
