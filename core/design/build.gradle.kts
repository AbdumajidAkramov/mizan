plugins {
    id("convention-android-library")
    alias(deps.plugins.kotlin.android)
    alias(deps.plugins.compose.compiler)
}
android {
    namespace = "dev.esbi.mizan.design"

    buildFeatures {
        compose = true
    }

}
dependencies {

    implementation(deps.core.ktx)
    implementation(platform(deps.androidx.compose.bom))
    implementation(deps.androidx.compose.ui)
    implementation(deps.androidx.compose.ui.graphics)
    implementation(deps.androidx.compose.ui.tooling.preview)
    implementation(deps.androidx.compose.material3)
    implementation(deps.androidx.compose.foundation.layout)
    implementation(deps.material.icons.core)
    implementation(deps.material.icons.extended)
    implementation(deps.coil)
    implementation(deps.coil.compose)

    debugImplementation(deps.androidx.compose.ui.tooling)
}
