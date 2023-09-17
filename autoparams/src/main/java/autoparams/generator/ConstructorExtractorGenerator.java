package autoparams.generator;

class ConstructorExtractorGenerator extends TypeMatchingGenerator {

    public ConstructorExtractorGenerator() {
        super(
            DefaultConstructorExtractor::new,
            ConstructorExtractor.class);
    }
}
