
#include <jni.h>
#include <android/asset_manager_jni.h>
#include <android/native_window_jni.h>

#include "vulkan_app.h"

static VulkanApp app;

extern "C" {

JNIEXPORT void JNICALL
Java_io_github_avereshchagin_vulkan_VulkanJniBridge_nativeInitVulkan(
        JNIEnv *env,
        jobject obj,
        jobject jAssetMgr,
        jobject surface
) {
    auto javaAssetManager = env->NewGlobalRef(jAssetMgr);
    auto assetManager = AAssetManager_fromJava(env, jAssetMgr);
    auto window = ANativeWindow_fromSurface(env, surface);

    LOGI("init vulkan");
    app.reset(window, assetManager);
    if (!app.initialized) {
        app.initVulkan();
    }
}

JNIEXPORT void JNICALL
Java_io_github_avereshchagin_vulkan_VulkanJniBridge_nativeShutdownVulkan(
        JNIEnv *env,
        jobject obj
) {
    app.cleanup();
}

JNIEXPORT void JNICALL
Java_io_github_avereshchagin_vulkan_VulkanJniBridge_nativeResize(
        JNIEnv *env,
        jobject obj,
        jint width,
        jint height
) {
    app.resize(width, height);
}

JNIEXPORT void JNICALL
Java_io_github_avereshchagin_vulkan_VulkanJniBridge_nativeDraw(
        JNIEnv *env,
        jobject obj
) {
    LOGI("render");
    app.render();
}

JNIEXPORT void JNICALL
Java_io_github_avereshchagin_vulkan_VulkanJniBridge_setColor(
        JNIEnv *env,
        jobject obj,
        jfloat r,
        jfloat g,
        jfloat b
) {
    app.setColor(r, g, b);
}

JNIEXPORT void JNICALL
Java_io_github_avereshchagin_vulkan_VulkanJniBridge_setBgColor(
        JNIEnv *env,
        jobject obj,
        jfloat r,
        jfloat g,
        jfloat b
) {
    app.setBgColor(r, g, b);
}
}
