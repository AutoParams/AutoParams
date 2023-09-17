package test.autoparams;

import autoparams.AutoSource;
import autoparams.Repeat;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpecsForRepeat {

    private static int shouldBe10 = 0;
    private static int shouldBe3 = 0;

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    @Order(1)
    void sut_work_correctly(int x) {
        shouldBe10++;
    }

    @Test
    @Order(2)
    void verify_sut_work_correctly() {
        assertEquals(10, shouldBe10);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat
    @Order(3)
    void sut_has_default_value() {
        shouldBe3++;
    }

    @Test
    @Order(4)
    void verify_sut_has_default_value() {
        assertEquals(3, shouldBe3);
    }

}
