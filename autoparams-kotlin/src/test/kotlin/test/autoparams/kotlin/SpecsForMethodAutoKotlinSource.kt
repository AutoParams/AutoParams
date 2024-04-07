package test.autoparams.kotlin

import java.util.stream.Stream
import autoparams.kotlin.AutoKotlinSource
import autoparams.kotlin.MethodAutoKotlinSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments

class SpecsForMethodAutoKotlinSource {

    companion object {

        @Suppress("unused")
        @JvmStatic
        fun stringIntAndListProvider(): Stream<Arguments> = Stream.of(
            Arguments.of("apple", 1, listOf("a", "b")),
        )
    }

    @ParameterizedTest
    @MethodAutoKotlinSource(["stringIntAndListProvider"])
    fun `SUT correctly fills arguments`(
        value1: String,
        value2: Int,
        value3: List<String>
    ) {
        assertThat(value1).isEqualTo("apple")
        assertThat(value2).isEqualTo(1)
        assertThat(value3).containsExactly("a", "b")
    }

    @ParameterizedTest
    @MethodAutoKotlinSource(["stringIntAndListProvider"])
    fun `SUT correctly fills extra arguments`(
        value1: String,
        value2: Int,
        value3: List<String>,
        value4: DataBagWithDefaultArguments
    ) {
        assertThat(value4).isNotNull()
        assertThat(value4.value2).isNotEmpty()
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory creates instance`(value: Array<String>) {
        assertThat(MethodAutoKotlinSource.create(value)).isNotNull()
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory correctly configures annotationClass`(
        value: Array<String>
    ) {
        val actual: MethodAutoKotlinSource = MethodAutoKotlinSource.create(value)
        assertThat(actual.annotationClass).isEqualTo(MethodAutoKotlinSource::class)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory correctly configures value`(value: Array<String>) {
        val actual: MethodAutoKotlinSource = MethodAutoKotlinSource.create(value)
        assertThat(actual.value).containsExactly(*value)
    }
}
