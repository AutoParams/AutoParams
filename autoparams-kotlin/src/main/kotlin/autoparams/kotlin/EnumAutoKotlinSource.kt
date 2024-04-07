package autoparams.kotlin

import java.lang.reflect.Proxy
import autoparams.customization.Customization
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.EnumSource
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ArgumentsSource(EnumAutoKotlinArgumentsProvider::class)
@Customization(KotlinCustomizer::class)
annotation class EnumAutoKotlinSource(
    val value: KClass<out Enum<*>>,
    val names: Array<String> = [],
    val mode: EnumSource.Mode = EnumSource.Mode.INCLUDE
) {

    companion object ProxyFactory {

        fun create(
            value: KClass<out Enum<*>>,
            names: Array<String>,
            mode: EnumSource.Mode
        ): EnumAutoKotlinSource = Proxy.newProxyInstance(
            EnumAutoKotlinSource::class.java.classLoader,
            arrayOf(EnumAutoKotlinSource::class.java)
        ) { _, method, _ ->
            when (method.name) {
                "annotationType" -> EnumAutoKotlinSource::class.java
                "value" -> value.java
                "names" -> names
                "mode" -> mode
                else -> method.defaultValue
            }
        } as EnumAutoKotlinSource
    }
}
