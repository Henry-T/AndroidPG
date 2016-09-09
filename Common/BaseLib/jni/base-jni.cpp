/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <android/log.h>

#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include <jni.h>
#include <netdb.h>
#include <set>
#include <ares.h>
#include <ares_dns.h>
using namespace std;

#define  LOG_TAG    "base-jni.cpp"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

typedef unsigned char byte;


JavaVM* g_jvm = NULL;
JNIEnv* g_jEnv = NULL;
jobject g_jObj;
jmethodID g_jOnResolvedCallbackID;

std::string HexDump(std::vector<byte> data) {
  std::stringstream ss;
  for (size_t ii = 0; ii < data.size();  ii++) {
    char buffer[2 + 1];
    sprintf(buffer, "%02x", data[ii]);
    ss << buffer;
  }
  return ss.str();
}

std::string HexDump(const byte *data, int len) {
  return HexDump(std::vector<byte>(data, data + len));
}

std::string HexDump(const char *data, int len) {
  return HexDump(reinterpret_cast<const byte*>(data), len);
}


static void
wait_ares(ares_channel channel)
{
    for(;;){
        struct timeval *tvp, tv;
        fd_set read_fds, write_fds;
        int nfds;

        FD_ZERO(&read_fds);
        FD_ZERO(&write_fds);
        nfds = ares_fds(channel, &read_fds, &write_fds);
        if(nfds == 0){
            break;
        }
        tvp = ares_timeout(channel, NULL, &tv);
        select(nfds, &read_fds, &write_fds, NULL, tvp);
        ares_process(channel, &read_fds, &write_fds);
    }
}

string AddressToString(const void* vaddr, int len) {
  const byte* addr = reinterpret_cast<const byte*>(vaddr);
  ostringstream ss;
  if (len == 4) {
    char buffer[4*4 + 3 + 1];
    sprintf(buffer, "%u.%u.%u.%u",
            (unsigned char)addr[0],
            (unsigned char)addr[1],
            (unsigned char)addr[2],
            (unsigned char)addr[3]);
    ss << buffer;
  } else if (len == 16) {
    for (int ii = 0; ii < 16;  ii+=2) {
      if (ii > 0) ss << ':';
      char buffer[4 + 1];
      sprintf(buffer, "%02x%02x", (unsigned char)addr[ii], (unsigned char)addr[ii+1]);
      ss << buffer;
    }
  } else {
    ss << "!" << HexDump(addr, len) << "!";
  }
  return ss.str();
}

