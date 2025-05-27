package test.autoparams.animal;

public abstract sealed class Animal permits Mammal, Bird, Fish {
}
