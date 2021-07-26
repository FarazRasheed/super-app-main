
    //    Kotlin
    object KotlinDependencies {
        const val KOTLIN_REFLECTION =
            "org.jetbrains.kotlin:kotlin-reflect:${KotlinVersions.STANDARD_LIBRARY}"
        const val KOTLIN_STD_LIB =
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KotlinVersions.STANDARD_LIBRARY}"

        // kotlin coroutine
        const val COROUTINE_CORE =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${KotlinVersions.COROUTINE_VERSION}"
        const val COROUTINE_ANDROID =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${KotlinVersions.COROUTINE_VERSION}"
    }

    // Retrofit2 & Networking
    object NetworkDependencies {

        const val RETROFIT = "com.squareup.retrofit2:retrofit:${NetworkVersions.RETROFIT}"
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${NetworkVersions.OKHTTP}"
        const val OKHTTP_LOGGING_INTERCEPTOR =
            "com.squareup.okhttp3:logging-interceptor:${NetworkVersions.OKHTTP}"
        const val RETROFIT_CONVERTOR_GSON =
            "com.squareup.retrofit2:converter-gson:${NetworkVersions.OKHTTPGSON}"
        const val GSON = "com.google.code.gson:gson:${NetworkVersions.GSON}"

        //Glide Image Loading
        const val GLIDE = "com.github.bumptech.glide:glide:${NetworkVersions.GLIDE}"
        const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${NetworkVersions.GLIDE}"

    }

    //LifeCycle_KTX
    object LifeCycleKtxDependencies {
        const val CORE_KTX = "androidx.core:core-ktx:${LifeCycle_KTX.CORE_KTX}"
        const val LIFECYCL_EEXTENSIONS =
            "androidx.lifecycle:lifecycle-extensions:${LifeCycle_KTX.LIFECYCL_EEXTENSIONS}"
        const val Collection_KTX =
            "androidx.collection:collection-ktx:${LifeCycle_KTX.Collection_KTX}"
        const val VIEW_MODEL_KTX =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${LifeCycle_KTX.LIFECYCLE}"
        const val VIEW_MODEL_SAVE_STATE_KTX =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${LifeCycle_KTX.LIFECYCLE}"
        const val LIVEDATA_KTX =
            "androidx.lifecycle:lifecycle-livedata-ktx:${LifeCycle_KTX.LIFECYCLE}"
        const val LIFECYCLE_RUNTIME =
            "androidx.lifecycle:lifecycle-runtime:${LifeCycle_KTX.LIFECYCLE}"
        const val LIFECYCLE_RUNTIME_KTX =
            "androidx.lifecycle:lifecycle-runtime-ktx:${LifeCycle_KTX.LIFECYCLE}"
        const val LIFECYCLE_COMMON_JAVA =
            "androidx.lifecycle:lifecycle-common-java8:${LifeCycle_KTX.LIFECYCLE}"
        const val REACTIVE_STREAMS =
            "androidx.lifecycle:lifecycle-reactivestreams-ktx:${LifeCycle_KTX.LIFECYCLE}"
    }

    // Androidx Architecture
    // Androidx

    object AndroidxDependencies {
        const val APPCOMPAT = "androidx.appcompat:appcompat:${AndroidXVersions.APPCOMPAT}"
        const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${AndroidXVersions.FRAGMENT_KTX}"
        const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${AndroidXVersions.ACTIVITY_KTX}"
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${AndroidXVersions.CONSTRAINT_LAYOUT}"
        const val CONSTRAINT_LAYOUT_SOLVER =
            "androidx.constraintlayout:constraintlayout-solver:${AndroidXVersions.CONSTRAINT_LAYOUT}"
        const val CARD_VIEW = "androidx.cardview:cardview:${AndroidXVersions.CARD_VIEW}"
        const val RECYCLERVIEW =
            "androidx.recyclerview:recyclerview:${AndroidXVersions.RECYCLERVIEW}"
        const val VECTOR_DRAWABLE =
            "androidx.vectordrawable:vectordrawable:${AndroidXVersions.VECTOR_DRAWABLE}"
        const val VECTOR_DRAWABLE_ANIMATED =
            "androidx.vectordrawable:vectordrawable-animated:${AndroidXVersions.VECTOR_DRAWABLE}"
        const val BIOMETRIC = "androidx.biometric:biometric:${AndroidXVersions.BIOMETRIC}"
        const val VIEWPAGER2 = "androidx.viewpager2:viewpager2:${AndroidXVersions.VIEWPAGER2}"
        const val SECURITY = "androidx.security:security-crypto:${AndroidXVersions.SECURITY_CRYPTO}"
        const val MATERIAL = "com.google.android.material:material:${AndroidXVersions.MATERIAL}"
        const val PAGING_RUNTIME = "androidx.paging:paging-runtime:${AndroidXVersions.PAGING}"
    }

    object NavigationDependencies {
        //    Navigation KTX
        const val NAVIGATION_RUNTIME =
            "androidx.navigation:navigation-runtime-ktx:${BuildPluginsVersions.NAVIGATION}"
        const val NAVIGATION_FRAGMENT_KTX =
            "androidx.navigation:navigation-fragment-ktx:${BuildPluginsVersions.NAVIGATION}"
        const val NAVIGATION_UI_KTX =
            "androidx.navigation:navigation-ui-ktx:${BuildPluginsVersions.NAVIGATION}"
    }

    object FireBaseDependencies {
        //    Google Play Services
        //sms consent
        const val PLAY_SERVICES_AUTH =
            "com.google.android.gms:play-services-auth:${FireBaseVersions.AUTH}"
        const val PLAY_SERVICES_PHONE =
            "com.google.android.gms:play-services-auth-api-phone:${FireBaseVersions.AUTH_PHONE}"

        //Firebase Crashlytics
        const val FIREBASE_CRASHLYTICS =
            "com.google.firebase:firebase-crashlytics:${FireBaseVersions.FIREBASE_CRASHLYTICS}"
        const val FIREBASE_CRASH =
            "com.google.firebase:firebase-crash:${FireBaseVersions.FIREBASE_CRASH}"
        const val FIREBASE_ANALYTICS =
            "com.google.firebase:firebase-analytics-ktx:${FireBaseVersions.ANALYTICS}"
        const val FIREBASE_MESSAGING =
            "com.google.firebase:firebase-messaging-ktx:${FireBaseVersions.MESSAGING_FCM}"

        // maps Location Place
        const val PLAY_SERVICES_LOCATION =
            "com.google.android.gms:play-services-location:${FireBaseVersions.ANALYTICS}"
        const val PLAY_SERVICES_PLACES =
            "com.google.android.gms:play-services-places:${FireBaseVersions.PLAY_SERVICES_PLACES}"
        const val PLAY_LIB_PLACE =
            "com.google.android.libraries.places:places:${FireBaseVersions.PLAY_LIB_PLACE}"
        const val PLAY_SERVICES_MAP =
            "com.google.android.gms:play-services-maps:${FireBaseVersions.PLAY_SERVICES_MAP}"
    }

    //ThirdParty
    object ThirdPartyDependencies {
        const val SDP = "com.intuit.sdp:sdp-android:${ThirdPartyVersions.SDP}"
        const val SSP = "com.intuit.ssp:ssp-android:${ThirdPartyVersions.SDP}"
        const val DATE_PICKET =
            "com.wdullaer:materialdatetimepicker:${ThirdPartyVersions.DATE_PICKET}"
        const val LIB_PHONE =
            "com.googlecode.libphonenumber:libphonenumber:${ThirdPartyVersions.LIB_PHONE}"
        const val IMAGE_COMPRESSION = "id.zelory:compressor:${ThirdPartyVersions.IMAGE_COMPRESSION}"
        const val INLINE_ACTIVITY_RESULT =
            "com.github.florent37:inline-activity-result-kotlin:${ThirdPartyVersions.INLINE_ACTIVITY_RESULT}"

    }

    object RXDependencies {
        // RX Java
        const val RX_ANDROID = "io.reactivex.rxjava2:rxandroid:2.1.1"
        const val RX_JAVA = "io.reactivex.rxjava2:rxjava:2.1.5"
        const val INLINE_ACTIVITY_RESULT =
            "com.github.florent37:inline-activity-result-kotlin:1.0.4"
    }
    object HiltDaggerDependencies {
        // Hilt Dagger DI
        const val DAGGER_HILT = "com.google.dagger:hilt-android:${HiltDaggerVersion.HILT_DI}"
        const val DAGGER_COMPILER = "com.google.dagger:hilt-android-compiler:${HiltDaggerVersion.HILT_DI}"
        const val HILT_VM = "androidx.hilt:hilt-lifecycle-viewmodel:${HiltDaggerVersion.HILT_VM}"
        const val HILT_COMPILER = "androidx.hilt:hilt-compiler:${HiltDaggerVersion.HILT_VM}"
    }

    object TestDependencies{
        const val JUNIT4 = "junit:junit:${TestDependenciesVersions.JUNIT4}"
        const val MOCKK = "io.mockk:mockk:${TestDependenciesVersions.MOCKK}"
        const val ROBOLECTRIC = "org.robolectric:robolectric:${TestDependenciesVersions.ROBOLECTRIC}"
        const val KLUENT = "org.amshove.kluent:kluent:${TestDependenciesVersions.KLUENT}"
        const val TESTRUNNER = "androidx.test:runner:${TestDependenciesVersions.TESTRUNNER}"
        const val TESTRULES = "androidx.test:rules:${TestDependenciesVersions.TESTRULES}"
        const val ESPRESSOCORE = "androidx.test.espresso:espresso-core:${TestDependenciesVersions.ESPRESSOCORE}"
        const val ESPRESSOINTENTS =
            "androidx.test.espresso:espresso-intents:${TestDependenciesVersions.ESPRESSOINTENTS}"
        const val TESTEXTENSIONS = "androidx.test.ext:junit:${TestDependenciesVersions.TESTEXTENSIONS}"
        const val HILTTESTING = "com.google.dagger:hilt-android-testing:${TestDependenciesVersions.HILTTESTING}"
    }

    object Libraries{
        const val DatabindingCompiler = "com.android.databinding:compiler:3.4.1"
        const val viewpagerDots = "com.tbuonomo.andrui:viewpagerdotsindicator:4.1.2"
        const val otpView = "com.github.aabhasr1:OtpView:v1.1.2"
        const val numberFomater = "io.michaelrocks:libphonenumber-android:8.12.10"
        const val recyclerfastscroll = "com.futuremind.recyclerfastscroll:fastscroll:0.2.5"
        const val discretescrollview = "com.yarolegovich:discrete-scrollview:1.4.9"
        const val baloon = "com.github.skydoves:balloon:1.1.4"
        const val quickAction = "me.piruin:quickaction:2.4.3"
        const val rangeSeekbar = "com.github.Jay-Goo:RangeSeekBar:v3.0.0"

        // Dagger
        const val dagger = "com.google.dagger:dagger:${LibrariesVersions.daggerVersion}"
        const val daggerAndroid = "com.google.dagger:dagger-android:${LibrariesVersions.daggerVersion}"
        const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${LibrariesVersions.daggerVersion}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${LibrariesVersions.daggerVersion}"
        const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${LibrariesVersions.daggerVersion}"

        // Animations
        const val androidanimations = "com.daimajia.androidanimations:library:2.3@aar"
        const val androidanimationseasing = "com.daimajia.easing:library:2.0@aar"
        const val expandableLayout = "net.cachapa.expandablelayout:expandablelayout:2.9.2"

        // Live chat
        const val livechat = "com.liveperson.android:lp_messaging_sdk:4.2.1"


        const val easyImage = "com.github.jkwiecien:EasyImage:1.3.1"

        //Leanplum
        const val leanplumFcm = "com.leanplum:leanplum-fcm:5.2.3"
        const val leanplumLocation = "com.leanplum:leanplum-location:5.2.3"

        //Logger
        const val timber = "com.jakewharton.timber:timber:4.7.1"

        //Swipeview actions on list
        const val swipeViewActions = "com.nikhilpanju.recyclerviewenhanced:recyclerviewenhanced:1.1.0"

        //exoplayer
        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.10.3"

        //adjust
        const val adjust = "com.adjust.sdk:adjust-android:4.21.0"
        const val adjustReferrer = "com.android.installreferrer:installreferrer:1.1"
        const val expand = "net.cachapa.expandablelayout:expandablelayout:2.9.2"

        //pdfViewer
        const val pdfViewer = "com.pdfview:pdfview-android:1.0.0"

        // Facebook
        const val facebook = "com.facebook.android:facebook-android-sdk:[5,6)"


        const val tagmanager = "com.google.android.gms:play-services-tagmanager:17.0.0"

        const val legacySupportV13 = "androidx.legacy:legacy-support-v13:1.0.0"
        const val legacySupportV4 = "androidx.legacy:legacy-support-v4:1.0.0"

    }


