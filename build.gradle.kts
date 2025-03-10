import org.gradle.kotlin.dsl.ktlint
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    // kotlin
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    // ktlint
    alias(libs.plugins.ktlint)
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        enableExperimentalRules.set(true)
        reporters {
            reporter(ReporterType.JSON)
        }
        filter {
            exclude {
                projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/")
            }
            exclude {
                projectDir.toURI().relativize(it.file.toURI()).path.contains("/build/")
            }
        }
    }
}
