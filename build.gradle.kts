import extensions.pluginId
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import task.BuildLifecyclePlugin
import task.test.MockitoAgentProvider
import task.test.TestContainer
import task.test.TestLoggingUtils
import task.test.TestSummary

plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependencyManagement)

  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.kotlin.jpa).apply(false)
  alias(libs.plugins.kotlin.kapt).apply(false)

  alias(libs.plugins.spotless)
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

  val mockitoAgent = configurations.create("mockitoAgent")

  dependencies {
    implementation(projectLibs.spring.boot.starter.webmvc)
    implementation(projectLibs.spring.tx)
    implementation(projectLibs.kotlin.reflect)
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation(projectLibs.kotlin.logging)

    testImplementation(projectLibs.spring.boot.starter.webmvc.test)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(projectLibs.mockito)
    mockitoAgent(projectLibs.mockito) { isTransitive = false }
  }

  tasks.withType<Test> {
    useJUnitPlatform()

    // https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
    jvmArgumentProviders.add(
      objects.newInstance<MockitoAgentProvider>().apply {
        agentJar.from(mockitoAgent)
      }
    )

    jvmArgs("-Xshare:off") // deactivate Class Data Sharing(CDS)

    testLogging {
      events =
        setOf(
          TestLogEvent.FAILED,
          TestLogEvent.SKIPPED,
          TestLogEvent.STANDARD_ERROR,
        )

      exceptionFormat = TestExceptionFormat.FULL
      showExceptions = true
      showCauses = true
      showStackTraces = true
    }

    //        ignoreFailures = true

    addTestListener(
      object : TestListener {
        override fun beforeSuite(desc: TestDescriptor) {}

        // handling after all test finished
        override fun afterSuite(desc: TestDescriptor, result: TestResult) {
          if (desc.parent != null) return

          val summary =
            TestSummary(
              projectName = project.name,
              taskName = name,
              result = result,
            )

          TestContainer.testResults = summary
        }

        override fun beforeTest(desc: TestDescriptor) {}

        // handling after each test finished
        override fun afterTest(desc: TestDescriptor, result: TestResult) {
          TestLoggingUtils.printEachResult(desc, result)
        }
      }
    )
  }

  pluginManager.apply(BuildLifecyclePlugin::class.java)
}
