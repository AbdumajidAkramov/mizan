plugins {
    alias(deps.plugins.kotlin.jvm)
}

dependencies {

    // Coroutines (pure Kotlin, no Android)
    implementation(deps.kotlinx.coroutines.core)

}
