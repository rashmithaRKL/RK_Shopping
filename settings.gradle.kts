pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

gradle.beforeProject {
    extra.apply {
        set("android.nonTransitiveRClass", true)
        set("android.nonFinalResIds", false)
    }
}

rootProject.name = "RK_Shopping"
include(":app")
