package test.autoparams.kotlin

import autoparams.kotlin.AutoKotlinSource
import autoparams.kotlin.ValueAutoKotlinSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import kotlin.reflect.KClass

class SpecsForValueAutoKotlinSource {

    @ParameterizedTest
    @ValueAutoKotlinSource(shorts = [16])
    fun `sut correctly fills short argument`(value: Short) {
        assertThat(value).isEqualTo(16)
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(bytes = [16])
    fun `sut correctly fills byte argument`(value: Byte) {
        assertThat(value).isEqualTo(16)
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(ints = [16])
    fun `sut correctly fills int argument`(value: Int) {
        assertThat(value).isEqualTo(16)
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(longs = [16])
    fun `sut correctly fills long argument`(value: Long) {
        assertThat(value).isEqualTo(16)
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(floats = [16.0f])
    fun `sut correctly fills float argument`(value: Float) {
        assertThat(value).isEqualTo(16.0f)
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(doubles = [16.0])
    fun `sut correctly fills double argument`(value: Double) {
        assertThat(value).isEqualTo(16.0)
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(chars = ['a'])
    fun `sut correctly fills char argument`(value: Char) {
        assertThat(value).isEqualTo('a')
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(booleans = [true])
    fun `sut correctly fills boolean argument`(value: Boolean) {
        assertThat(value).isTrue()
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(strings = ["a"])
    fun `sut correctly fills string argument`(value: String) {
        assertThat(value).isEqualTo("a")
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(classes = [String::class])
    fun `sut correctly fills class argument`(value: Class<*>) {
        assertThat(value).isEqualTo(String::class.java)
    }

    @ParameterizedTest
    @ValueAutoKotlinSource(classes = [String::class])
    fun `sut correctly fills kotlin class argument`(value: KClass<*>) {
        assertThat(value).isEqualTo(String::class)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory creates instance`(
        shorts: ShortArray,
        bytes: ByteArray,
        ints: IntArray,
        longs: LongArray,
        floats: FloatArray,
        doubles: DoubleArray,
        chars: CharArray,
        booleans: BooleanArray,
        strings: Array<String>,
        classes: Array<KClass<*>>
    ) {
        val actual: ValueAutoKotlinSource = ValueAutoKotlinSource.create(
            shorts,
            bytes,
            ints,
            longs,
            floats,
            doubles,
            chars,
            booleans,
            strings,
            classes
        )

        assertThat(actual).isNotNull
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory correctly configures annotationClass`(
        shorts: ShortArray,
        bytes: ByteArray,
        ints: IntArray,
        longs: LongArray,
        floats: FloatArray,
        doubles: DoubleArray,
        chars: CharArray,
        booleans: BooleanArray,
        strings: Array<String>,
        classes: Array<KClass<*>>
    ) {
        val actual: ValueAutoKotlinSource = ValueAutoKotlinSource.create(
            shorts,
            bytes,
            ints,
            longs,
            floats,
            doubles,
            chars,
            booleans,
            strings,
            classes
        )

        assertThat(actual.annotationClass)
            .isEqualTo(ValueAutoKotlinSource::class)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory correctly configures value`(
        shorts: ShortArray,
        bytes: ByteArray,
        ints: IntArray,
        longs: LongArray,
        floats: FloatArray,
        doubles: DoubleArray,
        chars: CharArray,
        booleans: BooleanArray,
        strings: Array<String>,
        classes: Array<KClass<*>>
    ) {
        val actual: ValueAutoKotlinSource = ValueAutoKotlinSource.create(
            shorts,
            bytes,
            ints,
            longs,
            floats,
            doubles,
            chars,
            booleans,
            strings,
            classes
        )

        assertThat(actual.shorts).containsExactly(*shorts)
        assertThat(actual.bytes).containsExactly(*bytes)
        assertThat(actual.ints).containsExactly(*ints)
        assertThat(actual.longs).containsExactly(*longs)
        assertThat(actual.floats).containsExactly(*floats)
        assertThat(actual.doubles).containsExactly(*doubles)
        assertThat(actual.chars).containsExactly(*chars)
        assertThat(actual.booleans).containsExactly(*booleans)
        assertThat(actual.strings).containsExactly(*strings)
        assertThat(actual.classes).containsExactly(*classes)
    }
}
