package dev.mambo.conventions

import dev.mambo.extensions.getBundle
import dev.mambo.extensions.getLibrary
import dev.mambo.extensions.implementation
import dev.mambo.extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class KoinConvention: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            val catalogs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                val bom = catalogs.findLibrary("koin-bom").get()
                implementation(platform(bom))
                implementation(catalogs.getBundle("koin"))
                testImplementation(catalogs.getLibrary("koin-test"))
                testImplementation(catalogs.getLibrary("koin-test-junit4"))
            }

        }
    }
}
