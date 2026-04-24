package pt.isel.mappers

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

// Programmer 1 (library CREATOR) code
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class AlternativeName(val name: String)

fun Any.mapTo(dstType: KClass<*>) : Any {
    val src = this
    //  obtain destType primaryConstructor
    val dstConstructor = dstType.primaryConstructor
    // Obtain the src class properties
    val srcProperties: Collection<KProperty1<Any, Any>> =
        src::class.memberProperties as Collection<KProperty1<Any, Any>>

    // Obtain constructor parameters <=> ALl type properties
    val params: Map<KParameter, Any?> =
        dstConstructor!!.parameters.associateWith { parameter ->
            var srcProperty: KProperty1<Any,Any> = srcProperties.find {
                    property ->
                if(parameter.name == property.name)
                    return@find true
                var alternativeName: AlternativeName? = parameter.findAnnotation<AlternativeName>()
                if(alternativeName == null)
                    return@find false
                alternativeName.name == property.name
            }!!
            getValue(srcProperty, parameter, src)
        }

    return dstConstructor.callBy(params)
}


fun getValue(srcProperty: KProperty<*>, dstParam: KParameter, srcObj: Any): Any? {

    val propType = srcProperty.returnType.classifier as KClass<*>
    val paramType = dstParam.type.classifier as KClass<*>
    var srcValue = srcProperty.getter.call(srcObj)
    if(srcValue == null) return srcValue
    return when (propType) {
        String::class, Int::class, Boolean::class -> srcValue
        else -> Mapper(propType, paramType).mapTo(srcValue)
    }
}

class Mapper(srcType: KClass<*>, val dstType: KClass<*>) {
    private val dstConstructor = dstType.primaryConstructor
    private val srcProperties: Collection<KProperty1<Any, Any>> =
        srcType.memberProperties as Collection<KProperty1<Any, Any>>
        private val dstParameters = dstConstructor!!.parameters
   private val dstParamToSrcProp: Map<KParameter, KProperty<*>> =
       dstParameters.associateWith { parameter ->
           var srcProperty: KProperty1<Any, Any> = srcProperties.find { property ->
               val name = parameter.findAnnotation<AlternativeName>()?.name ?: parameter.name
               return@find  name == property.name
           }!!
           return@associateWith srcProperty
       }


    fun mapTo(src: Any) : Any? {
        val params: Map<KParameter, Any?> =
            dstParamToSrcProp.entries.associate {
                    entry ->
                        val param = entry.key
                        val value = getValue(entry.value, param, src)
                        param to value
            }

        return dstConstructor!!.callBy(params)
   }

}


