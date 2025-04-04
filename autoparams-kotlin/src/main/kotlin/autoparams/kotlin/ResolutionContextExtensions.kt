package autoparams.kotlin

import autoparams.ResolutionContext

inline fun <reified T> ResolutionContext.resolve(): T {
    return this.resolve { T::class.java } as T
}
