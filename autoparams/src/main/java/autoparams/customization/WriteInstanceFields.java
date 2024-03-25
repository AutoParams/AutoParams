package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(FieldWriterFactory.class)
public @interface WriteInstanceFields {

    Class<?> value();
}
