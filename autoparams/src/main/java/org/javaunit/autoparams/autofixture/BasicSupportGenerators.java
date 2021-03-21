package org.javaunit.autoparams.autofixture;

import org.javaunit.autoparams.autofixture.generators.*;

import java.util.Arrays;
import java.util.List;

public class BasicSupportGenerators {
    public static List<DataGenerator> dataGenerators = Arrays.asList(
        new BooleanDataGenerator(),
        new CharacterDataGenerator(),
        new IntegerDataGenerator(),
        new DoubleDataGenerator(),
        new LongDataGenerator(),
        new FloatDataGenerator(),
        new EnumDataGenerator(),
        new StringDataGenerator(),
        new UuidDataGenerator(),
        new CollectionDataGenerator(),
        new ObjectDataGenerator()
    );
}
