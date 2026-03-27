package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class AlternativeName(val propName: String)

data class ADto(val x: Int = 0, val y: Int = 0)
data class ADomain(@param:AlternativeName("x")val a: Int = 0, val y: Int = 0) {

}


fun ADtoToADomain(adto: ADto) : ADomain {
    return ADomain(adto.x, adto.y)
}

fun mapTo(src: Any, dstType: KClass<*>) : Any {
    //  obtain destType primaryConstructor
    val dstConstructor = dstType.primaryConstructor
    // Obtain the src class properties
    val srcProperties: Collection<KProperty1<Any, Any>> = src::class.memberProperties as Collection<KProperty1<Any, Any>>

    // Obtain constructor parameters <=> ALl type properties
    val params: Map<KParameter, Any> = dstConstructor!!.parameters.associateWith { parameter ->
        var srcProperty: KProperty1<Any,Any> = srcProperties.find {
            if(parameter.name == it.name)
                return@find true
            var alternativeName: AlternativeName? = parameter.findAnnotation<AlternativeName>()
            if(alternativeName == null)
                return@find false
            alternativeName.propName == it.name
        }!!
        return@associateWith srcProperty.get(src)
    }

    return dstConstructor.callBy(params)
}



fun main() {
    val adto = ADto(2,-3)
    val adomain1: ADomain = ADtoToADomain(adto)
    println(adomain1)
    val adomain2: ADomain = mapTo(adto, ADomain::class) as ADomain
    println(adomain2)

}