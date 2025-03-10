enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("./conventions")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Actionary"

// module folders
include(":modules-datasources")
include(":modules-data")
include(":modules-ui")

// data sources modules
include(":modules-datasources:local")

// data modules
include(":modules-data:domain")
include(":modules-data:core")

// ui modules
include(":modules-ui:presentation")
include(":modules-ui:design")

// app modules
include(":app")