void ResolvedCallback(void *data, int status, int timeouts, struct hostent *hostent) {
  LOGD("ares ResolvedCallback called");

  JNIEnv * g_env;
  // todo note make sure JNIEnv is attached to current thread
  // http://stackoverflow.com/questions/12900695/how-to-obtain-jni-interface-pointer-jnienv-for-asynchronous-calls
  int getEnvStat = g_jvm->GetEnv((void **)&g_env, JNI_VERSION_1_6);
  if (getEnvStat == JNI_EDETACHED) {
    LOGD("cares GetEnv: not attached");
    if (g_jvm->AttachCurrentThread(&g_env, NULL) != 0) {
      LOGD("cares Failed to attach");
    }
  } else if (getEnvStat == JNI_OK) {
    //
    LOGD("cares GetEnv already attached");
  } else if (getEnvStat == JNI_EVERSION) {
    LOGD("cares GetEnv: version not supported");
  }

  LOGD("cares resolve status: %d", status);

  if (status == ARES_SUCCESS) {
    LOGD("cares ARES_SUCCESS The host lookup completed successfully.");
  } else if (status == ARES_ENOTIMP) {
    LOGD("cares The ares library does not know how to find addresses of type family .");
  } else if (status == ARES_EBADNAME) {
    LOGD("cares The hostname name is composed entirely of numbers and periods, but is not a valid representation of an Internet address.");
  } else if (status == ARES_ENOTFOUND) {
    LOGD("cares The address addr was not found.");
  } else if (status == ARES_ENOMEM) {
    LOGD("cares Memory was exhausted.");
  } else if (status == ARES_ECANCELLED) {
    LOGD("cares The query was cancelled.");
  } else if (status == ARES_EDESTRUCTION) {
    LOGD("cares The name service channel channel is being destroyed; the query will not be completed.");
  }

  ostringstream ostr;
  ostr << status;


  if (hostent->h_addr_list) {
    char** paddr = hostent->h_addr_list;
    while(*paddr != NULL) {
      ostr << ";" << AddressToString(*paddr, hostent->h_length);
      paddr++;
    }
  }

  LOGD("cares result for java: %s", ostr.str().c_str());

  jstring jStrRet = g_jEnv->NewStringUTF(ostr.str().c_str());
  g_jEnv->CallVoidMethod(g_jObj, g_jOnResolvedCallbackID, jStrRet);

  if (g_jEnv->ExceptionCheck()) {
    LOGD("cares ExceptionCheck found");
    g_jEnv->ExceptionDescribe();
  }

  // g_jvm->DetachCurrentThread();
}

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
// todo remove jstring
extern "C" JNIEXPORT jboolean JNICALL Java_com_lolofinil_AndroidPG_Common_BaseLib_util_CaresDnsResolver_caresResolve(JNIEnv* jEnv, jobject jObj, jstring dns, jstring domain)
{
  LOGD("cares native caresResolve called");
  g_jEnv = jEnv;
  g_jObj = jObj;
  int succ = jEnv->GetJavaVM(&g_jvm);
  if (succ == 0) {
    LOGD("cares get jvm success");
  } else {
    LOGD("cares get jvm failed");
  }

  jclass jObjClass = jEnv->GetObjectClass(jObj);
  if (jObjClass == NULL) {
    LOGD("cares failed to find class");
    return false;
  }

  g_jOnResolvedCallbackID = jEnv->GetMethodID(jObjClass, "cares_onResolved", "(Ljava/lang/String;)V");
  if (g_jOnResolvedCallbackID == NULL) {
    LOGD("cares unable to get method");
    return false;
  }

  // todo note cares's name means domain
  const char *dnsStr = jEnv->GetStringUTFChars(dns, 0);
  const char *donmainStr = jEnv->GetStringUTFChars(domain, 0);

  // == c-ares resolve
  ares_options opts;
  opts.nservers = 2;
  opts.servers = (struct in_addr*)malloc(opts.nservers*sizeof(struct in_addr));
  opts.servers[0].s_addr = htonl(0x72727272); // todo use dnsStr
  int optmask = ARES_OPT_SERVERS;
  ares_channel channel = NULL;
  int code = ares_init(&channel);
  if (code == ARES_SUCCESS) {
    LOGD("cares init channel success");
  } else {
    LOGD("cares init channel failed");
  }
  code = ares_init_options(&channel, &opts, optmask);
  if (code == ARES_SUCCESS) {
    LOGD("cares init options success");
  } else {
    LOGD("cares init options failed");
  }
  ares_gethostbyname(channel, donmainStr, AF_INET, ResolvedCallback, NULL);
  LOGD("cares ares_gethostbyname called");
  wait_ares(channel);
  LOGD("cares process done");
  ares_destroy(channel);
  ares_library_cleanup();
  // if (code) {
  //   LOGD("cares gethost call success");
  // } else {
  //   LOGD("cares gethost call failed");
  // }
  // == 

  return true;
}

extern "C" JNIEXPORT jstring JNICALL Java_com_lolofinil_AndroidPG_Common_BaseLib_util_CaresDnsResolver_stringFromJNI(JNIEnv* env, jobject thiz)
{
#if defined(__arm__)
  #if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
      #define ABI "armeabi-v7a/NEON"
    #else
      #define ABI "armeabi-v7a"
    #endif
  #else
   #define ABI "armeabi"
  #endif
#elif defined(__i386__)
   #define ABI "x86"
#elif defined(__mips__)
   #define ABI "mips"
#else
   #define ABI "unknown"
#endif

    // return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
    return env->NewStringUTF("Hello from JNI !  Compiled with ABI " ABI ".");
}

