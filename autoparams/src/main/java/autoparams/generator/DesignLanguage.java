package autoparams.generator;

public class DesignLanguage<T> extends DesignContext<T, DesignLanguage<T>> {

    DesignLanguage() {
    }

    @Override
    DesignLanguage<T> context() {
        return this;
    }

    ObjectGenerator[] getGenerators() {
        return generators.toArray(new ObjectGenerator[0]);
    }
}
