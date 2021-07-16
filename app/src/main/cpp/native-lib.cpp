//
// Created by Faheem Riaz on 15/05/2020.
//
#include <jni.h>
#include <string>
#include "base64.h"
#include <chrono>

extern "C" JNIEXPORT jobject JNICALL
Java_co_yap_app_MainAAPApplication_signatureKeysFromJNI(JNIEnv *env, jobject /*this*/,
                                                    jstring javaString, jstring flavour,
                                                    jstring buildVariant, jstring applicationId,
                                                    jstring versionName, jstring versionCode) {
    const char *nativeString = env->GetStringUTFChars(javaString, 0);
    jclass appSignature = env->FindClass(nativeString);
    jmethodID constructor = env->GetMethodID(appSignature, "<init>",
                                             "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;"
                                             "Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;"
                                             "Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;"
                                             "Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

    std::string sha1Encoded;
    std::string md5Encoded;
    std::string sha256Encoded;

    std::string api_endpoint;
    std::string leanPlumSecretKey;
    std::string leanPlumKey;
    std::string adjustAppToken;

    std::string sslPin1;
    std::string sslPin2;
    std::string sslPin3;
    std::string sslHost;

#ifdef LIVE
    api_endpoint = "https://ae-prod.yap.com/";
    adjustAppToken = "xty7lf6skgsg";
    sslPin1 = "sha256/SK10shgwb9jAeBvxJXrkBmjL2joCFoSq2Sp1tGyOcQk=";
    sslPin2 = "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8=";
    sslPin3 = "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA=";
    //VjLZe/p3W/PJnd6lL8JVNBCGQBZynFLdZSTIqcO0SJ8=
    sslHost = "*.yap.com";

    sha1Encoded = "ODU6OUY6NjM6N0M6NjI6N0I6Qjc6N0E6MDg6RTQ6OEI6MDY6OUU6M0U6MkQ6RTU6MEQ6OEM6Mjg6MjU=";
    md5Encoded = "MDg6NzM6ODQ6RTI6NEM6NTc6RTU6MUU6OEY6ODU6RTM6OTg6MUM6NDM6Qjg6NEE=";
    sha256Encoded = "ODY6QTE6MzQ6NEU6RkM6OTQ6M0I6NzA6Mjk6MjE6OUU6M0I6NzA6MzM6NDI6RUM6M0M6NjI6M0E6MkI6MEU6N0M6QkM6MDc6RTU6N0Q6M0M6Mjk6RTg6MkE6Q0Y6NTM=";

#endif
#ifdef Preprod
    api_endpoint = "https://ae-preprod.yap.com/";
    adjustAppToken = "uv1oiis7wni8";
    sslPin1 = "sha256/SK10shgwb9jAeBvxJXrkBmjL2joCFoSq2Sp1tGyOcQk=";
    sslPin2 = "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8=";
    sslPin3 = "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA=";
    //VjLZe/p3W/PJnd6lL8JVNBCGQBZynFLdZSTIqcO0SJ8=
    sslHost = "*.yap.com";

    sha1Encoded = "ODU6OUY6NjM6N0M6NjI6N0I6Qjc6N0E6MDg6RTQ6OEI6MDY6OUU6M0U6MkQ6RTU6MEQ6OEM6Mjg6MjU=";
    md5Encoded = "MDg6NzM6ODQ6RTI6NEM6NTc6RTU6MUU6OEY6ODU6RTM6OTg6MUM6NDM6Qjg6NEE=";
    sha256Encoded = "ODY6QTE6MzQ6NEU6RkM6OTQ6M0I6NzA6Mjk6MjE6OUU6M0I6NzA6MzM6NDI6RUM6M0M6NjI6M0E6MkI6MEU6N0M6QkM6MDc6RTU6N0Q6M0M6Mjk6RTg6MkE6Q0Y6NTM=";

#endif
#ifdef STG
    api_endpoint = "https://stg.yap.co/";
    adjustAppToken = "am0wjeshw5xc";
    sslPin1 = "sha256/ZrRL6wSXl/4lm1KItkcZyh56BGOoxMWUDJr7YVqE4no=";
    sslPin2 = "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8=";
    sslPin3 = "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA=";
    sslHost = "*.yap.co";

    sha1Encoded = "REI6QTg6REE6OTg6RUY6ODA6QkY6ODQ6MDQ6RDE6NzM6Rjg6QzE6RjE6QzA6MTU6NTk6MjA6MTY6RDI=";
    md5Encoded = "MjU6ODQ6MUY6RTE6RjE6QTg6QzI6NTg6N0I6QUU6RUE6QjM6NDE6NjU6NzY6RkU=";
    sha256Encoded = "QTQ6QUM6MTQ6RjM6REQ6RDg6NTc6RTk6RkM6QUM6N0M6MDk6NkM6QTQ6MEQ6RUM6QjU6MEU6RTE6OTY6QTI6RjA6Qjc6Q0M6QjA6MEY6MDc6MDA6Qzc6N0M6RjM6Qjg=";

#endif
#ifdef QA
    api_endpoint = "https://qa.yap.co/";
    adjustAppToken = "am0wjeshw5xc";
    sslPin1 = "sha256/e5L5CAoQjV0HFzAnunk1mPHVx1HvPxcfJYI0UtLyBwY=";
    sslPin2 = "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA=";
    sslPin3 = "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=";
    sslHost = "*.yap.co";

    sha1Encoded = "";
    md5Encoded = "";
    sha256Encoded = "";


#endif
#ifdef DEV
    api_endpoint = "https://dev-b.yap.co/";
    adjustAppToken = "am0wjeshw5xc";
    //dummy keys should be update on once dev server has SSL enabled
    sslPin1 = "sha256/e5L5CAoQjV0HFzAnunk1mPHVx1HvPxcfJYI0UtLyBwY=";
    sslPin2 = "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA=";
    sslPin3 = "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=";
    sslHost = "*.yap.co";

    sha1Encoded = "";
    md5Encoded = "";
    sha256Encoded = "";
#endif

    const char *appId = env->GetStringUTFChars(applicationId, 0);
    const char *productFlavour = env->GetStringUTFChars(flavour, 0);
    const char *buildType = env->GetStringUTFChars(buildVariant, 0);
    const char *vName = env->GetStringUTFChars(versionName, 0);
    const char *vCode = env->GetStringUTFChars(versionCode, 0);
    if (strcmp(productFlavour, "live") == 0 && strcmp(buildType, "release") == 0) {
        leanPlumSecretKey = "app_DtOp3ipxDUi9AM7Bg3jv351hZ4DVrLgC9JZX4L46lIc";
        leanPlumKey = "prod_MfjUF6Sh3GuNE2RtQMkXZTeCUSTS3K0v2CLeGCp0gzk";

    } else if (strcmp(productFlavour, "live") == 0 && strcmp(buildType, "debug") == 0) {
        leanPlumSecretKey = "app_DtOp3ipxDUi9AM7Bg3jv351hZ4DVrLgC9JZX4L46lIc";
        leanPlumKey = "dev_RAFVBmDKypdOr3kbd326JUoqGLr8iSvt2Lei4BK48qk";

    }else if (strcmp(productFlavour, "Preprod") == 0 && strcmp(buildType, "release") == 0) {
        leanPlumSecretKey = "app_jvEgXTi9zZUpoFck8XVxVY4zBgAEYZrPVTliIuaO0IQ";
        leanPlumKey = "prod_EjIC6dCuGaGr36p2qRvG3GkRIhuYf9vgBEGjQ3jBqLM";

    } else if (strcmp(productFlavour, "Preprod") == 0 && strcmp(buildType, "debug") == 0) {
        leanPlumSecretKey = "app_jvEgXTi9zZUpoFck8XVxVY4zBgAEYZrPVTliIuaO0IQ";
        leanPlumKey = "dev_HnmEVN0GDZbhInJjmX767e7InveRC23LkSokuLLuA3s";

    }else if (strcmp(productFlavour, "stg") == 0 && strcmp(buildType, "release") == 0) {
        leanPlumSecretKey = "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk";
        leanPlumKey = "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0";

    } else if (strcmp(productFlavour, "stg") == 0 && strcmp(buildType, "debug") == 0) {
        leanPlumSecretKey = "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk";
        leanPlumKey = "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ";

    } else if (strcmp(productFlavour, "qa") == 0 && strcmp(buildType, "release") == 0) {
        leanPlumSecretKey = "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk";
        leanPlumKey = "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0";
    } else if (strcmp(productFlavour, "qa") == 0 && strcmp(buildType, "debug") == 0) {

        leanPlumSecretKey = "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk";
        leanPlumKey = "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ";
    } else if (strcmp(productFlavour, "dev") == 0 && strcmp(buildType, "release") == 0) {

        leanPlumSecretKey = "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk";
        leanPlumKey = "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0";
    } else if (strcmp(productFlavour, "dev") == 0 && strcmp(buildType, "debug") == 0) {

        leanPlumSecretKey = "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk";
        leanPlumKey = "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ";
    } else {

    }

    std::string sha1Decoded = base64_decode((sha1Encoded.c_str()));
    std::string md5Decoded = base64_decode((md5Encoded.c_str()));
    std::string sha256Decoded = base64_decode((sha256Encoded.c_str()));
    //un-comment when we needed to encode a string into base64
    //std::string encoded = base64_encode(reinterpret_cast<const unsigned char *>(sha256.c_str()),
    //sha256.length());

    jobject jObj = env->NewObject(appSignature, constructor, env->NewStringUTF(md5Decoded.c_str()),
                                  env->NewStringUTF(sha1Decoded.c_str()),
                                  env->NewStringUTF(sha256Decoded.c_str()),
                                  env->NewStringUTF(leanPlumSecretKey.c_str()),
                                  env->NewStringUTF(leanPlumKey.c_str()),
                                  env->NewStringUTF(adjustAppToken.c_str()),
                                  env->NewStringUTF(api_endpoint.c_str()),
                                  env->NewStringUTF(buildType),
                                  env->NewStringUTF(productFlavour),
                                  env->NewStringUTF(vName),
                                  env->NewStringUTF(vCode),
                                  env->NewStringUTF(appId),
                                  env->NewStringUTF(sslPin1.c_str()),
                                  env->NewStringUTF(sslPin2.c_str()),
                                  env->NewStringUTF(sslPin3.c_str()),
                                  env->NewStringUTF(sslHost.c_str()));
    return jObj;
}
