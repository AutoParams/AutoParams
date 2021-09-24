package org.javaunit.autoparams;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.javaunit.autoparams.generator.CompositeObjectGenerator;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class AutoArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<AutoSource> {

    private final ObjectGenerator generator;
    private int repeat;

    private AutoArgumentsProvider(ObjectGenerator generator) {
        this.generator = generator;
        repeat = 1;
    }

    public AutoArgumentsProvider() {
        this(new CompositeObjectGenerator(ObjectGenerator.DEFAULT, new BuilderGenerator()));
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        final ArgumentsGenerator generator = createArgumentsGenerator(context);
        return generator.generateArguments(context);
    }

    private ArgumentsGenerator createArgumentsGenerator(ExtensionContext context) {
        final int repeat = getRepeat(context);
        return new ArgumentsGenerator(new ObjectGenerationContext(context, generator), repeat);
    }

    private int getRepeat(ExtensionContext context) {
        final Method method = context.getRequiredTestMethod();
        return findAnnotation(method, Repeat.class).map(Repeat::value).orElse(repeat);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void accept(AutoSource annotation) {
        repeat = annotation.repeat();
    }

}
