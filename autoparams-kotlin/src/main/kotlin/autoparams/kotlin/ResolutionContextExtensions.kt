package autoparams.kotlin

import autoparams.ResolutionContext

@Deprecated(
    "Use ResolutionContext.resolve extension method instead",
    ReplaceWith("resolve", "autoparams.kotlin.resolve")
)
inline fun <reified T> ResolutionContext.generate(): T {
    return this.resolve { T::class.java } as T
}

inline fun <reified T> ResolutionContext.resolve(): T {
    return this.resolve { T::class.java } as T
}
