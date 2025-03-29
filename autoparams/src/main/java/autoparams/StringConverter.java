package autoparams;

import java.util.Optional;

@FunctionalInterface
public interface StringConverter {

    StringConverter DEFAULT = new DefaultStringConverter();

    Optional<Object> convert(String source, ObjectQuery query);
}
