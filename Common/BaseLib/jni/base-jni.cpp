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
#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include <jni.h>
#include <netdb.h>
#include "ares.h"
#include "ares_dns.h"
using namespace std;

typedef unsigned char byte;

JNIEnv* g_jEnv;
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
  std::string ret = "";
  ret += status;
  if (hostent->h_addr_list) {
    char** paddr = hostent->h_addr_list;
    while(*paddr != NULL) {
      std::string addr = AddressToString(*paddr, hostent->h_length);
      ret += ";"+addr;
    }
  }

  jstring jStrRet = g_jEnv->NewStringUTF(ret.c_str());
  g_jEnv->CallVoidMethod(g_jObj, g_jOnResolvedCallbackID, jStrRet);
}

extern "C" {
  /* This is a trivial JNI example where we use a native method
   * to return a new VM String. See the corresponding Java source
   * file located at:
   *
   *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
   */
  // todo remove jstring
  jboolean  // jboolean
  Java_com_lolofinil_AndroidPG_Common_BaseLib_util_CaresDnsResolver_caresResolve(JNIEnv* jEnv, jobject jObj, jstring dns, jstring domain)
  {
    g_jEnv = jEnv;
    g_jObj = jObj;

    jclass jObjClass = jEnv->GetObjectClass(jObj);
    if (jObjClass == NULL) {
      std::cout<<"failed to find class"<<std::endl;
      return false;
    }

    g_jOnResolvedCallbackID = jEnv->GetMethodID(jObjClass, "cares_onResolved", "(Ljava/lang/String;)V");
    if (g_jOnResolvedCallbackID == NULL) {
      std::cout<<"unable to get method"<<std::endl;
      return false;
    }

    // todo note ares's name means domain
    const char *dnsStr = jEnv->GetStringUTFChars(dns, 0);
    const char *donmainStr = jEnv->GetStringUTFChars(domain, 0);
    // const char *dnsStr = jEnv->GetStringUTFChars(jEnv, dns, 0);
    // const char *donmainStr = jEnv->GetStringUTFChars(jEnv, domain, 0);

    ares_options opts;
    opts.nservers = 2;
    opts.servers = (struct in_addr*)malloc(opts.nservers*sizeof(struct in_addr));
    opts.servers[0].s_addr = htonl(0x72727272); // todo use dnsStr

    int optmask = ARES_OPT_SERVERS;

    ares_channel channel = NULL;
    ares_init_options(&channel, &opts, optmask);

    ares_init(&channel);


    ares_gethostbyname(channel, donmainStr, AF_INET, ResolvedCallback, NULL);
    return true;
    // return jEnv->NewStringUTF("test");
  }

  jstring
  Java_com_lolofinil_AndroidPG_Common_BaseLib_util_CaresDnsResolver_caresResolve__Ljava_lang_String_2Ljava_lang_String_2( JNIEnv* env,
                                                    jobject thiz )
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
      return env->NewStringUTF("Hello from JNI !  Compiled with ABI " ABI ".");
      // return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
  }

  JNIEXPORT jstring JNICALL Java_com_lolofinil_AndroidPG_Common_BaseLib_util_CaresDnsResolver_stringFromJNI(JNIEnv* env, jobject thiz)
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

}
