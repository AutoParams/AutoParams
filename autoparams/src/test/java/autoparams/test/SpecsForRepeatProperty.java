package autoparams.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import autoparams.AutoSource;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;

@TestMethodOrder(OrderAnnotation.class)
public class SpecsForRepeatProperty {

    private static int count = 0;

    @ParameterizedTest
    @AutoSource()
    @Order(1)
    void run() {
        count++;
    }

    @Test
    @Order(2)
    void if_repeat_property_not_specified_sut_executes_test_just_one_time() {
        assertEquals(1, count);
    }

    @ParameterizedTest
    @AutoSource(repeat = 3)
    @Order(3)
    void repeat_run() {
        count++;
    }

    @Test
    @Order(4)
    void if_repeat_property_specified_sut_executes_test_repeatedly() {
        assertEquals(1 + 3, count);
    }

}
