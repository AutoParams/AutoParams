package test.autoparams.generator;

import java.lang.reflect.Type;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.generator.EmailAddressGenerationOptions;
import autoparams.generator.EmailAddressGenerationOptionsProvider;
import autoparams.generator.ObjectContainer;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForEmailAddressGenerationOptionsProvider {

    @ParameterizedTest
    @AutoSource
    void sut_returns_options(
        EmailAddressGenerationOptions options,
        ResolutionContext context
    ) {
        EmailAddressGenerationOptionsProvider sut =
            new EmailAddressGenerationOptionsProvider(options);
        Type type = EmailAddressGenerationOptions.class;

        ObjectContainer actual = sut.generate(type, context);

        assertThat(actual.unwrapOrElseThrow()).isSameAs(options);
    }
}
