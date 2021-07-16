

#include <jni.h>
#include "def.h"
#include "path_helper.h"
#include "unzip_helper.h"
#include "pkcs7_helper.h"

JNIEXPORT jbyteArray JNICALL
Java_co_yap_security_SecurityHelper_bytesFromJNI(JNIEnv *env, jobject ctx, jobject obj) {
    NSV_LOGI("pathHelperGetPath starts\n");
    char *path = pathHelperGetPath();
    NSV_LOGI("pathHelperGetPath finishes\n");

    if (!path) {
        return NULL;
    }
    NSV_LOGI("pathHelperGetPath result[%s]\n", path);
    NSV_LOGI("unzipHelperGetCertificateDetails starts\n");
    size_t len_in = 0;
    size_t len_out = 0;
    unsigned char *content = unzipHelperGetCertificateDetails(path, &len_in);
    NSV_LOGI("unzipHelperGetCertificateDetails finishes\n");
    if (!content) {
        free(path);
        return NULL;
    }
    NSV_LOGI("pkcs7HelperGetSignature starts\n");

    unsigned char *res = pkcs7HelperGetSignature(content, len_in, &len_out);
    NSV_LOGI("pkcs7HelperGetSignature finishes\n");
    jbyteArray jbArray = NULL;
    if (NULL != res || len_out != 0) {
        jbArray = (*env)->NewByteArray(env, len_out);
        (*env)->SetByteArrayRegion(env, jbArray, 0, len_out, (jbyte *) res);
    }
    free(content);
    free(path);
    pkcs7HelperFree();
    return jbArray;
}

