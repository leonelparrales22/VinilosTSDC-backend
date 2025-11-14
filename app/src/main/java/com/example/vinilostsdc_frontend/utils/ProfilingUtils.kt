package com.example.vinilostsdc_frontend.utils

import android.content.Context
import android.os.BatteryManager
import android.os.Debug
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ProfilingUtils {

    private const val TAG = "ProfilingUtils"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    data class ResourceMetrics(
        val timestamp: String,
        val userStory: String,
        val deviceModel: String,
        val androidVersion: String,
        val initialMemory: Long,
        val finalMemory: Long,
        val memoryUsed: Long,
        val initialBattery: Int,
        val finalBattery: Int,
        val batteryUsed: Int,
        val timeTakenMs: Long
    )

    private fun getDeviceInfo(context: Context): Pair<String, String> {
        val deviceModel = android.os.Build.MODEL
        val androidVersion = android.os.Build.VERSION.RELEASE
        return Pair(deviceModel, androidVersion)
    }

    private fun getMemoryUsage(): Long {
        val runtime = Runtime.getRuntime()
        return runtime.totalMemory() - runtime.freeMemory()
    }

    private fun getBatteryLevel(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    suspend fun <T> profileOperation(
        context: Context,
        userStory: String,
        operation: suspend () -> T
    ): T = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        val initialMemory = getMemoryUsage()
        val initialBattery = getBatteryLevel(context)
        val (deviceModel, androidVersion) = getDeviceInfo(context)

        Log.d(TAG, "Starting profiling for $userStory on $deviceModel")

        val result = operation()

        val endTime = System.currentTimeMillis()
        val finalMemory = getMemoryUsage()
        val finalBattery = getBatteryLevel(context)
        val timeTaken = endTime - startTime
        val memoryUsed = finalMemory - initialMemory
        val batteryUsed = initialBattery - finalBattery

        val metrics = ResourceMetrics(
            timestamp = dateFormat.format(Date()),
            userStory = userStory,
            deviceModel = deviceModel,
            androidVersion = androidVersion,
            initialMemory = initialMemory,
            finalMemory = finalMemory,
            memoryUsed = memoryUsed,
            initialBattery = initialBattery,
            finalBattery = finalBattery,
            batteryUsed = batteryUsed,
            timeTakenMs = timeTaken
        )

        logMetrics(metrics)
        saveMetricsToFile(context, metrics)

        Log.d(TAG, "Profiling completed for $userStory: time=${timeTaken}ms, memory=${memoryUsed}bytes, battery=${batteryUsed}%")

        result
    }

    private fun logMetrics(metrics: ResourceMetrics) {
        Log.i(TAG, """
            Profiling Metrics:
            User Story: ${metrics.userStory}
            Device: ${metrics.deviceModel} (Android ${metrics.androidVersion})
            Time: ${metrics.timeTakenMs}ms
            Memory Used: ${metrics.memoryUsed} bytes
            Battery Used: ${metrics.batteryUsed}%
            Timestamp: ${metrics.timestamp}
        """.trimIndent())
    }

    private fun saveMetricsToFile(context: Context, metrics: ResourceMetrics) {
        try {
            val file = File(context.getExternalFilesDir(null), "profiling_metrics.csv")
            val fileExists = file.exists()

            FileWriter(file, true).use { writer ->
                if (!fileExists) {
                    writer.write("Timestamp,UserStory,DeviceModel,AndroidVersion,InitialMemory,FinalMemory,MemoryUsed,InitialBattery,FinalBattery,BatteryUsed,TimeTakenMs\n")
                }
                writer.write("${metrics.timestamp},${metrics.userStory},${metrics.deviceModel},${metrics.androidVersion},${metrics.initialMemory},${metrics.finalMemory},${metrics.memoryUsed},${metrics.initialBattery},${metrics.finalBattery},${metrics.batteryUsed},${metrics.timeTakenMs}\n")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save metrics to file", e)
        }
    }
}