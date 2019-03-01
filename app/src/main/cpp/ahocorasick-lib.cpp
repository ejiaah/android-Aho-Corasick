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

std::wstring convertJstringToWstring(JNIEnv *env, jstring string) {
    std::wstring value;
    if (string == NULL) {
        return value; // empty string
    }
    const jchar *raw = env->GetStringChars(string, NULL);
    if (raw != NULL) {
        jsize len = env->GetStringLength(string);
        value.assign(raw, raw + len);
        env->ReleaseStringChars(string, raw);
    }
    return value;
}

jstring convertWstringToJstring(JNIEnv *env, std::wstring string) {
    size_t len = string.length();
    jchar *value_char = (jchar *) malloc((len + 1) * sizeof(jchar));
    for (int i = 0; i < len; i++)
        value_char[i] = string[i];
    value_char[len] = 0;

    jstring value = env->NewString(value_char, len);
    delete[] value_char;
    value_char = NULL;
    return value;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_setCaseInsensitive(
        JNIEnv *env,
        jobject, /* this */
        jboolean isOn) {
    trie.set_case_insensitive(isOn);
}

extern "C" JNIEXPORT void JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_setRemoveOverlaps(
        JNIEnv *env,
        jobject, /* this */
        jboolean isOn) {
    trie.set_remove_overlaps(isOn);
}

extern "C" JNIEXPORT void JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_setOnlyWholeWords(
        JNIEnv *env,
        jobject, /* this */
        jboolean isOn) {
    trie.set_only_whole_words(isOn);
}

extern "C" JNIEXPORT void JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_insert(
        JNIEnv *env,
        jobject, /* this */
        jstring keyword) {

    if (keyword == NULL) {
        return;
    }

    std::wstring wtext = convertJstringToWstring(env, keyword);
    trie.insert(wtext);
    LOGD("insert: %ls", wtext.c_str());
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_ejiaah_aho_1corasick_MainActivity_parseText(
        JNIEnv *env,
        jobject, /* this */
        jstring text) {
    if (text == NULL) {
        return NULL;
    }

    std::wstring wtext = convertJstringToWstring(env, text);
    auto emits = trie.parse_text(wtext);

    jobjectArray results = (jobjectArray) env->NewObjectArray(emits.size(),
                                                              env->FindClass("java/lang/String"),
                                                              env->NewStringUTF(""));
    int i = 0;
    for (const auto &emit : emits) {
        std::wstring match = emit.get_keyword();
        LOGD("emit: %ls", match.c_str());
        env->SetObjectArrayElement(results, i, convertWstringToJstring(env, match));
        i++;
    }

    return results;
}


