//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//    ext.kotlin_version = "1.3.50"
//    ext.navigationVersion = "2.1.0-alpha05"
//    repositories {
//        google()
////        mavenCentral()
//        maven { url 'httpsps://maven.fabric.io/public' }
//        jcenter()
//    }
//    dependencies {
////        classpath "com.android.tools.build:gradle:4.2.2"
////        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
//
//        // Added these from yap Legacy
//        classpath 'io.fabric.tools:gradle:1.31.2'
//        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.3.0'
//        classpath 'com.google.gms:google-services:4.3.4'  // Google Services plugin
//        classpath 'com.android.tools.build:gradle:3.6.3'
//        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
//        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"
//        classpath ("com.github.dcendents:android-maven-gradle-plugin:2.0")
//        // NOTE: Do not place your application dependencies here; they belong
//        // in the individual module build.gradle files
//    }
//}
//
//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        jcenter() // Warning: this repository is going to shut down soon
//
//        // Added these from yap Legacy
//        maven { url "https://repo.leanplum.com/" }
//        maven { url 'https://jitpack.io' }
//        maven { url "https://dl.bintray.com/drummer-aidan/maven" }
//        maven { url "http://pay.cards/maven" }
//    }
//}
//
//task clean(type: Delete) {
//    delete rootProject.buildDir
//}
//// Added these from yap Legacy
//subprojects {
//    if (project.name == 'app') {
//        apply plugin: 'com.android.application'
//    } else {
//        apply plugin: 'com.android.library'
//    }
//
//    apply from: '../dependencies/build.gradle'
//}

plugins {
    id(BuildPluginsConfig.androidApplication) apply false
    id(BuildPluginsConfig.androidLibrary) apply false
    kotlin(BuildPluginsConfig.kotlinAndroid) apply false
    kotlin(BuildPluginsConfig.kotlinKapt) apply false
    id(BuildPluginsConfig.kotlinParcelize) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(BuildClassesConfig.ANDROID_GRADLE_PLUGIN)
        classpath(BuildClassesConfig.KOTLIN_GRADLE_PLUGIN)
        classpath(BuildClassesConfig.NAVIGATION_SAFE_ARGS)
        classpath(BuildClassesConfig.HILT_GRADLE_PLUGIN)
        classpath(BuildClassesConfig.FIREBASE_CRASHLYTICS_GRADLE)
        classpath(BuildClassesConfig.GOOGLE_SERVICES)
        classpath(BuildClassesConfig.MAVEN_GRAGLE)
        classpath(BuildClassesConfig.IO_FEBRIC)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.leanplum.com/") }
        maven { url = uri("https://dl.bintray.com/drummer-aidan/maven") }
        maven { url = uri("http://pay.cards/maven") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}