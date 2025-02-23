pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.google.com") }
    }
    versionCatalogs {
        create("libs") {
            version("agp", "8.8.0")
            version("kotlin", "1.9.22")
        }
    }
}

rootProject.name = "RK_Shopping"
include(":app")
