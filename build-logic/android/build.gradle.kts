plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(deps.android.gradle.plugin)
    implementation(deps.android.cache.fix.gradle.plugin)
    implementation(projects.kotlin)
    implementation(projects.utils)
}
