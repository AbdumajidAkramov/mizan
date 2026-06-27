enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "build-logic"

pluginManagement {
    apply(from = "gradle/plugin-management-settings.gradle.kts")
}

dependencyResolutionManagement {
    apply(from = "gradle/dependency-resolution-management-settings.gradle.kts")
}

include(":android")
include(":checks")
include(":kotlin")
include(":ksp")
include(":utils")
