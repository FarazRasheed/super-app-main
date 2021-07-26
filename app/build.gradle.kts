plugins {
    id(BuildPluginsConfig.androidApplication)
    kotlin(BuildPluginsConfig.kotlinAndroid)
    kotlin(BuildPluginsConfig.kotlinKapt)
    id(BuildPluginsConfig.kotlinParcelize)
    id(BuildPluginsConfig.androidHilt)
    id(BuildPluginsConfig.navigationSafeArgs)
    id(BuildPluginsConfig.googleServices)
    id(BuildPluginsConfig.crashlytics)

    // Internal Script plugins
    id(ScriptPlugins.variants)
    id(ScriptPlugins.compilation)
}

 {
    compileSdkVersion(AppConfig.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(AppConfig.MIN_SDK_VERSION)
        targetSdkVersion(AppConfig.TARGET_SDK_VERSION)
        multiDexEnabled = true
        applicationId = AppConfig.APP_ID
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME
        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }
    viewBinding {
        android.buildFeatures.viewBinding = true
    }
    buildFeatures {
        this.dataBinding = true

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

//    kapt {
//        arguments {
//            arg("dagger.hilt.shareTestComponents", "true")
//        }
//    }
//  sourceSets {
//    map { it.java.srcDir("src/${it.name}/kotlin") }
//
//    //TODO: Remove this when migrating the DI framework
//    getByName("main") { java.srcDir("$buildDir/generated/source/kapt/main") }
//  }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":core"))
    implementation(project(":networking"))
    implementation(project(":yap-ui"))
    implementation(project(":identityscanner_v2"))
    implementation(project(mapOf("path" to ":localization")))
    implementation(project(mapOf("path" to ":utils")))
    implementation(project(mapOf("path" to ":permissionx")))
    implementation(DependenciesManager.kotlinImplementation)
    implementation(DependenciesManager.lifeCycleKtxImplementation)
    implementation(DependenciesManager.androidxImplementation)
    implementation(DependenciesManager.navigationImplementation)
    implementation(DependenciesManager.thirdPartyImplementation)
    implementation(DependenciesManager.networkImplementation)
    implementation(DependenciesManager.hiltImplementation)
    implementation(DependenciesManager.fireBaseImplementation)
    implementation(ThirdPartyDependencies.IMAGE_COMPRESSION)
    implementation(ThirdPartyDependencies.IMAGE_PICKER)
    implementation(ThirdPartyDependencies.BOTTOM_SHEETS_CORE)
    implementation(ThirdPartyDependencies.BOTTOM_SHEETS_KTX)
    implementation(ThirdPartyDependencies.BOTTOM_SHEETS)
    implementation(ThirdPartyDependencies.EXPANDABLE_LAYOUT)
    implementation(ThirdPartyDependencies.TIMBER)
    kapt(DependenciesManager.hiltKapt)
    kapt(DependenciesManager.networkKapt)
    testImplementation(DependenciesManager.testingImplementation)
    androidTestImplementation(DependenciesManager.androidTestImplementation)
}
