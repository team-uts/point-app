@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  // UnstableApiUsage
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    // jitpack.io is used to fetch the payment-shared library
    maven { url = uri("https://jitpack.io") }
  }
}

rootProject.name = "point-app"

include(
  "app",
  "domain",
  "persistence",
  "infra",
)
