package autoparams.kotlin

import autoparams.AutoParams
import autoparams.customization.Customization

/**
 * An extension of [AutoParams] tailored for Kotlin tests.
 *
 * This annotation is designed for use in Kotlin tests. It enables AutoParams to
 * provide automatically generated arguments for Kotlin test methods. Using
 * [AutoKotlinParams] ensures that test parameters are populated with
 * generated values with the support of Kotlin's type system.
 *
 * **Example: Using AutoKotlinParams with a Kotlin data class**
 *
 * This example demonstrates how [AutoKotlinParams] can be used to test a
 * Kotlin data class `Point` with default values. The annotation ensures that
 * the `point` parameter in `testMethod` receives automatically generated values
 * for its `x` and `y` properties, rather than the default values of 0.
 * ```kotlin
 * data class Point(val x: Int = 0, val y: Int = 0)
 *
 * class PointTests {
 *
 *     @Test
 *     @AutoKotlinParams
 *     fun testMethod(point: Point) {
 *         // Assert that point.x and point.y are not their default values
 *         assertNotEquals(0, point.x)
 *         assertNotEquals(0, point.y)
 *     }
 * }
 * ```
 *
 * @see AutoParams
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@AutoParams
@Customization(KotlinCustomizer::class)
annotation class AutoKotlinParams
