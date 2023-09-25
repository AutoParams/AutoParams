package autoparams.kotlin

import autoparams.generator.ObjectGenerationContext

inline fun <reified T> ObjectGenerationContext.generate(): T {
    return this.generate { T::class.java } as T
}
