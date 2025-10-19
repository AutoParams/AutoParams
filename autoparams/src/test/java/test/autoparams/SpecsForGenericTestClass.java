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

    public abstract static class MiddleEntity<U extends MiddleEntity<U>>
        extends HierarchyEntity<U> {
        private String description;

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class Product extends MiddleEntity<Product> {
        private final int id;

        public Product(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    abstract static class MiddleEntityTest<U extends MiddleEntity<U>>
        extends HierarchyEntityTest<U> {
        @ParameterizedTest
        @AutoSource
        void setDescription(U sut, String description) {
            assertNotNull(sut);
            assertNotNull(description);
            sut.setDescription(description);
            assertNotNull(sut.getDescription());
        }
    }

    @Nested
    class ProductTest extends MiddleEntityTest<Product> {
    }

    public interface GenericRepository<T> {

        void save(T entity);

        T findById(int id);
    }

    public static class CategoryRepository implements GenericRepository<Category> {
        @Override
        public void save(Category entity) {
        }

        @Override
        public Category findById(int id) {
            return new Category("test");
        }
    }

    abstract static class RepositoryTest<R extends GenericRepository<E>, E> {
        @ParameterizedTest
        @AutoSource
        void save_entity(R repository, E entity) {
            assertNotNull(repository);
            assertNotNull(entity);
            repository.save(entity);
        }
    }

    @Nested
    class CategoryRepositoryTest
        extends RepositoryTest<CategoryRepository, Category> {
    }
}
