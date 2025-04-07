package autoparams.customization;

import java.lang.reflect.AnnotatedElement;
import java.util.function.Consumer;

@FunctionalInterface
public interface AnnotatedElementConsumer extends Consumer<AnnotatedElement> {
}
