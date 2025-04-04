package autoparams.kotlin

import autoparams.AssetConverter
import autoparams.ParameterQuery
import kotlin.reflect.KClass

internal class KotlinClassAssetConverter(
    private val baseConverter: AssetConverter
) : AssetConverter {

    override fun convert(query: ParameterQuery, asset: Any?): Any? = if (
        query.parameter.type.equals(KClass::class.java) &&
        asset is Class<*>
    ) {
        asset.kotlin
    } else {
        baseConverter.convert(query, asset)
    }
}
