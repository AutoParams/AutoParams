package test.autoparams.kotlin

import java.lang.annotation.ElementType
import java.util.UUID
import autoparams.kotlin.AutoKotlinSource
import autoparams.kotlin.EnumAutoKotlinSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@Suppress("UNUSED_PARAMETER")
class SpecsForEnumAutoKotlinSource {

    @ParameterizedTest
    @EnumAutoKotlinSource(ElementType::class)
    fun `SUT correctly fills first argument`(value: ElementType) {
    }

    @ParameterizedTest
    @EnumAutoKotlinSource(ElementType::class)
    fun `SUT correctly fills extra arguments`(
        value1: ElementType,
        value2: UUID,
        value3: DataBagWithDefaultArguments
    ) {
        assertNotNull(value1)
        assertNotNull(value2)
        assertThat(value3.value2).isNotEmpty()
    }

    @ParameterizedTest
    @EnumAutoKotlinSource(ElementType::class, names = ["TYPE"])
    fun `SUT accepts names`(value: ElementType) {
        assertEquals(ElementType.TYPE, value)
    }

    @ParameterizedTest
    @EnumAutoKotlinSource(
        ElementType::class,
        names = ["TYPE"],
        mode = EnumSource.Mode.EXCLUDE
    )
    fun `SUT accepts mode`(value: ElementType) {
        assertNotEquals(ElementType.TYPE, value)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory creates instance`(
        value: ElementType,
        names: Array<String>,
        mode: EnumSource.Mode
    ) {
        val actual: EnumAutoKotlinSource = EnumAutoKotlinSource.create(
            ElementType::class,
            names,
            mode
        )

        assertNotNull(actual)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory correctly configures annotationClass`(
        value: ElementType,
        names: Array<String>,
        mode: EnumSource.Mode
    ) {
        val actual: EnumAutoKotlinSource = EnumAutoKotlinSource.create(
            ElementType::class,
            names,
            mode
        )

        assertEquals(EnumAutoKotlinSource::class, actual.annotationClass)
    }

    @ParameterizedTest
    @AutoKotlinSource
    fun `Proxy factory correctly configures properties`(
        value: ElementType,
        names: Array<String>,
        mode: EnumSource.Mode
    ) {
        val actual: EnumAutoKotlinSource = EnumAutoKotlinSource.create(
            ElementType::class,
            names,
            mode
        )

        assertEquals(ElementType::class, actual.value)
        assertEquals(names, actual.names)
        assertEquals(mode, actual.mode)
    }
}
