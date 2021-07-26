
    object AndroidXVersions {
        const val APPCOMPAT = "1.1.0-beta01"
        const val FRAGMENT_KTX = "2.1.0-alpha05"
        const val ACTIVITY_KTX = "1.2.3"
        const val CONSTRAINT_LAYOUT = "1.1.3"
        const val RECYCLERVIEW = "1.2.1"
        const val CARD_VIEW = "1.0.0"
        const val BIOMETRIC = "1.1.0"
        const val MATERIAL = "1.2.0-alpha01"
        const val VIEWPAGER2 = "1.0.0"
        const val VECTOR_DRAWABLE = "1.1.0"
        const val PAGING = "3.0.0"
        const val SECURITY_CRYPTO = "1.0.0"
    }

    object LifeCycle_KTX {
        const val CORE_KTX = "1.0.2"
        const val Collection_KTX = "1.1.0"
        const val LIFECYCLE = "2.3.1"
        const val LIFECYCL_EEXTENSIONS = "2.1.0"
    }

    object NetworkVersions {
        const val GLIDE = "4.12.0"
        const val RETROFIT = "2.6.0"
        const val OKHTTPGSON = "2.5.0"
        const val OKHTTP = "3.10.0"
        const val GSON = "2.8.5"
    }

    object FireBaseVersions {
        //firebase
        const val AUTH = "19.0.0"
        const val AUTH_PHONE = "17.5.1"
        const val ANALYTICS = "18.0.0"
        const val MESSAGING_FCM = "22.0.0"
        const val PLAY_SERVICES_PLACES = "17.0.0"
        const val PLAY_LIB_PLACE = "2.4.0"
        const val PLAY_SERVICES_MAP = "17.0.1"
        const val FIREBASE_CRASHLYTICS = "18.0.1"
        const val FIREBASE_CRASH = "16.2.1"

        const val crashlytics = "17.2.1"
        const val crashlyticsSdk = "2.10.1"
        const val crashlyticsClassPath = "2.2.1"
    }

    object KotlinVersions {
        const val STANDARD_LIBRARY = "1.5.10"
        const val COROUTINE_VERSION = "1.4.1"
    }

    object ThirdPartyVersions {
        const val SDP = "1.0.6"
        const val DATE_PICKET = "4.2.3"
        const val LIB_PHONE = "8.12.10"
        const val IMAGE_COMPRESSION = "3.3.0"
        const val INLINE_ACTIVITY_RESULT = "1.0.4"
    }

    object SDKVersions {
        const val MIN_SDK_VERSION = 23
        const val COMPILE_SDK_VERSION = 30
        const val TARGET_SDK_VERSION = COMPILE_SDK_VERSION
        const val BUILD_TOOLS_VERSION = "30.0.3"
    }

    object BuildPluginsVersions {
        const val KOTLIN = KotlinVersions.STANDARD_LIBRARY
        const val BUILDGRADLE = "4.2.1"
        const val HILT = "2.36"
        const val NAVIGATION = "2.3.5"
    }

    object HiltDaggerVersion {
        const val HILT_DI = BuildPluginsVersions.HILT
        const val HILT_VM = "1.0.0-alpha03"
    }

    object TestDependenciesVersions {
        const val JUNIT4 = "4.13.1"
        const val MOCKK = "1.10.0"
        const val ROBOLECTRIC = "4.4"
        const val KLUENT = "1.14"
        const val TESTRUNNER = "1.1.0"
        const val ESPRESSOCORE = "3.2.0"
        const val ESPRESSOINTENTS = "3.1.0"
        const val TESTEXTENSIONS = "1.1.1"
        const val TESTRULES = "1.1.0"
        const val HILTTESTING = BuildPluginsVersions.HILT
    }


    object LibrariesVersions{

        const val daggerVersion = "2.16"
    }
