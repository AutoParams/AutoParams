package test.autoparams;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ComplexObject {

    private final int value1;
    private final String value2;
    private final UUID value3;
}
