plugins {
    alias(libs.plugins.mambo.conventions.feature)
}

android {
    namespace = "dev.mambo.lib.ui.presentation"
}

dependencies {
    api(projects.modulesUi.design)
    implementation(libs.bundles.voyager)
    implementation(projects.modulesData.domain)
}
