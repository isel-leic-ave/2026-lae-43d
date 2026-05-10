package pt.isel.lae43d

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


// Programmer 1 (library CREATOR) code
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class AlternativeName(val name: String)


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class MapTo(val destName: String)

class MapperReflection<T : Any, R : Any>(private val srcType: KClass<T>, private val dstType: KClass<R>) : Mapper<T, R> {
    val primaryConstructor = dstType.primaryConstructor!!
    val parameterToProp: Map<KParameter, (Any) -> Any?> =
        primaryConstructor.parameters.associateWith { parameter ->
            val prop = getCorrespondingProperty(parameter, srcType)!!
            return@associateWith getParameterFunctionGetterValue(prop, parameter)
        }

    override fun mapFrom(src: T): R {
        val argumentsMap: Map<KParameter, Any?> =
            parameterToProp.entries.associate { (param, propValueGetter) ->
                val obj = propValueGetter(src)
                return@associate param to obj
            }
        return primaryConstructor.callBy(argumentsMap)
    }

    private fun getParameterValue(propSrc: KProperty<*>, paramDest: KParameter, src: Any): Any? {

        if(propSrc.returnType == paramDest.type)
            return propSrc.getter!!.call(src)
        val propType = propSrc.returnType.classifier as KClass<*>
        val paramType = paramDest.type.classifier as KClass<*>
        val propValue = propSrc.getter!!.call(src)
        val mapper = MapperReflection(propType, paramType)

        return if(propValue == null) propValue else null//mapper.mapFrom(propValue)
    }

    private fun getParameterFunctionGetterValue(propSrc: KProperty<*>, paramDest: KParameter): (src: Any) -> Any? {
        if(propSrc.returnType == paramDest.type)
            return { src: Any -> propSrc.getter.call(src) }

        val propType = propSrc.returnType.classifier as KClass<*>
        val paramType = paramDest.type.classifier as KClass<*>
        val mapper = MapperReflection(propType, paramType)
        return { src: Any ->
            val propValue = propSrc.getter.call(src)
            if(propValue == null) propValue else null//mapper.mapFrom(propValue)
        }
    }

    private fun getCorrespondingProperty(parameter: KParameter, srcType: KClass<*>): KProperty<*>? {
        return srcType.memberProperties.find { property ->
            if (property.name == parameter.name)
                return@find true
            val mapToAnnotation = property.findAnnotation<AlternativeName>()
            if (mapToAnnotation == null)
                return@find false
            return@find mapToAnnotation.name == parameter.name
        } as KProperty<*>?

    }
}




