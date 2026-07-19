plugins {
    alias(deps.plugins.kotlin.jvm)
    alias(deps.plugins.kotlin.serialization)
}

dependencies {
    implementation(deps.kotlinx.coroutines.core)
    implementation(deps.kotlinx.serialization.json)
    implementation(deps.dagger.core)
}
