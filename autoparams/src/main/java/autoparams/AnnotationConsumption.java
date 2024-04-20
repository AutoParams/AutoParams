package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.jupiter.params.support.AnnotationConsumer;

final class AnnotationConsumption {

    public static void consumeAnnotationIfMatch(
        Object service,
        Annotation annotation
    ) {
        if (match(service, annotation)) {
            consume(service, annotation);
        }
    }

    private static boolean match(Object service, Annotation annotation) {
        Class<?> annotationType = annotation.annotationType();
        for (Type type : service.getClass().getGenericInterfaces()) {
            if (isAnnotationConsumer(type, annotationType)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isAnnotationConsumer(
        Type type,
        Class<?> annotationType
    ) {
        if (type instanceof ParameterizedType == false) {
            return false;
        }

        return isAnnotationConsumer((ParameterizedType) type, annotationType);
    }

    private static boolean isAnnotationConsumer(
        ParameterizedType type,
        Class<?> annotationType
    ) {
        return type.getRawType().equals(AnnotationConsumer.class)
            && type.getActualTypeArguments()[0].equals(annotationType);
    }

    @SuppressWarnings("unchecked")
    private static void consume(Object service, Annotation annotation) {
        ((AnnotationConsumer<Annotation>) service).accept(annotation);
    }
}
