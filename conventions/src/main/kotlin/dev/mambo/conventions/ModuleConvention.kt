package dev.mambo.conventions

import dev.mambo.extensions.androidTestImplementation
import dev.mambo.extensions.configureAndroidLibrary
import dev.mambo.extensions.getLibrary
import dev.mambo.extensions.getPlugin
import dev.mambo.extensions.implementation
import dev.mambo.extensions.library
import dev.mambo.extensions.plugins
import dev.mambo.extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

class ModuleConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val catalogs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            // setup module plugins
            plugins {
                apply(catalogs.getPlugin("android-library"))
                apply(catalogs.getPlugin("kotlin-android"))
                apply(catalogs.getPlugin("ktlint"))
                apply(catalogs.getPlugin("kotlin-serialization"))
                apply(catalogs.getPlugin("mambo-conventions-koin"))
            }
            // setup android
            library {
                configureAndroidLibrary()
            }
            // setup dependencies
            dependencies {
                // kotlin test
                androidTestImplementation(kotlin("test"))
                testImplementation(kotlin("test"))
                // androidx
                implementation(catalogs.getLibrary("androidx-core-ktx"))
                implementation(catalogs.getLibrary("androidx-appcompat"))
                androidTestImplementation(catalogs.getLibrary("androidx-junit"))
                androidTestImplementation(catalogs.getLibrary("androidx-espresso-core"))
                // timber
                implementation(catalogs.getLibrary("timber"))
                // coroutines
                implementation(catalogs.getLibrary("kotlinx-coroutines-android"))
                testImplementation(catalogs.getLibrary("kotlinx-coroutines-test"))
                androidTestImplementation(catalogs.getLibrary("kotlinx-coroutines-test"))
                // serialization
                implementation(catalogs.getLibrary("kotlinx-serialization-json"))
                // kotlinx datetime
                implementation(catalogs.getLibrary("kotlinx-datetime"))
                // mockk
                testImplementation(catalogs.getLibrary("mockk"))
                androidTestImplementation(catalogs.getLibrary("mockk"))
            }
        }
    }
}
