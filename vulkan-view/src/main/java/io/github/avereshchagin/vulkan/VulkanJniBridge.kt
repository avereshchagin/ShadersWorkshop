package io.github.avereshchagin.vulkan

import android.content.res.AssetManager
import android.view.Surface

class VulkanJniBridge {

    private var nativePointer: Long = 0

    external fun nativeInitVulkan(assetManager: AssetManager, surface: Surface)

    external fun nativeShutdownVulkan()

    external fun nativeResize(width: Int, height: Int)

    external fun nativeDraw()

    companion object {
        init {
            System.loadLibrary("vulkan_view")
        }
    }
}
