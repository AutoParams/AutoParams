package examples.basic;

import java.util.List;

import autoparams.AutoParams;
import examples.Calculator;
import examples.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Examples from the Quick Start Guide demonstrating basic AutoParams usage.
 * These tests are designed to be executable and serve as documentation.
 */
class QuickStartExamples {
    
    @Test
    @AutoParams
    void testCalculatorAdd(int a, int b) {
        Calculator calculator = new Calculator();
        int result = calculator.add(a, b);
        assertEquals(a + b, result);
    }
    
    @Test
    @AutoParams
    void testArbitraryValues(String first, String second) {
        assertNotNull(first);
        assertNotNull(second);
        assertNotEquals(first, second); // AutoParams generates different values
    }
    
    @Test
    @AutoParams
    void testUserCreation(User user) {
        assertNotNull(user);
        assertNotNull(user.getName());
        assertTrue(user.getAge() > 0);
        assertNotNull(user.getEmail());
    }
    
    @Test
    @AutoParams
    void testGeneratedList(List<Integer> numbers) {
        assertNotNull(numbers);
        assertFalse(numbers.isEmpty());
        
        // AutoParams generates a list with arbitrary integers
        for (Integer number : numbers) {
            assertNotNull(number);
        }
    }
    
    @Test
    @AutoParams
    void testGeneratedArray(String[] items) {
        assertNotNull(items);
        assertTrue(items.length > 0);
        
        // AutoParams generates an array with arbitrary strings
        for (String item : items) {
            assertNotNull(item);
            assertFalse(item.isEmpty());
        }
    }    
}
