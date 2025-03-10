package dev.mambo.extensions

import com.android.build.gradle.LibraryExtension
import dev.mambo.models.AndroidSdk
import org.gradle.api.JavaVersion

fun LibraryExtension.configureAndroid(enableCompose: Boolean = false) {
    compileSdk = AndroidSdk.compileSdk
    defaultConfig {
        minSdk = AndroidSdk.minimumSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    if (enableCompose)
        buildFeatures {
            compose = true
        }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
