package test.autoparams;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.UUID;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.freezeArgument;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForResolutionContext {

    private static String captureOutput(Runnable runnable) {
        PrintStream originalOut = System.out;
        String outputStr = null;
        try {
            var outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));
            runnable.run();
            outputStr = outputStream.toString();
        } finally {
            System.setOut(originalOut);
            System.out.print(outputStr);
        }
        return outputStr;
    }

    @Test
    @AutoParams
    void customize_applies_customizers_correctly(
        ResolutionContext sut,
        UUID id,
        int stockQuantity
    ) {
        sut.customize(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = sut.resolve(Product.class);

        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void branch_applies_customizers_correctly(
        ResolutionContext sut,
        UUID id,
        int stockQuantity
    ) {
        ResolutionContext context = sut.branch(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = context.resolve(Product.class);

        assertThat(product.id()).isEqualTo(id);
        assertThat(product.stockQuantity()).isEqualTo(stockQuantity);
    }

    @Test
    @AutoParams
    void branch_does_not_affect_original_context(
        ResolutionContext sut,
        UUID id,
        int stockQuantity
    ) {
        sut.branch(
            freezeArgument("id").to(id),
            freezeArgument("stockQuantity").to(stockQuantity)
        );

        Product product = sut.resolve(Product.class);

        assertThat(product.id()).isNotEqualTo(id);
        assertThat(product.stockQuantity()).isNotEqualTo(stockQuantity);
    }

    @Test
    void enableLogging_activates_console_logging() {
        ResolutionContext sut = new ResolutionContext();
        sut.enableLogging();

        String output = captureOutput(() -> sut.resolve(String.class));

        assertThat(output).isNotEmpty();
    }

    @Test
    void logging_is_disabled_by_default() {
        ResolutionContext sut = new ResolutionContext();
        String output = captureOutput(() -> sut.resolve(String.class));
        assertThat(output).isEmpty();
    }

    @Test
    void logging_state_persists_across_multiple_resolve_operations() {
        ResolutionContext sut = new ResolutionContext();
        sut.enableLogging();

        String output = captureOutput(() -> {
            sut.resolve(String.class);
            sut.resolve(Integer.class);
        });

        String[] lines = output.split("\\R");
        assertThat(lines.length).isGreaterThan(1);
    }

    @Test
    void enableLogging_can_be_called_multiple_times_safely() {
        ResolutionContext sut = new ResolutionContext();
        sut.enableLogging();
        sut.enableLogging();
        sut.enableLogging();

        String output = captureOutput(() -> sut.resolve(String.class));

        assertThat(output).isNotEmpty();
    }
}
