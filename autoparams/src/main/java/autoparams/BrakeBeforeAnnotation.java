package autoparams;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@BrakeWith(AnnotationBrake.class)
public @interface BrakeBeforeAnnotation {

    Class<? extends Annotation>[] value();
}
