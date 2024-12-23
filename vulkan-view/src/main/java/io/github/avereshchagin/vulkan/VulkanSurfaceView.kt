package io.github.avereshchagin.vulkan

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

open class VulkanSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, defStyleRes: Int = 0
): SurfaceView(
    context, attrs, defStyle, defStyleRes
), SurfaceHolder.Callback2 {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val coroutineScope = CoroutineScope(Dispatchers.IO.limitedParallelism(1) + SupervisorJob())

    private val bridge = VulkanJniBridge()

    init {
        alpha = 1f
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.i("Vulkan", "Surface init")
        bridge.nativeInitVulkan(context.assets, holder.surface)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.i("Vulkan", "Surface resize $width $height")
        bridge.nativeResize(width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.i("Vulkan", "Surface destroyed")
        coroutineScope.coroutineContext.cancelChildren()
        bridge.nativeShutdownVulkan()
    }

    override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
        bridge.nativeDraw()
    }

    override fun surfaceRedrawNeededAsync(holder: SurfaceHolder, drawingFinished: Runnable) {
        coroutineScope.launch {
            val start = SystemClock.elapsedRealtime()
            bridge.nativeDraw()
            Log.i("Vulkan", "Draw time ${SystemClock.elapsedRealtime() - start} ms")
            drawingFinished.run()
        }
    }

    fun redraw() {
        bridge.nativeDraw()
    }

    fun setColor(red: Float, green: Float, blue: Float) {
        bridge.setColor(red, green, blue)
    }

    fun setBgColor(red: Float, green: Float, blue: Float) {
        bridge.setBgColor(red, green, blue)
    }
}