package org.javaunit.autoparams;

final class StreamGenerator extends CompositeObjectGenerator {

    public StreamGenerator() {
        super(
            new GenericStreamGenerator());
    }

}
