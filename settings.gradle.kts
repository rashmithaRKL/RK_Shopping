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
    versionCatalogs {
        create("libs") {
            version("agp", "8.2.2")
            version("kotlin", "1.9.22")
            version("shimmer", "0.5.0")
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

gradle.beforeProject {
    extra.apply {
        set("android.nonTransitiveRClass", true)
        set("android.nonFinalResIds", false)
        set("android.useAndroidX", true)
        set("android.enableJetifier", true)
    }
}

rootProject.name = "RK_Shopping"
include(":app")
