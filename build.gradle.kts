import dev.teamuts.gradle.utils.pluginId

plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependencyManagement)

  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kotlin.jpa).apply(false)
  alias(libs.plugins.kotlin.kapt).apply(false)

  alias(libs.plugins.spotless)
  alias(libs.plugins.teamuts.paymentConvention)
}

val projectLibs = libs

allprojects {
  group = "dev.teamuts.point"
  description = "point-app"

  apply {
    plugin(projectLibs.plugins.spotless.pluginId())
  }

  spotless {
    kotlin {
      target("src/**/*.kt")
      targetExclude("**/generated/**", "**/build/**")

      ktfmt().googleStyle()
      trimTrailingWhitespace()
      endWithNewline()
    }

    kotlinGradle {
      target("**/*.gradle.kts")
      targetExclude("**/build/**")

      ktfmt().googleStyle()
      trimTrailingWhitespace()
      endWithNewline()
    }
  }
}

subprojects {
  apply {
    plugin(projectLibs.plugins.spring.boot.pluginId())
    plugin(projectLibs.plugins.spring.dependencyManagement.pluginId())
    plugin(projectLibs.plugins.kotlin.jvm.pluginId())
    plugin(projectLibs.plugins.kotlin.spring.pluginId())
    plugin(projectLibs.plugins.kotlin.jpa.pluginId())
    plugin(projectLibs.plugins.teamuts.paymentConvention.pluginId())
  }

  java {
    toolchain {
      languageVersion = JavaLanguageVersion.of(projectLibs.versions.java.get())
    }
  }

  kotlin {
    compilerOptions {
      freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
  }

  val mockitoAgent = configurations.maybeCreate("mockitoAgent")
  dependencies {
    implementation(projectLibs.spring.boot.starter.webmvc)
    implementation(projectLibs.spring.tx)
    implementation(projectLibs.kotlin.reflect)
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation(projectLibs.kotlin.logging)

    implementation(projectLibs.teamuts.payment.shared)

    testImplementation(projectLibs.spring.boot.starter.webmvc.test)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(projectLibs.mockito)
    mockitoAgent(projectLibs.mockito) { isTransitive = false }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  // https://github.com/team-uts/payment-gradle-plugins
  paymentConvention {
    testLogging {
      enabled = true
    }
  }
}
