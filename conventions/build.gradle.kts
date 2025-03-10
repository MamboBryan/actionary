plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

group = "dev.mambo.conventions"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        fun createPlugin(value: String, className: String) {
            plugins.register(value) {
                id = value
                implementationClass = className
            }
        }
        createPlugin("mambo.conventions.koin", "dev.mambo.conventions.KoinConvention")
        createPlugin("mambo.conventions.module", "dev.mambo.conventions.ModuleConvention")
        createPlugin("mambo.conventions.feature", "dev.mambo.conventions.FeatureConvention")
    }
}