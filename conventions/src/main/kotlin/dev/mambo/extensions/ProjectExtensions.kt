package dev.mambo.extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure

internal fun Project.plugins(block: PluginManager.() -> Unit) {
    with(pluginManager) { block() }
}

internal fun Project.library(block: LibraryExtension.() -> Unit) {
    extensions.configure<LibraryExtension> { block() }
}

internal fun Project.application(block: ApplicationExtension.() -> Unit) {
    extensions.configure<ApplicationExtension> { block() }
}
