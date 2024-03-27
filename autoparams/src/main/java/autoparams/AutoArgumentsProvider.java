package autoparams;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

final class AutoArgumentsProvider implements ArgumentsProvider {

    private final ObjectGenerator generator = ObjectGenerator.DEFAULT;
    private final ObjectProcessor processor = ObjectProcessor.DEFAULT;

    @Override
    public Stream<? extends Arguments> provideArguments(
        ExtensionContext context
    ) {
        final ArgumentsGenerator generator = createArgumentsGenerator(context);
        return generator.generateArguments(context);
    }

    private ArgumentsGenerator createArgumentsGenerator(
        ExtensionContext context
    ) {
        final int repeat = getRepeat(context);
        return new ArgumentsGenerator(
            new ResolutionContext(context, generator, processor),
            repeat
        );
    }

    private int getRepeat(ExtensionContext context) {
        final Method method = context.getRequiredTestMethod();
        return findAnnotation(method, Repeat.class)
            .map(Repeat::value)
            .orElse(1);
    }
}
