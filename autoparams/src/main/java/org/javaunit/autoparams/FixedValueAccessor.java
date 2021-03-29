package org.javaunit.autoparams;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;

final class FixedValueAccessor {

    private static final Namespace NAMESPACE = Namespace.create(FixedValueAccessor.class);

    @SuppressWarnings("unchecked")
    public static void set(ExtensionContext context, Class<?> type, Object value) {
        Store store = context.getStore(NAMESPACE);
        HashMap<Class<?>, Object> values = store.getOrComputeIfAbsent(
            Fixed.class,
            k -> new HashMap<Class<?>, Object>(),
            HashMap.class);
        values.putIfAbsent(type, DefaultArgumentConverter.INSTANCE.convert(value, type));
    }

    @SuppressWarnings("unchecked")
    public static Iterable<Map.Entry<Class<?>, Object>> entries(ExtensionContext context) {
        Store store = context.getStore(NAMESPACE);
        HashMap<Class<?>, Object> values = store.getOrDefault(Fixed.class, HashMap.class, null);
        return values == null ? Collections.emptyList() : values.entrySet();
    }

}
