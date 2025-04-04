package autoparams;

import java.lang.reflect.Type;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.UnwrapFailedException;
import autoparams.processor.ObjectProcessor;
import autoparams.type.TypeReference;

public class ResolutionContext {

    private ObjectGenerator generator;
    private ObjectProcessor processor;

    public ResolutionContext(
        ObjectGenerator generator,
        ObjectProcessor processor
    ) {
        this.generator = generator;
        this.processor = processor;
    }

    public ResolutionContext() {
        this(ObjectGenerator.DEFAULT, ObjectProcessor.DEFAULT);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <T> T resolve(T... typeHint) {
        if (typeHint == null) {
            throw new IllegalArgumentException("The argument 'typeHint' is null.");
        } else if (typeHint.length > 0) {
            String message = "The argument 'typeHint' must be empty."
                + " It is used only to determine"
                + " the type of the object to be created.";
            throw new IllegalArgumentException(message);
        }

        Class<?> type = typeHint.getClass().getComponentType();
        boolean isGeneric = type.getTypeParameters().length > 0;
        if (isGeneric) {
            String message = "To resolve an object of a generic class,"
                + " use the method 'resolve(TypeReference<T>)' instead.";
            throw new IllegalArgumentException(message);
        }

        return (T) resolve(type);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        return (T) resolve(new DefaultObjectQuery(type));
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(TypeReference<T> typeReference) {
        return (T) resolve(new DefaultObjectQuery(typeReference.getType()));
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

    public void customize(Customizer... customizers) {
        for (Customizer customizer : customizers) {
            applyCustomizer(customizer);
        }
    }

    public ResolutionContext branch(Customizer... customizers) {
        ResolutionContext context = new ResolutionContext(generator, processor);
        context.customize(customizers);
        return context;
    }
}
