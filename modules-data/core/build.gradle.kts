plugins {
    alias(libs.plugins.mambo.conventions.module)
}

android {
    namespace = "dev.mambo.lib.data.core"
}

dependencies {
    implementation(projects.modulesData.domain)
    implementation(projects.modulesDatasources.local)
    implementation(libs.kotlinx.datetime)
}
