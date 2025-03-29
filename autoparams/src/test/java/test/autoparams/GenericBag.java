package test.autoparams;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GenericBag<T> {

    private final T value;
}
