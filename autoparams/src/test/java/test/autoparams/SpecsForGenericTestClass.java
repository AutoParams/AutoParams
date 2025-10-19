package test.autoparams;

import autoparams.AutoSource;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpecsForGenericTestClass {

    public abstract static class HierarchyEntity<T extends HierarchyEntity<T>> {
        private T parent;

        public void changeParent(T parent) {
            this.parent = parent;
        }

        public T getParent() {
            return parent;
        }
    }

    public static class Category extends HierarchyEntity<Category> {
        private final String name;

        public Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    abstract static class HierarchyEntityTest<T extends HierarchyEntity<T>> {
        @ParameterizedTest
        @AutoSource
        void sut_is_not_null(T sut) {
            assertNotNull(sut);
        }

        @ParameterizedTest
        @AutoSource
        void changeParent(T sut, T parent) {
            assertNotNull(sut);
            assertNotNull(parent);
            sut.changeParent(parent);
            assertNotNull(sut.getParent());
        }
    }

    @Nested
    class CategoryTest extends HierarchyEntityTest<Category> {
    }
}
