package autoparams.kotlin

import java.lang.reflect.Proxy
import autoparams.customization.Customization
import org.junit.jupiter.params.provider.ArgumentsSource

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ArgumentsSource(MethodAutoKotlinArgumentsProvider::class)
@Customization(KotlinCustomizer::class)
annotation class MethodAutoKotlinSource(val value: Array<String> = [""]) {

    companion object ProxyFactory {

        fun create(value: Array<String>): MethodAutoKotlinSource {
            return Proxy.newProxyInstance(
                MethodAutoKotlinSource::class.java.classLoader,
                arrayOf(MethodAutoKotlinSource::class.java)
            ) { _, method, _ ->
                when (method.name) {
                    "annotationType" -> MethodAutoKotlinSource::class.java
                    "value" -> value
                    else -> method.defaultValue
                }
            } as MethodAutoKotlinSource
        }
    }
}
