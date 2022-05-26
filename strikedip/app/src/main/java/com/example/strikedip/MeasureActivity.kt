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
import androidx.lifecycle.ViewModelProvider
import com.example.strikedip.databinding.ActivityMainBinding
import com.example.strikedip.databinding.ActivityMeasureBinding
import kotlin.math.*
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

class MeasureActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMeasureBinding
    private lateinit var sensorManager: SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private lateinit var directReadingMethod: Array<Double>
    private lateinit var vectorMethod: Array<Double>
    private lateinit var rotationMethod: Array<Double>
    private lateinit var trigonoMethod: Array<Double>

    private var time_direct: Long = 0
    private var time_vector: Long = 0
    private var time_rotation: Long = 0
    private var time_trigono: Long = 0

    private val idObject by lazy {
        intent.getIntExtra(ListStrikeDipActivity.EXTRA_ID_OBJECT, 0)
    }

    private lateinit var measureViewModel: MeasureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeasureBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val datasource = StrikeDipDatabase.getInstance(this).strikeDipDao
        val viewModelFactory = MeasureViewModelFactory(datasource, application)

        measureViewModel = ViewModelProvider(this, viewModelFactory).get(MeasureViewModel::class.java)

        binding.btnTakeStrikeDip.setOnClickListener{
            val data = StrikeDipEntity(
                objectId = idObject,

                accelerometer_x = accelerometerReading[0].toDouble(),
                accelerometer_y = accelerometerReading[1].toDouble(),
                accelerometer_z = accelerometerReading[2].toDouble(),

                magnetometer_x = magnetometerReading[0].toDouble(),
                magnetometer_y = magnetometerReading[1].toDouble(),
                magnetometer_z = magnetometerReading[2].toDouble(),

                orientation_x = Math.toDegrees(orientationAngles[1].toDouble()),
                orientation_y = Math.toDegrees(orientationAngles[2].toDouble()),
                orientation_z =  Math.toDegrees(orientationAngles[0].toDouble()),

                orientation_rad_x = orientationAngles[1].toDouble(),
                orientation_rad_y = orientationAngles[2].toDouble(),
                orientation_rad_z = orientationAngles[0].toDouble(),

                strike_direct = directReadingMethod[2],
                arah_dip_direct = directReadingMethod[1],
                sudut_dip_direct = directReadingMethod[0],

                strike_vector = vectorMethod[2],
                arah_dip_vector = vectorMethod[1],
                sudut_dip_vector = vectorMethod[0],

                strike_rotation = rotationMethod[2],
                arah_dip_rotation = rotationMethod[1],
                sudut_dip_rotation = rotationMethod[0],

                strike_trigono = trigonoMethod[2],
                arah_dip_trigono = trigonoMethod[1],
                sudut_dip_trigono = trigonoMethod[0],

                time_direct = time_direct,
                time_vector = time_vector,
                time_rotation = time_rotation,
                time_trigono = time_trigono
            )
            measureViewModel.addStrikeDip(data)
            finish()
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

        directReadingMethod = directReadingMethod(
            arrayOf(
                accelerometerReading[0].toDouble(),
                accelerometerReading[1].toDouble(),
                accelerometerReading[2].toDouble()
            )
        )

        time_direct = measureNanoTime {
            directReadingMethod(
                arrayOf(
                    accelerometerReading[0].toDouble(),
                    accelerometerReading[1].toDouble(),
                    accelerometerReading[2].toDouble()
                )
            )
        }

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

        time_vector = measureNanoTime {
            vectorMethod(
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
        }

        rotationMethod = rotationMethod(
            orientationAngles[1].toDouble(),
            orientationAngles[2].toDouble(),
            orientationAngles[0].toDouble()
        )

        time_rotation = measureNanoTime {
            rotationMethod(
                orientationAngles[1].toDouble(),
                orientationAngles[2].toDouble(),
                orientationAngles[0].toDouble()
            )
        }

        trigonoMethod = trigonoMethod(
            orientationAngles[1].toDouble(),
            orientationAngles[2].toDouble(),
            orientationAngles[0].toDouble()
        )

        time_trigono = measureNanoTime {
            trigonoMethod(
                orientationAngles[1].toDouble(),
                orientationAngles[2].toDouble(),
                orientationAngles[0].toDouble()
            )
        }

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

            tvSudutDipDirectRead.text = directReadingMethod[0].toString()
            tvSudutDipVector.text = vectorMethod[0].toString()
            tvSudutDipRotate.text = rotationMethod[0].toString()
            tvSudutDipTrigono.text = trigonoMethod[0].toString()

            tvArahDipDirectRead.text = directReadingMethod[1].toString()
            tvArahDipVector.text = vectorMethod[1].toString()
            tvArahDipRotate.text = rotationMethod[1].toString()
            tvArahDipTrigono.text = trigonoMethod[1].toString()

            tvStrikeDirectRead.text = directReadingMethod[2].toString()
            tvStrikeVector.text = vectorMethod[2].toString()
            tvStrikeRotate.text = rotationMethod[2].toString()
            tvStrikeTrigono.text = trigonoMethod[2].toString()

            tvTimeDirectRead.text = time_direct.toString()
            tvTimeVector.text = time_vector.toString()
            tvTimeRotate.text = time_rotation.toString()
            tvTimeTrigono.text = time_trigono.toString()
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
                asin(sqrt(1/sinPitch/sinPitch+1/sinRoll/sinRoll) *sinPitch*sinRoll)
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

    private fun directReadingMethod(accelerometer: Array<Double>): Array<Double>{
        val dipAngle = if (accelerometer[2] > 0){
            atan(sqrt(accelerometer[0].pow(2) + accelerometer[1].pow(2)) /accelerometer[2])
        } else if (accelerometer[2] < 0){
            atan(sqrt(accelerometer[0].pow(2) + accelerometer[1].pow(2)) /(-1 * accelerometer[2]))
        } else {
            90.0
        }

        val arahDip = if (accelerometer[0] != 0.0){
            if (accelerometer[2] >= 0){
                if (accelerometer[0]>0){
                    180 - atan(accelerometer[1]/accelerometer[0])
                } else {
                    0 - atan(accelerometer[1]/accelerometer[0])
                }
            } else {
                if (accelerometer[0]>0){
                    0 - atan(accelerometer[1]/accelerometer[0])
                } else {
                    180 - atan(accelerometer[1]/accelerometer[0])
                }
            }
        } else {
            if(accelerometer[1]>0){
                0.0
            } else if (accelerometer[1]<0){
                90.0
            } else {
                Double.NaN
            }
        }

        val strike = (arahDip - 90 + 720)%360
        return arrayOf(dipAngle, arahDip, strike)
    }

    companion object{
        const val EXTRA_ID_OBJECT = "extra_id_object"

        fun start(context: Context, objectId: Int){
            val intent = Intent(context, MeasureActivity::class.java)
            intent.putExtra(EXTRA_ID_OBJECT, objectId)
            context.startActivity(intent)
        }
    }
}