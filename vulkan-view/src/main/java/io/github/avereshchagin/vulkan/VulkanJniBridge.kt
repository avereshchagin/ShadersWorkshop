package io.github.avereshchagin.vulkan

import android.content.res.AssetManager
import android.util.Log
import android.view.Surface

class VulkanJniBridge {

    external fun nativeInitVulkan(assetManager: AssetManager, surface: Surface)

    external fun nativeShutdownVulkan()

    external fun nativeResize(width: Int, height: Int)

    external fun nativeDraw()

    companion object {
        init {
            Log.i("VK", "load library")
            System.loadLibrary("vulkan_view")
        }
    }
}
