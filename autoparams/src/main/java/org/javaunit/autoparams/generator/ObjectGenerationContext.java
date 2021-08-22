package org.javaunit.autoparams.generator;

import org.javaunit.autoparams.customization.Customizer;

public final class ObjectGenerationContext {

    private ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;
    }

    ObjectGenerator getGenerator() {
        return generator;
    }

    public Object generate(ObjectQuery query) {
        try {
            return generator.generate(query, this).unwrapOrElseThrow();
        } catch (UnwrapFailedException exception) {
            throw composeGenerationFailedException(query, exception);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T generate(Class<T> type) {
        return (T) generate(() -> type);
    }

    private RuntimeException composeGenerationFailedException(ObjectQuery query, Throwable cause) {
        String messageFormat = "Object cannot be created with the given query '%s'."
            + " This can happen if the query represents an interface or abstract class.";
        String message = String.format(messageFormat, query);
        return new RuntimeException(message, cause);
    }

    public void customizeGenerator(Customizer customizer) {
        generator = customizer.customize(generator);
    }

}
