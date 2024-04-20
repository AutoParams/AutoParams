package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

import org.junit.jupiter.api.extension.ParameterContext;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@BrakeWith(PredicateBrake.class)
public @interface BrakeBefore {

    Class<? extends Predicate<ParameterContext>> value();
}
