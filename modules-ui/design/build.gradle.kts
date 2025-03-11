plugins {
    alias(libs.plugins.mambo.conventions.feature)
}

android {
    namespace = "dev.mambo.lib.ui.design"
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
}
