package pt.isel

// By default an Annotation in Kotlin is already Runtime Retention
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class TagColor(
    val color: String
)
