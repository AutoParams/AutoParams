package test.autoparams;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HasGenericSetters<T1, T2> {

    private int value1;
    private T1 value2;
    private T2 value3;
}
