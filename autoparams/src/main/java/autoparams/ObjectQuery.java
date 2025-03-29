package autoparams;

import java.lang.reflect.Type;

@FunctionalInterface
public interface ObjectQuery {

    Type getType();
}
