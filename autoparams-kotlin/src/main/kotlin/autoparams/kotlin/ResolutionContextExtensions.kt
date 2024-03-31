package autoparams.kotlin

import autoparams.ResolutionContext

inline fun <reified T> ResolutionContext.generate(): T {
    return this.generate { T::class.java } as T
}
