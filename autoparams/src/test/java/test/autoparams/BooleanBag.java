package test.autoparams;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BooleanBag {

    private final boolean value;

    public boolean getValue() {
        return value;
    }
}
