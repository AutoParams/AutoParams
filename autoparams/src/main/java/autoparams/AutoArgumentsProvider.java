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
        return new ArgumentsGenerator(
            new ResolutionContext(
                context,
                ObjectGenerator.DEFAULT,
                ObjectProcessor.DEFAULT
            ),
            getRepeat(context)
        );
    }

    private int getRepeat(ExtensionContext context) {
        final Method method = context.getRequiredTestMethod();
        return findAnnotation(method, Repeat.class)
            .map(Repeat::value)
            .orElse(1);
    }
}
