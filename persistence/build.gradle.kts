import dev.teamuts.gradle.flyway.FlywayManager
import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false

jar.enabled = true

plugins {
  alias(libs.plugins.kotlin.jpa)
  alias(libs.plugins.kotlin.kapt)

  alias(libs.plugins.flyway)
}

buildscript {
  dependencies {
    classpath(libs.flyway.mysql)
  }
}

noArg {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.Embeddable")
  annotation("jakarta.persistence.MappedSuperclass")
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

dependencies {
  implementation(project(":domain"))

  implementation(libs.flyway.core)
  implementation(libs.flyway.mysql)

  implementation(libs.spring.boot.h2console)
  implementation(libs.spring.boot.starter.data.jpa)

  implementation(variantOf(libs.querydsl.jpa) { classifier("jakarta") })
  kapt(variantOf(libs.querydsl.apt) { classifier("jakarta") })
  kapt("jakarta.persistence:jakarta.persistence-api")

  runtimeOnly(libs.database.mysql)
  testRuntimeOnly(libs.database.h2)
  testImplementation(libs.spring.boot.starter.data.jpa.test)
}

flyway {
  val flywayManager = FlywayManager.init()

  baselineDescription = "Start Flyway Migration!"
  baselineOnMigrate = true
  locations = flywayManager.getLocations()
  configFiles = flywayManager.getConfigFiles()
  user = flywayManager.getUser()
  password = flywayManager.getPassword()
}
