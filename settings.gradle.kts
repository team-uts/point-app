@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
  plugins {
    kotlin("kapt") version "2.3.21"
  }
}

dependencyResolutionManagement {
  // UnstableApiUsage
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
  }
}

rootProject.name = "point-app"

include(
  "app",
  "domain",
  "persistence",
  "infra",
  "shared",
)
