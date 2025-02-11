package com.example.rk_shop.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.sqrt

class SensorUtil(context: Context) {

    private val sensorManager: SensorManager = 
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Sensor types
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private val magnetometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val light: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private val proximity: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    fun getAccelerometerData(): Flow<AccelerometerData> = callbackFlow {
        if (accelerometer == null) {
            throw SensorNotFoundException("Accelerometer not available")
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitude = sqrt(x * x + y * y + z * z)
                
                trySend(AccelerometerData(x, y, z, magnitude))
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    fun getGyroscopeData(): Flow<GyroscopeData> = callbackFlow {
        if (gyroscope == null) {
            throw SensorNotFoundException("Gyroscope not available")
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                trySend(GyroscopeData(
                    event.values[0],
                    event.values[1],
                    event.values[2]
                ))
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            gyroscope,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    fun getLightData(): Flow<Float> = callbackFlow {
        if (light == null) {
            throw SensorNotFoundException("Light sensor not available")
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                trySend(event.values[0])
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            light,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    fun getProximityData(): Flow<Float> = callbackFlow {
        if (proximity == null) {
            throw SensorNotFoundException("Proximity sensor not available")
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                trySend(event.values[0])
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            proximity,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    fun getDeviceOrientation(): Flow<OrientationData> = callbackFlow {
        if (accelerometer == null || magnetometer == null) {
            throw SensorNotFoundException("Required sensors not available")
        }

        val accelerometerReading = FloatArray(3)
        val magnetometerReading = FloatArray(3)
        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        System.arraycopy(event.values, 0, accelerometerReading, 0, 3)
                        updateOrientation()
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        System.arraycopy(event.values, 0, magnetometerReading, 0, 3)
                        updateOrientation()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

            private fun updateOrientation() {
                if (SensorManager.getRotationMatrix(
                        rotationMatrix,
                        null,
                        accelerometerReading,
                        magnetometerReading
                    )
                ) {
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                    trySend(OrientationData(
                        azimuth = Math.toDegrees(orientationAngles[0].toDouble()),
                        pitch = Math.toDegrees(orientationAngles[1].toDouble()),
                        roll = Math.toDegrees(orientationAngles[2].toDouble())
                    ))
                }
            }
        }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            listener,
            magnetometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    fun hasSensor(sensorType: Int): Boolean {
        return sensorManager.getDefaultSensor(sensorType) != null
    }

    companion object {
        fun isSensorAvailable(context: Context, sensorType: Int): Boolean {
            val sensorManager = 
                context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            return sensorManager.getDefaultSensor(sensorType) != null
        }
    }
}

data class AccelerometerData(
    val x: Float,
    val y: Float,
    val z: Float,
    val magnitude: Float
)

data class GyroscopeData(
    val x: Float,
    val y: Float,
    val z: Float
)

data class OrientationData(
    val azimuth: Double,  // Rotation around Z axis (0=North, 90=East, 180=South, 270=West)
    val pitch: Double,    // Rotation around X axis (-180 to 180)
    val roll: Double      // Rotation around Y axis (-90 to 90)
)

class SensorNotFoundException(message: String) : Exception(message)
