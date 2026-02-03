buildscript {
    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.13.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
}