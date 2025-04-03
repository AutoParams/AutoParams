package test.autoparams

import autoparams.kotlin.AutoKotlinParams
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Examples {

    data class Point(val x: Int = 0, val y: Int = 0)

    @Test
    @AutoKotlinParams
    fun testMethod(point: Point) {
        assertThat(point.x).isNotEqualTo(0)
        assertThat(point.y).isNotEqualTo(0)
    }
}
