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
#include <jni.h>
#include <ares.h>
using namespace std;

typedef unsigned char byte;

std::string AddressToString(const void* vaddr, int len) {
  const byte* addr = reinterpret_cast<const byte*>(vaddr);
  std::stringstream ss;
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
  std:string ret = "";
  ret += status;
  if (hostent->h_addr_list) {
    char** paddr = hostent->h_addr_list;
    while(*paddr != NULL) {
      std::string addr = AddressToString(*paddr, hostent->h_length);
      ret += ";"+addr;
    }
  }

  g_jEnv->CallVoidMethod(g_jObj, g_jOnResolvedCallbackID, ret);
}

JNIEnv* g_jEnv;
jobject g_jObj;
jmethidID g_jOnResolvedCallbackID;


/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
// todo remove jstring
JNIEXPORT jboolean com_lolofinil_AndroidPG_Common_BaseLib_util_ares_resolve(JNIEnv* jEnv, jobject jObj, jstring dns, jstring domain)
{
  g_jEnv = jEnv;
  g_jObj = jObj;

  jclass jObjClass = jEnv->GetObjectClass(jObj);
  if (jObjClass == NULL) {
    std::cout<<"failed to find class"<<std::endl;
    return false;
  }

  g_jOnResolvedCallbackID = jEnv->GetMethodID(jObjClass, "cares_onResolved", "(I)V");
  if (g_jOnResolvedCallbackID == NULL) {
    std::cout<<"unable to get method"<<std:endl;
    return false;
  }

  // todo note ares's name means domain
  ares_gethostbyname(dns, domain+".", AF_INET, ResolvedCallback, NULL);
  return true;
}
