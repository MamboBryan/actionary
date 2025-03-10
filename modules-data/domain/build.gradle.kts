plugins {
    alias(libs.plugins.mambo.conventions.module)
}

android {
    namespace = "dev.mambo.lib.data.domain"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}
