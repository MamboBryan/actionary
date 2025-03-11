plugins {
    alias(libs.plugins.mambo.conventions.feature)
}

android {
    namespace = "dev.mambo.lib.ui.presentation"
}

dependencies {
    // modules
    api(projects.modulesUi.design)
    implementation(projects.modulesData.domain)
    // libraries
    implementation(libs.bundles.voyager)
    implementation(libs.androidx.core.splashscreen)
}
