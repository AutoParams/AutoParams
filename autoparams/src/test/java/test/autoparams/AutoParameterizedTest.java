package test.autoparams;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

@ParameterizedTest
@AutoSource
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoParameterizedTest {
}
