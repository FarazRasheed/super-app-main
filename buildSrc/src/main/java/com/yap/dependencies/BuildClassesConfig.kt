object BuildClassesConfig {
    const val ANDROID_GRADLE_PLUGIN =
        "com.android.tools.build:gradle:${BuildPluginsVersions.BUILDGRADLE}"
    const val KOTLIN_GRADLE_PLUGIN =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildPluginsVersions.KOTLIN}"
    const val HILT_GRADLE_PLUGIN =
        "com.google.dagger:hilt-android-gradle-plugin:${BuildPluginsVersions.HILT}"
    const val NAVIGATION_SAFE_ARGS =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${BuildPluginsVersions.NAVIGATION}"

    const val FIREBASE_CRASHLYTICS_GRADLE = "com.google.firebase:firebase-crashlytics-gradle:2.3.0"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:4.3.4"

    const val IO_FEBRIC = "io.fabric.tools:gradle:1.31.2"
    const val MAVEN_GRAGLE = "com.github.dcendents:android-maven-gradle-plugin:2.0"

    val classPaths = arrayListOf<String>().apply {
        add(ANDROID_GRADLE_PLUGIN)
        add(KOTLIN_GRADLE_PLUGIN)
        add(HILT_GRADLE_PLUGIN)
        add(NAVIGATION_SAFE_ARGS)
    }
}