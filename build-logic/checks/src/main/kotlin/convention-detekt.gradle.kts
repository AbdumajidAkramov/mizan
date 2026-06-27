import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.accessors.dm.LibrariesForDeps

plugins {
    base
    id("io.gitlab.arturbosch.detekt")
}

if (project.name != "gradle-kotlin-dsl-accessors") {
    val deps = the<LibrariesForDeps>()
    dependencies {
        detektPlugins(deps.detekt.compose.rules)
        detektPlugins(deps.detekt.formatting)
    }
}

val detektAll = tasks.register<Detekt>(name = "detektAll") {
    config.setFrom(project.rootDir.resolve(relative = "config/detekt.yml"))
    reportsDir.set(project.rootDir.resolve(relative = "config/reports"))

    parallel = true
    setSource(files(projectDir))

    buildUponDefaultConfig = false

    include("**/*.kt")
    include("**/*.kts")
    exclude("**/build/**")
    exclude("**/resources/**")
    exclude(".gradle")
    exclude("application")

    reports {
        html.required.set(false)
        md.required.set(false)
        sarif.required.set(false)
        txt.required.set(false)
        xml {
            required.set(true)
            outputLocation.set(reportsDir.get().resolve(relative = "detekt-results.xml"))
        }
    }
}

tasks.named("check").configure {
    dependsOn(detektAll)
}
