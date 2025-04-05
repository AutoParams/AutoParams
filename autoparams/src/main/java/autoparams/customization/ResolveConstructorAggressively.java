package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(AggressiveConstructorResolutionCustomizerFactory.class)
public @interface ResolveConstructorAggressively {

    Class<?>[] value();
}
