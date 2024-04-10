package autoparams.kotlin

import java.lang.reflect.Proxy
import autoparams.customization.Customization
import org.junit.jupiter.params.provider.ArgumentsSource
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ArgumentsSource(ValueAutoKotlinArgumentsProvider::class)
@Customization(KotlinCustomizer::class)
annotation class ValueAutoKotlinSource(
    val shorts: ShortArray = [],
    val bytes: ByteArray = [],
    val ints: IntArray = [],
    val longs: LongArray = [],
    val floats: FloatArray = [],
    val doubles: DoubleArray = [],
    val chars: CharArray = [],
    val booleans: BooleanArray = [],
    val strings: Array<String> = [],
    val classes: Array<KClass<*>> = [],
) {

    companion object ProxyFactory {

        fun create(
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
        ): ValueAutoKotlinSource {
            return Proxy.newProxyInstance(
                ValueAutoKotlinSource::class.java.classLoader,
                arrayOf(ValueAutoKotlinSource::class.java)
            ) { _, method, _ ->
                when (method.name) {
                    "annotationType" -> ValueAutoKotlinSource::class.java
                    "shorts" -> shorts
                    "bytes" -> bytes
                    "ints" -> ints
                    "longs" -> longs
                    "floats" -> floats
                    "doubles" -> doubles
                    "chars" -> chars
                    "booleans" -> booleans
                    "strings" -> strings
                    "classes" -> classes.map { it.java }.toTypedArray()
                    else -> method.defaultValue
                }
            } as ValueAutoKotlinSource
        }
    }
}
