package org.javaunit.autoparams;

@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
final class SimpleValueObjectGenerator extends CompositeObjectGenerator {

    public SimpleValueObjectGenerator() {
        super(
            new UrlGenerator() // TODO: Move to the new simple value generator.
        );
    }

}
