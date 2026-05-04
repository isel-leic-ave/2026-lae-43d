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

class MapperReflection(private val srcType: KClass<*>, private val dstType: KClass<*>) {
    val primaryConstructor = dstType.primaryConstructor!!
    val parameterToProp: Map<KParameter, (Any) -> Any?> =
        primaryConstructor.parameters.associateWith { parameter ->
            val prop = getCorrespondingProperty(parameter, srcType)!!
            return@associateWith getParameterFunctionGetterValue(prop, parameter)
        }

    fun mapTo(src: Any): Any {
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

        return if(propValue == null) propValue else mapper.mapTo(propValue)
    }

    private fun getParameterFunctionGetterValue(propSrc: KProperty<*>, paramDest: KParameter): (src: Any) -> Any? {
        if(propSrc.returnType == paramDest.type)
            return { src: Any -> propSrc.getter.call(src) }

        val propType = propSrc.returnType.classifier as KClass<*>
        val paramType = paramDest.type.classifier as KClass<*>
        val mapper = MapperReflection(propType, paramType)
        return { src: Any ->
            val propValue = propSrc.getter.call(src)
            if(propValue == null) propValue else mapper.mapTo(propValue)
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



fun Any.mapTo(dstType: KClass<*>): Any {
    val src = this
    // Obtain the source type KClass representative
    val srcType = this::class
    val primaryConstructor = dstType.primaryConstructor!!
    // Para cada parâmetro do construtor primário, obter o valor da
    // propriedade com o mesmo nome no objeto src

    // For each primary constructor parameter, obtain the value of the
    // property with the same name in src object
    var argumentsMap: Map<KParameter, Any?> =
        primaryConstructor.parameters.associateWith { parameter ->
            val propSrc: KProperty<*>? = srcType.memberProperties.find { property ->
                property.returnType
                if (property.name == parameter.name)
                    return@find true
                val mapToAnnotation = property.findAnnotation<AlternativeName>()
                if (mapToAnnotation == null)
                    return@find false
                return@find mapToAnnotation.name == parameter.name


            } as KProperty<*>?
            // Goal: return the value corresponding to the parameter argument
            return@associateWith getValue(propSrc, parameter, src)
        }
    // Obtain the primary constructor for the destination type and call it with all the
    // arguments in argumentsMap

    return primaryConstructor.callBy(argumentsMap)
}

private fun getValue(propSrc: KProperty<*>?, paramDest: KParameter, src: Any): Any? {
    val propValue = propSrc!!.call(src)
    if(propSrc.returnType == paramDest.type)
        return propValue

    return propValue?.mapTo(paramDest.type.classifier as KClass<*>)
}



