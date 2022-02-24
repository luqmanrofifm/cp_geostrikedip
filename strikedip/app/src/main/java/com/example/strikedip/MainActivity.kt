package com.example.strikedip

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.strikedip.databinding.ActivityMainBinding
import kotlin.math.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private lateinit var vectorMethod: Array<Double>
    private lateinit var rotationMethod: Array<Double>
    private lateinit var trigonoMethod: Array<Double>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        updateOrientationAngles()

        vectorMethod = vectorMethod(
            arrayOf(
                accelerometerReading[0].toDouble(),
                accelerometerReading[1].toDouble(),
                accelerometerReading[2].toDouble()
            ),
            arrayOf(
                magnetometerReading[0].toDouble(),
                magnetometerReading[1].toDouble(),
                magnetometerReading[2].toDouble()
            ),
            orientationAngles[1].toDouble()
        )

        rotationMethod = rotationMethod(
            orientationAngles[1].toDouble(),
            orientationAngles[2].toDouble(),
            orientationAngles[0].toDouble()
        )

        trigonoMethod = trigonoMethod(
            orientationAngles[1].toDouble(),
            orientationAngles[2].toDouble(),
            orientationAngles[0].toDouble()
        )

        initView()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        if (accuracy > 50) {
            Log.e("berubah akurasi", "lebih besar 50")
        }
    }

    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }

    private fun initView() {
        with(binding) {
            tvAcceloX.text = accelerometerReading[0].toString()
            tvAcceloY.text = accelerometerReading[1].toString()
            tvAcceloZ.text = accelerometerReading[2].toString()

            tvMagnetoX.text = magnetometerReading[0].toString()
            tvMagnetoY.text = magnetometerReading[1].toString()
            tvMagnetoZ.text = magnetometerReading[2].toString()

            tvDegZ.text = Math.toDegrees(orientationAngles[0].toDouble()).toString()
            tvDegX.text = Math.toDegrees(orientationAngles[1].toDouble()).toString()
            tvDegY.text = Math.toDegrees(orientationAngles[2].toDouble()).toString()

            tvRadZ.text = orientationAngles[0].toString()
            tvRadX.text = orientationAngles[1].toString()
            tvRadY.text = orientationAngles[2].toString()

            tvSudutDipVector.text = vectorMethod[0].toString()
            tvSudutDipRotate.text = rotationMethod[0].toString()
            tvSudutDipTrigono.text = trigonoMethod[0].toString()

            tvArahDipVector.text = vectorMethod[1].toString()
            tvArahDipRotate.text = rotationMethod[1].toString()
            tvArahDipTrigono.text = trigonoMethod[1].toString()

            tvStrikeVector.text = vectorMethod[2].toString()
            tvStrikeRotate.text = rotationMethod[2].toString()
            tvStrikeTrigono.text = trigonoMethod[2].toString()
        }
    }

    private fun vectorMethod(
        accelerometer: Array<Double>,
        magnetometer: Array<Double>,
        pitch: Double
    ): Array<Double> {

        val n2 = if (pitch > -90 && pitch < 90) {
            -1
        } else {
            1
        }
        val N = arrayOf(0, 0, n2)

        val crossAccelMagnet = arrayOf(
            accelerometer[1] * magnetometer[2] - accelerometer[2] * magnetometer[1],
            accelerometer[2] * magnetometer[0] - accelerometer[0] * magnetometer[2],
            accelerometer[0] * magnetometer[1] - accelerometer[1] * magnetometer[0]
        )

        val crossAccelN = arrayOf(
            accelerometer[1] * N[2] - accelerometer[2] * N[1],
            accelerometer[2] * N[0] - accelerometer[0] * N[2],
            accelerometer[0] * N[1] - accelerometer[1] * N[0]
        )

        val A2E = Math.toDegrees(
            acos(
                (crossAccelMagnet[0] * crossAccelN[0]
                        + crossAccelMagnet[1] * crossAccelN[1] +
                        crossAccelMagnet[2] * crossAccelN[2]) /
                        (sqrt(
                            crossAccelMagnet[0] * crossAccelMagnet[0] +
                                    crossAccelMagnet[1] * crossAccelMagnet[1] +
                                    crossAccelMagnet[2] * crossAccelMagnet[2]
                        ) * sqrt(
                            crossAccelN[0] * crossAccelN[0] +
                                    crossAccelN[1] * crossAccelN[1] +
                                    crossAccelN[2] * crossAccelN[2]
                        ))
            )
        )

        val clockwise =
            if ((crossAccelN[0] * crossAccelMagnet[1] - crossAccelMagnet[0] * crossAccelN[1]) > 0) {
                1
            } else {
                -1
            }

        val sudutDip = Math.toDegrees(
            acos(
                abs(
                    accelerometer[2] / sqrt(
                        accelerometer[0] * accelerometer[0] +
                                accelerometer[1] * accelerometer[1] +
                                accelerometer[2] * accelerometer[2]
                    )
                )
            )
        )
        val strike = (90 + (clockwise * A2E) + 720) % 360
        val arahDip = (strike - N[2] * 90 + 720) % 360

        return arrayOf(sudutDip, arahDip, strike)
    }

    private fun rotationMethod(pitch: Double, roll: Double, azimuth: Double): Array<Double> {
        val vx = sin(pitch) * sin(azimuth) + cos(pitch) * cos(azimuth) * sin(roll)
        val vy = cos(pitch) * sin(roll) * sin(azimuth) - cos(azimuth) * sin(pitch)
        val vz = cos(pitch) * cos(roll)

        val dipAngle: Double = if (vz > 0) {
            Math.toDegrees(atan(sqrt(vx * vx + vy * vy) / vz))
        } else {
            Math.toDegrees(atan(sqrt(vx * vx + vy * vy) / -vz))
        }

        //Log.d("vx", vx.toString())
        //Log.d("vy", vy.toString())
        //Log.d("vz", vz.toString())
        //Log.d("atan", Math.toDegrees(atan(vy / vx)).toString())

        val strikeOfDip: Double = if (vz >= 0) {
            if (vx > 0) {
                90 + Math.toDegrees(atan(vy / vx))
            } else {
                270 + Math.toDegrees(atan(vy / vx))
            }
        } else {
            if (vx > 0) {
                270 + Math.toDegrees(atan(vy / vx))
            } else {
                90 + Math.toDegrees(atan(vy / vx))
            }
        }

        val strike = (strikeOfDip - 90 + 720)%360
//            if (strikeOfDip < 90) {
//            360 - (strikeOfDip - 90)
//        } else {
//            strikeOfDip - 90
//        }

        return arrayOf(dipAngle, strikeOfDip, strike)
    }

    private fun trigonoMethod(pitch: Double, roll: Double, azimuth: Double):Array<Double> {
        val sinPitch = sin(pitch)
        val sinRoll = sin(roll)

        val dipAngle = abs(
            Math.toDegrees(
                asin(sqrt(1/sinPitch/sinPitch+1/sinRoll/sinRoll)*sinPitch*sinRoll)
            )
        )
        val epsilon = Math.toDegrees(acos(sinPitch / sin(Math.toRadians(dipAngle))))

        val arahDip = if(roll>0){
            (azimuth-epsilon+720)%360
        } else{
            (azimuth+epsilon+720)%360
        }

        val strike = (arahDip - 90 + 720)%360

        return arrayOf(dipAngle,arahDip,strike)
    }

}