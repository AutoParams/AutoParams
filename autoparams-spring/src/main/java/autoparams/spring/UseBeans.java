package autoparams.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import autoparams.customization.CustomizerSource;

@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(SpringCustomizerFactory.class)
public @interface UseBeans {
}
