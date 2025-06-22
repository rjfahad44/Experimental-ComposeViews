pluginManagement {
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
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") } // 🔥 Added Kotlin EAP
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") } // 🔥 Added Kotlin EAP
    }
}

rootProject.name = "Experimental-ComposeViews"
include(":app")
include(":StackSwipeCardPager")
include(":MoveableImage")
include(":PartyEffect")
