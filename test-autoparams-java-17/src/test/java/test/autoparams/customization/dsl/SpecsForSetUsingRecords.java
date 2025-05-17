package test.autoparams.customization.dsl;

import autoparams.AutoParams;
import autoparams.ResolutionContext;
import org.junit.jupiter.api.Test;
import test.autoparams.Product;

import static autoparams.customization.dsl.ArgumentCustomizationDsl.set;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForSetUsingRecords {

    @Test
    @AutoParams
    void set_correctly_sets_argument_of_record(
        ResolutionContext context,
        String name
    ) {
        context.customize(set(Product::name).to(name));
        Product product = context.resolve();
        assertThat(product.name()).isEqualTo(name);
    }

    public record IssueContainer(String issue) {
    }

    @Test
    @AutoParams
    void set_correctly_sets_argument_of_record_with_name_starting_with_is(
        ResolutionContext context,
        String issue
    ) {
        context.customize(set(IssueContainer::issue).to(issue));
        IssueContainer container = context.resolve();
        assertThat(container.issue()).isEqualTo(issue);
    }

    public record IsContainer(String is) {
    }

    @Test
    @AutoParams
    void set_correctly_sets_argument_of_record_with_name_is(
        ResolutionContext context,
        String is
    ) {
        context.customize(set(IsContainer::is).to(is));
        IsContainer container = context.resolve();
        assertThat(container.is()).isEqualTo(is);
    }

    public record GetupContainer(String getup) {
    }

    @Test
    @AutoParams
    void set_correctly_sets_argument_of_record_with_name_starting_with_get(
        ResolutionContext context,
        String getup
    ) {
        context.customize(set(GetupContainer::getup).to(getup));
        GetupContainer container = context.resolve();
        assertThat(container.getup()).isEqualTo(getup);
    }

    public record GetContainer(String get) {
    }

    @Test
    @AutoParams
    void set_correctly_sets_argument_of_record_with_name_get(
        ResolutionContext context,
        String get
    ) {
        context.customize(set(GetContainer::get).to(get));
        GetContainer container = context.resolve();
        assertThat(container.get()).isEqualTo(get);
    }
}
