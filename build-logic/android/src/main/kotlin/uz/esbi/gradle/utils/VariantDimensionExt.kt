package uz.esbi.gradle.utils

import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Project
import uz.esbi.gradle.propertyOrThrow

private const val VERSION_CODE_FIELD_KEY = "app.version.code"
private const val VERSION_CODE_FIELD_NAME = "VERSION_CODE"
private const val VERSION_NAME_FIELD_KEY = "app.version.name"
private const val VERSION_NAME_FIELD_NAME = "VERSION_NAME"

fun VariantDimension.addAppVersionToBuildConfig(project: Project) {
    buildConfigField(
        name = VERSION_CODE_FIELD_NAME,
        value = project.propertyOrThrow(VERSION_CODE_FIELD_KEY, Int::class.java)
    )
    buildConfigField(
        name = VERSION_NAME_FIELD_NAME,
        value = project.propertyOrThrow(VERSION_NAME_FIELD_KEY, String::class.java)
    )
}

fun VariantDimension.buildConfigField(name: String, value: Any) {
    val stringValue = if (value is String) "\"$value\"" else value.toString()
    buildConfigField(value::class.java.name, name, stringValue)
}
