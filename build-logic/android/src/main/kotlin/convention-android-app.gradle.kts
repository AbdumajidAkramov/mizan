import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.AppExtension
import uz.esbi.gradle.utils.currentArtifactTaskOrNull

plugins {
    id("com.android.application")
    id("org.gradle.android.cache-fix")
    id("kotlin-android")
    id("convention-kotlin-base")
    id("convention-android-base")
}

@Suppress("DEPRECATION")
configure<AppExtension> {
    lintOptions.isCheckDependencies = true
}

@Suppress("DEPRECATION")
configure<ApplicationAndroidComponentsExtension> {
    onVariants { variant ->
        afterEvaluate {
            currentArtifactTaskOrNull?.doLast {
                val apkPath = variant.artifacts.get(SingleArtifact.APK).get().asFile
                val bundlePath = variant.artifacts.get(SingleArtifact.BUNDLE).orNull?.asFile?.parent
                copy {
                    from(apkPath, bundlePath)
                    include("*.aab", "*.apk", "*.json")
                    into("${project.buildDir}/artifacts/")
                }
            }
        }
    }
}
