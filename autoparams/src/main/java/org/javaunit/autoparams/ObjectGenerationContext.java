package org.javaunit.autoparams;

import javax.annotation.Nullable;

final class ObjectGenerationContext {

    public interface FixHook {
        void fix(Class<?> type, Object argument);
    }

    private ObjectGenerator generator;
    private FixHook fixHook;

    public ObjectGenerationContext(ObjectGenerator generator, FixHook fixHook) {
        this.generator = generator;
        this.fixHook = fixHook;
    }

    @Nullable
    public Object generate(ObjectQuery query) {
        GenerationResult result = generator.generate(query, this);
        if (result.isAbsent()) {
            String format = "An object cannot be generated with the given query '%s'. "
                + "This can happen if the query represents an interface or abstract class.";

            throw new ObjectGenerationException(String.format(format, query.getType()));
        }

        return result.get();
    }

    void fix(Class<?> type, Object argument) {
        generator = new CompositeObjectGenerator(
            new TypeMatchingGenerator(() -> argument, type),
            generator);
        fixHook.fix(type, argument);
    }

}
