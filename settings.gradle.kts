apply(pathDir = "gradle/include")
rootProject.name = "Mizan"

includeBuild("build-logic")

pluginManagement {
    apply(from = "build-logic/gradle/plugin-management-settings.gradle.kts")
}

dependencyResolutionManagement {
    apply(from = "build-logic/gradle/dependency-resolution-management-settings.gradle.kts")
}

fun apply(pathDir: String) {
    val files = File(pathDir).listFiles() ?: return
    for (file in files) {
        if (file.isDirectory) {
            apply(pathDir = file.path)
        }
        if (file.isFile && file.name.endsWith(suffix = ".gradle")) {
            apply(from = file.path)
        }
    }
}
