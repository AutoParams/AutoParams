package autoparams.generator;

final class ObjectGenerationContextGenerator extends TypeMatchingGenerator {

    public ObjectGenerationContextGenerator() {
        super((query, context) -> context, ObjectGenerationContext.class);
    }
}
