// build.gradle.kts (Project level)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.25" apply false // ✅ Actualizado
    id("com.google.devtools.ksp") version "1.9.25-1.0.20" apply false // ✅ Actualizado para Kotlin 1.9.25
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
