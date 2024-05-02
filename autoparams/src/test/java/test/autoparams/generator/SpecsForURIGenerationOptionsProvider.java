package test.autoparams.generator;

import java.lang.reflect.Type;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.URIGenerationOptions;
import autoparams.generator.URIGenerationOptionsProvider;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForURIGenerationOptionsProvider {

    @ParameterizedTest
    @AutoSource
    void sut_returns_options(
        URIGenerationOptions options,
        ResolutionContext context
    ) {
        URIGenerationOptionsProvider sut =
            new URIGenerationOptionsProvider(options);
        Type type = URIGenerationOptions.class;

        ObjectContainer actual = sut.generate(type, context);

        assertThat(actual.unwrapOrElseThrow()).isSameAs(options);
    }
}
