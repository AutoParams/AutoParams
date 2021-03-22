package org.javaunit.autoparams;

final class ObjectGenerationContext {

    private final ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;

    }

    public Object generate(ObjectQuery query) {
        return generator.generate(query, this).orElseThrow(() -> {
            String format = "Objects cannot be generated based on the given query '%s'. "
                + "This can happen if the query represents an interface or abstract class.";

            return new ObjectGenerationException(String.format(format, query.getType()));
        });
    }
}
