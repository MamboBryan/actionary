package dev.mambo.conventions

import dev.mambo.extensions.androidTestImplementation
import dev.mambo.extensions.configureAndroid
import dev.mambo.extensions.debugImplementation
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

class FeatureConvention : Plugin<Project> {
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
                apply(catalogs.getPlugin("kotlin-compose-compiler"))
            }
            // setup android
            library {
                configureAndroid(enableCompose = true)
            }
            // setup dependencies
            dependencies {
                // kotlin test
                testImplementation(kotlin("test"))
                androidTestImplementation(kotlin("test"))
                // junit
                testImplementation(catalogs.getLibrary("junit"))
                androidTestImplementation(catalogs.getLibrary("androidx-junit"))
                // androidx
                implementation(catalogs.getLibrary("androidx-core-ktx"))
                implementation(catalogs.getLibrary("androidx-appcompat"))
                androidTestImplementation(catalogs.getLibrary("androidx-espresso-core"))
                // compose
                implementation(platform(catalogs.getLibrary("androidx-compose-bom")))
                implementation(catalogs.getLibrary("androidx-ui"))
                implementation(catalogs.getLibrary("androidx-material3"))
                implementation(catalogs.getLibrary("androidx-ui-graphics"))
                implementation(catalogs.getLibrary("androidx-activity-compose"))
                androidTestImplementation(platform(catalogs.getLibrary("androidx-compose-bom")))
                androidTestImplementation(catalogs.getLibrary("androidx-ui-test-junit4"))
                debugImplementation(catalogs.getLibrary("androidx-ui-tooling"))
                debugImplementation(catalogs.getLibrary("androidx-ui-test-manifest"))
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
