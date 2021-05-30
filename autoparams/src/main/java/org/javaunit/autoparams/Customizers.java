package org.javaunit.autoparams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.javaunit.autoparams.customization.Customizer;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

final class Customizers {

    private static final Namespace NAMESPACE = Namespace.create(new Object());

    static void addCustomizer(ExtensionContext context, Customizer customizer) {
        getStore(context).add(customizer);
    }

    static Stream<Customizer> getCustomizers(ExtensionContext context) {
        return getStore(context).stream();
    }

    @SuppressWarnings("unchecked")
    private static List<Customizer> getStore(ExtensionContext context) {
        return (List<Customizer>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
            Customizers.class,
            k -> new ArrayList<Customizer>(),
            List.class);
    }

}
