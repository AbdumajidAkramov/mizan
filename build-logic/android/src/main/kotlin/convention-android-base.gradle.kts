import com.android.build.gradle.BaseExtension
import uz.esbi.gradle.propertyOrDefault
import uz.esbi.gradle.withDeps

configure<BaseExtension> {
    project.withDeps { deps ->
        val sdkVersions = deps.versions.android.build
        buildToolsVersion(sdkVersions.tools.get())
        compileSdkVersion(sdkVersions.compileSdk.get().toInt())
        defaultConfig {
            minSdk = sdkVersions.minSdk.get().toInt()
            targetSdk = sdkVersions.targetSdk.get().toInt()
        }

        @Suppress("UnstableApiUsage")
        composeOptions {
            kotlinCompilerExtensionVersion = deps.versions.compose.bom.get()
        }
    }

    buildTypes {
        create("staging") {
            matchingFallbacks += listOf("release")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }


    @Suppress("DEPRECATION")
    lintOptions {
        isAbortOnError = project.propertyOrDefault("build.lint.abortOnError", false)
        isCheckReleaseBuilds = false
        isQuiet = project.propertyOrDefault("build.lint.quiet", false)
        isIgnoreWarnings = project.propertyOrDefault("build.lint.ignoreWarnings", false)
        isWarningsAsErrors = project.propertyOrDefault("build.lint.warningAsErrors", false)
        htmlReport = false
        textReport = false

        lintConfig = project.rootDir.resolve(relative = "config/lint.xml")
        baselineFile = project.rootDir.resolve(relative = "config/lint-baseline.xml")
        xmlOutput = project.rootDir.resolve(relative = "config/reports/lint-results.xml")
    }

    packagingOptions {
        resources.excludes.add("/META-INF/*")
        resources.excludes.add("/META-INF/*.kotlin_module")
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    with(buildFeatures) {
        aidl = false
        compose = false
        buildConfig = false
        prefab = false
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = false
    }
}

project.withDeps { deps ->
    dependencies.add("coreLibraryDesugaring", deps.android.tools.desugar)
}
