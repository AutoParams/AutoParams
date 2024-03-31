package test.autoparams.kotlin

data class AllParametersHaveDefaultArguments(
    val value1: String = "",
    val value2: List<Int> = emptyList(),
    val value3: Long = 0,
)
