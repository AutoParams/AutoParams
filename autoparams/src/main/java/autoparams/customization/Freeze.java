package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@RecycleArgument(LimitedFreezingRecycler.class)
public @interface Freeze {

    boolean byExactType() default true;

    boolean byImplementedInterfaces() default false;
}
