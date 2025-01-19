package io.github.avereshchagin.vulkan

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class VulkanSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, defStyleRes: Int = 0
): SurfaceView(
    context, attrs, defStyle, defStyleRes
), SurfaceHolder.Callback2 {

    private val bridge = VulkanJniBridge()

    init {
        alpha = 1f
        holder.addCallback(this)
        Log.i("Vulkan", "Surface init")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        bridge.nativeInitVulkan(context.assets, holder.surface)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.i("Vulkan", "Surface resize $width $height")
        bridge.nativeResize(width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.i("Vulkan", "Surface destroyed")
    }

    override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
        bridge.nativeDraw()
    }
}