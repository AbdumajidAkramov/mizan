plugins {
    id("convention-android-app")
    alias(deps.plugins.android.application)
    alias(deps.plugins.compose.compiler)
    alias(deps.plugins.kotlin.serialization)
    alias(deps.plugins.google.ksp)
    alias(deps.plugins.google.services)
    alias(deps.plugins.firebase.crashlytics)
}

android {
    namespace = "dev.esbi.mizan"

    defaultConfig {
        applicationId = "dev.esbi.mizan"
        multiDexEnabled = true
        versionCode = property("app.version.code")?.toString()?.toInt()
        versionName = property("app.version.name")?.toString()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    bundle {
        language.enableSplit = false
    }
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.ui)
    implementation(projects.features.dashboard)
    implementation(projects.features.addTransaction)

    // Compose
    implementation(deps.core.ktx)
    implementation(deps.androidx.lifecycle.runtime.ktx)
    implementation(deps.androidx.activity.compose)
    implementation(platform(deps.androidx.compose.bom))
    implementation(deps.androidx.compose.ui)
    implementation(deps.androidx.compose.ui.graphics)
    implementation(deps.androidx.compose.ui.tooling.preview)
    implementation(deps.androidx.compose.material3)
    implementation(deps.androidx.compose.foundation.layout)
    implementation(deps.material.icons.core)
    implementation(deps.material.icons.extended)

    // Navigation
    implementation(deps.androidx.navigation.compose)

    // Kotlin Serialization
    implementation(deps.kotlinx.serialization.json)

    // Dagger2
    implementation(deps.dagger.core)
    ksp(deps.dagger.compiler)

    // Vico Charts
    implementation(deps.vico.compose)
    implementation(deps.vico.compose.m3)
    implementation(deps.vico.core)

    // ViewModel
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

    // DataStore
    implementation(deps.datastore)
    implementation(deps.datastore.core.okio)

    // Firebase (versions managed by BOM)
    implementation(platform(deps.firebase.bom))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-messaging")

    testImplementation(deps.junit4)
    androidTestImplementation(deps.androidx.junit)
    androidTestImplementation(deps.androidx.espresso.core)
    androidTestImplementation(platform(deps.androidx.compose.bom))
    androidTestImplementation(deps.androidx.compose.ui.test.junit4)
    debugImplementation(deps.androidx.compose.ui.tooling)
    debugImplementation(deps.androidx.compose.ui.test.manifest)
}
