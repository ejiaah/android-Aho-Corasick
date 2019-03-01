#include <jni.h>
#include <string>
#include <android/log.h>
#include "aho_corasick.hpp"

#define MODULE_NAME  "ahocorasick-lib.cpp"
#define LOGV(...) { __android_log_print(ANDROID_LOG_VERBOSE, MODULE_NAME, __VA_ARGS__); }
#define LOGD(...) { __android_log_print(ANDROID_LOG_DEBUG, MODULE_NAME, __VA_ARGS__); }
#define LOGI(...) { __android_log_print(ANDROID_LOG_INFO, MODULE_NAME, __VA_ARGS__); }
#define LOGW(...) { __android_log_print(ANDROID_LOG_WARN,MODULE_NAME, __VA_ARGS__); }
#define LOGE(...) { __android_log_print(ANDROID_LOG_ERROR,MODULE_NAME, __VA_ARGS__); }
#define LOGF(...) { __android_log_print(ANDROID_LOG_FATAL,MODULE_NAME, __VA_ARGS__); }

aho_corasick::wtrie trie;
std::wstring converted_string;

extern "C" JNIEXPORT jstring JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_setCaseInsensitive(
        JNIEnv* env,
        jboolean isOn) {
    trie.set_case_insensitive(isOn);
}

extern "C" JNIEXPORT void JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_setRemoveOverlaps(
        JNIEnv* env,
        jboolean isOn) {
    trie.set_remove_overlaps(isOn);
}

extern "C" JNIEXPORT void JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_setOnlyWholeWords(
        JNIEnv* env,
        jboolean isOn) {
    trie.set_only_whole_words(isOn);
}