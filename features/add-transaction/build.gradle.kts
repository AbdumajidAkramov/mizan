plugins {
    id("convention-android-library")
    alias(deps.plugins.compose.compiler)
    alias(deps.plugins.google.ksp)
}

android {
    namespace = "dev.esbi.mizan.features.addtransaction"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.ui)

    implementation(platform(deps.compose.bom))
    implementation(deps.bundles.compose)
    implementation(deps.bundles.decompose)
    implementation(deps.dagger.core)
    implementation(deps.kotlinx.coroutines.core)
    implementation(deps.androidx.lifecycle.viewmodel.compose)

    // CameraX
    implementation(deps.camera.core)
    implementation(deps.camera.camera2)
    implementation(deps.camera.lifecycle)
    implementation(deps.camera.view)

    // ML Kit
    implementation(deps.mlkit.barcode.scanning)

    // Permissions
    implementation(deps.accompanist.permissions)
}
