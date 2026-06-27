package uz.esbi.gradle

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.exclude

@Suppress("unused")
fun ModuleDependency.exclude(
    dependency: Provider<MinimalExternalModuleDependency>
) = with(dependency.get().module) {
    exclude(group = group, module = name)
}
