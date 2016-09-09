

#include <jni.h>

#ifndef __BASE_JNI_H_
#define __BASE_JNI_H_


#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_com_lolofinil_AndroidPG_Common_BaseLib_util_CaresDnsResolver_stringFromJNI(JNIEnv *, jobject);

JNIEXPORT jboolean JNICALL Java_com_lolofinil_AndroidPG_Common_BaseLib_util_CaresDnsResolver_caresResolve(JNIEnv*, jobject, jstring, jstring);

#ifdef __cplusplus
}
#endif

#endif