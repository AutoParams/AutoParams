package test.autoparams;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import autoparams.AutoParams;
import autoparams.LogResolution;
import autoparams.ResolutionContext;
import autoparams.SupportedParameterPredicate;
import autoparams.type.TypeReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForResolutionLogging {

    public record User(UUID id, String email) {
    }

    public record Address(String street, String city, String zipCode) {
    }

    public record Product(String name, BigDecimal price) {
    }

    public record Order(User customer, Address shippingAddress, List<Product> products) {
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
            outputLines = outputStr.isEmpty()
                ? new String[0]
                : outputStr.split("\\R");
        } finally {
            System.setOut(originalOut);
            System.out.print(outputStr);
        }

        return outputLines;
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_single_line_with_simple_object_query(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(String.class));
        assertThat(output).hasSize(1);
        assertThat(output[0]).startsWith("String");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_arrow_after_query_with_simple_object_query(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(String.class));
        assertThat(output[0]).matches("String → .*");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_resolved_value_after_arrow_with_simple_object_query(
        ResolutionContext context
    ) {
        String[] result = new String[1];
        String[] output = captureOutput(() -> result[0] = context.resolve(String.class));
        assertThat(output[0]).matches("String → " + result[0] + " \\(.*\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_elapsed_time_in_ms_after_resolved_value_with_simple_object_query(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(String.class));
        assertThat(output[0]).matches(".*\\((<\\s*\\d+|\\d+)\\s*ms\\)$");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_does_not_print_any_log_for_ConstructorResolver(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Address.class));
        for (String line : output) {
            assertThat(line).doesNotContain("ConstructorResolver");
        }
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_does_not_print_any_log_for_ConstructorExtractor(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Address.class));
        for (String line : output) {
            assertThat(line).doesNotContain("ConstructorExtractor");
        }
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_one_depth_tree_structure_with_first_child(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Address.class));
        assertThat(output.length).isGreaterThan(1);
        assertThat(output[1]).startsWith(" ├─ ");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_one_depth_tree_structure_with_second_child(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Address.class));
        assertThat(output.length).isGreaterThan(2);
        assertThat(output[2]).startsWith(" ├─ ");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_one_depth_tree_structure_with_last_child(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Address.class));
        assertThat(output.length).isGreaterThan(3);
        assertThat(output[3]).startsWith(" └─ ");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_one_depth_entry_correctly(ResolutionContext context) {
        String[] output = captureOutput(() -> context.resolve(Address.class));
        assertThat(output[1]).matches(" ├─ String street → street.* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_value_of_root_correctly(ResolutionContext context) {
        String[] output = captureOutput(() -> context.resolve(Address.class));
        assertThat(output[0]).matches(
            "Address → Address\\[street=street.*, city=city.*, zipCode=zipCode.*] \\(.*ms\\)"
        );
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_value_of_last_child_correctly(ResolutionContext context) {
        String[] output = captureOutput(() -> context.resolve(Address.class));
        assertThat(output[3]).matches(" └─ String zipCode → zipCode.* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_two_depth_tree_structure_with_first_leaf(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() ->
            context.resolve(new TypeReference<List<Address>>() {})
        );
        assertThat(output[2]).matches(" │ {3}├─ String street → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_two_depth_tree_structure_with_second_leaf(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() ->
            context.resolve(new TypeReference<List<Address>>() {})
        );
        assertThat(output[3]).matches(" │ {3}├─ String city → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_two_depth_tree_structure_with_last_leaf(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() ->
            context.resolve(new TypeReference<List<Address>>() {})
        );
        assertThat(output[4]).matches(" │ {3}└─ String zipCode → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_two_depth_tree_structure_with_first_leaf_of_last_stem(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() ->
            context.resolve(new TypeReference<List<Address>>() {})
        );
        assertThat(output[10]).matches(" {5}├─ String street → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_two_depth_tree_structure_with_second_leaf_of_last_stem(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() ->
            context.resolve(new TypeReference<List<Address>>() {})
        );
        assertThat(output[11]).matches(" {5}├─ String city → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_two_depth_tree_structure_with_last_leaf_of_last_stem(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() ->
            context.resolve(new TypeReference<List<Address>>() {})
        );
        assertThat(output[12]).matches(" {5}└─ String zipCode → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_does_not_print_log_for_EmailAddressGenerationOptions(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(User.class));
        for (String line : output) {
            assertThat(line).doesNotContain("EmailAddressGenerationOptions");
        }
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_does_not_print_log_for_SupportedParameterPredicate(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() ->
            context.resolve(SupportedParameterPredicate.class)
        );
        for (String line : output) {
            assertThat(line).doesNotContain("SupportedParameterPredicate");
        }
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_does_not_print_log_for_ResolutionContext(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() ->
            context.resolve(ResolutionContext.class)
        );
        for (String line : output) {
            assertThat(line).doesNotContain("ResolutionContext");
        }
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_deep_tree_structure_with_first_leaf(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Order.class));
        assertThat(output[10]).matches(" {5}│ {3}├─ String name → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_deep_tree_structure_with_last_leaf(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Order.class));
        assertThat(output[11]).matches(" {5}│ {3}└─ BigDecimal price → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_deep_tree_structure_with_first_leaf_of_last_stem(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Order.class));
        assertThat(output[16]).matches(" {9}├─ String name → .* \\(.*ms\\)");
    }

    @Test
    @AutoParams
    @LogResolution
    void sut_prints_deep_tree_structure_with_last_leaf_of_last_stem(
        ResolutionContext context
    ) {
        String[] output = captureOutput(() -> context.resolve(Order.class));
        assertThat(output[17]).matches(" {9}└─ BigDecimal price → .* \\(.*ms\\)");
    }
}
