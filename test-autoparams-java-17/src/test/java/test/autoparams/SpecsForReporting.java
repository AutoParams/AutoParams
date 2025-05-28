package test.autoparams;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import autoparams.customization.Freeze;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForReporting {

    @Test
    void writes_resolving_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(String.class));
        String actual = output[0];

        assertThat(actual).contains("Resolving for:");
        assertThat(actual).contains("java.lang.String");
    }

    @Test
    @AutoParams
    void writes_resolved_event_correctly(
        @Freeze String value,
        ResolutionContext sourceContext
    ) {
        ResolutionContext context = sourceContext.branch();
        String[] output = captureOutput(() -> context.resolve(String.class));
        String actual = output[1];

        assertThat(actual).matches(".*Resolved\\(<*\\d+ ms\\):.*");
        assertThat(actual).contains("java.lang.String");
        assertThat(actual).contains(value);
    }

    @Test
    void writes_blank_line_between_object_resolution() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> {
            context.resolve(String.class);
            context.resolve(Integer.class);
        });

        assertThat(output[2]).isEmpty();
        assertThat(output[3]).isNotEmpty();
    }

    @Test
    void does_not_write_blank_line_after_last_object_resolution_in_scope() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[4]).isNotEmpty();
    }

    @Test
    void writes_zero_depth_indentation_for_resolving_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[0]).doesNotContain("|-- ");
    }

    @Test
    void writes_one_depth_indentation_for_resolving_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[1]).startsWith("|-- ");
    }

    @Test
    void writes_two_depth_indentation_for_resolving_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[2]).startsWith("|   |-- ");
    }

    @Test
    void writes_three_depth_indentation_for_resolving_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[14]).startsWith("|   |   |-- ");
    }

    @Test
    void writes_zero_depth_indentation_for_resolved_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[27]).doesNotContain("|");
    }

    @Test
    void writes_one_depth_indentation_for_resolved_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[4]).startsWith("|   ");
    }

    @Test
    void writes_two_depth_indentation_for_resolved_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[3]).startsWith("|   |   ");
    }

    @Test
    void writes_three_depth_indentation_for_resolved_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[15]).startsWith("|   |   |   ");
    }

    @Test
    void writes_one_depth_blank_for_resolved_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Product.class));
        assertThat(output[5]).startsWith("|");
    }

    @Test
    void writes_two_depth_blank_for_resolved_event_correctly() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> context.resolve(Review.class));
        assertThat(output[17]).startsWith("|   |");
    }

    public static class Uninstantiable {

        protected Uninstantiable() {
        }
    }

    @Test
    void writes_resolution_log_even_when_object_resolution_fails() {
        var context = new ResolutionContext();
        String[] output = captureOutput(() -> {
            try {
                context.resolve(Uninstantiable.class);
            } catch (Exception ignored) {
            }
        });
        assertThat(output[0]).contains("Resolving for:");
    }

    private static String[] captureOutput(Runnable runnable) {
        PrintStream originalOut = System.out;
        String outputStr = null;
        String[] outputLines;
        try {
            var outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));
            runnable.run();
            outputStr = outputStream.toString();
            outputLines = outputStr.split("\\R");
        } finally {
            System.setOut(originalOut);
            System.out.print(outputStr);
        }

        return outputLines;
    }
}
