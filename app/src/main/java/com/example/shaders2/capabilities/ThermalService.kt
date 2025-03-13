package com.example.shaders2.capabilities

import android.content.Context
import android.os.PowerManager
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow


class ThermalService(
    context: Context
) {

    private val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    val thermalFlow = flow {
        while (true) {
            emit(powerManager.currentThermalStatus)
            delay(1000)
        }
    }

    fun getCurrent(): Int {
        Log.i("THERM", powerManager.currentThermalStatus.toString())
        Log.i("THERM", powerManager.getThermalHeadroom(10).toString())

        powerManager.addThermalStatusListener { status ->
            // THERMAL_STATUS_NONE, THERMAL_STATUS_LIGHT, ...
        }
        return powerManager.currentThermalStatus
    }

    fun foo(context: Context) {

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        // float value greater than 0.0 depending on current temperature,
        // where 1.0 corresponds to THERMAL_STATUS_SEVERE
        val thermalHeadroom = powerManager.getThermalHeadroom(10)

        // one of THERMAL_STATUS_NONE, THERMAL_STATUS_LIGHT, THERMAL_STATUS_MODERATE, ...
        val thermalStatus = powerManager.currentThermalStatus
        powerManager.addThermalStatusListener { status ->
            // thermal status has been changed, take measures
        }

    }
}