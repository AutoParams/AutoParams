package autoparams.kotlin

import autoparams.AutoSource
import autoparams.customization.Customization

/**
 * An extension of [AutoSource] tailored for Kotlin parameterized tests.
 *
 * This annotation is designed for use in Kotlin parameterized tests. It enables
 * AutoParams to provide automatically generated arguments for test methods.
 * Using [AutoKotlinSource] ensures that parameters are populated with generated
 * values with the support of Kotlin's type system.
 *
 * **Example: Using AutoKotlinSource in a parameterized test**
 *
 * This example demonstrates how [AutoKotlinSource] can be used in a Kotlin
 * parameterized test. The annotation ensures that the `point` parameter in
 * `testMethod` receives automatically generated values for its `x` and `y`
 * properties, rather than the default values of 0.
 * ```kotlin
 * data class Point(val x: Int = 0, val y: Int = 0)
 *
 * class PointTests {
 *
 *     @ParameterizedTest
 *     @AutoKotlinSource
 *     fun testMethod(point: Point) {
 *         // Assert that point.x and point.y are not their default values
 *         assertNotEquals(0, point.x)
 *         assertNotEquals(0, point.y)
 *     }
 * }
 * ```
 *
 * @see autoparams.AutoSource
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@AutoSource
@Customization(KotlinCustomizer::class)
annotation class AutoKotlinSource
