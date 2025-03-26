package test.autoparams;

import java.util.UUID;

import autoparams.AutoParams;
import org.junit.jupiter.api.Test;

public class SpecsForAutoParams {

    @Test
    @AutoParams
    void sut_resolves_arguments(String args1, UUID args2) {
    }
}
