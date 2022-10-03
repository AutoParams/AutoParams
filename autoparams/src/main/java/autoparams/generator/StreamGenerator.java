package autoparams.generator;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

final class StreamGenerator extends CompositeObjectGenerator {

    public StreamGenerator() {
        super(
            new TypeMatchingGenerator(Factories::createIntStream, IntStream.class),
            new TypeMatchingGenerator(Factories::createLongStream, LongStream.class),
            new TypeMatchingGenerator(Factories::createDoubleStream, DoubleStream.class),
            new GenericStreamGenerator()
        );
    }

}
