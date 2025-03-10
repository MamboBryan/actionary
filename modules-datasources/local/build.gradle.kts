plugins {
    alias(libs.plugins.mambo.conventions.module)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "dev.mambo.lib.local"
}

dependencies {
    // androidx-room
    implementation(libs.bundles.androidx.room)
    testImplementation(libs.room.testing)
    // ksp
    ksp(libs.room.compiler)
}
