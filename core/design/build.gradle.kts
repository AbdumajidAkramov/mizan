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

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.coil)
    implementation(libs.coil.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
