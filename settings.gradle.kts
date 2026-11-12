@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    // to use the payment-convention plugin
    maven { url = uri("https://team-uts.github.io/payment-gradle-plugins/maven-repo") }
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  // UnstableApiUsage
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    // payment shared modules.
    maven {
      url = uri("https://team-uts.github.io/payment-shared/maven-repo")
      content { includeGroup("dev.teamuts.shared") }
    }
    mavenCentral()
  }
}

rootProject.name = "point-app"

include(
  "app",
  "domain",
  "persistence",
  "infra",
)
