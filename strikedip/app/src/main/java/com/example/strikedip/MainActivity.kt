package com.example.strikedip

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.strikedip.databinding.ActivityMainBinding
import com.mapbox.maps.MapInitOptions

class MainActivity : AppCompatActivity(), SensorEventListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        MapInitOptions.getDefaultResourceOptions(BuildConfig.mapbox_access_token)

        binding.btnMap.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
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

    private fun initView(){
        with(binding){
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
        }
    }

    private fun vectorMethod(){}

    private fun rotationMethod(){}

    private fun trigonoMethod(){}
}