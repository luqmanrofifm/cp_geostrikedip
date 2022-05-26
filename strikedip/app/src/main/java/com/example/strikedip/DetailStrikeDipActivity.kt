package com.example.strikedip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.strikedip.databinding.ActivityDetailStrikeDipBinding

class DetailStrikeDipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStrikeDipBinding
    private lateinit var detailStrikeDipViewModel: DetailStrikeDipViewModel

    private val id by lazy {
        intent.getIntExtra(DetailStrikeDipActivity.EXTRA_ID, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStrikeDipBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val datasource = StrikeDipDatabase.getInstance(this).strikeDipDao
        val viewModelFactory = DetailStrikeDipViewModelFactory(datasource, application)

        detailStrikeDipViewModel = ViewModelProvider(this, viewModelFactory).get(DetailStrikeDipViewModel::class.java)

        detailStrikeDipViewModel.getStrikeDip(id)

        detailStrikeDipViewModel.dataStrikeDip.observe(this){
            with(binding){
                tvAcceloX.text = it.accelerometer_x.toString()
                tvAcceloY.text = it.accelerometer_y.toString()
                tvAcceloZ.text = it.accelerometer_z.toString()

                tvMagnetoX.text = it.magnetometer_x.toString()
                tvMagnetoY.text = it.magnetometer_y.toString()
                tvMagnetoZ.text = it.magnetometer_z.toString()

                tvDegZ.text = it.orientation_z.toString()
                tvDegX.text = it.orientation_x.toString()
                tvDegY.text = it.orientation_y.toString()

                tvRadZ.text = it.orientation_rad_z.toString()
                tvRadX.text = it.orientation_rad_x.toString()
                tvRadY.text = it.orientation_rad_y.toString()

                tvSudutDipDirectRead.text = it.sudut_dip_direct.toString()
                tvSudutDipVector.text = it.sudut_dip_vector.toString()
                tvSudutDipRotate.text = it.sudut_dip_rotation.toString()
                tvSudutDipTrigono.text = it.sudut_dip_trigono.toString()

                tvArahDipDirectRead.text = it.arah_dip_direct.toString()
                tvArahDipVector.text = it.arah_dip_vector.toString()
                tvArahDipRotate.text = it.arah_dip_rotation.toString()
                tvArahDipTrigono.text = it.arah_dip_trigono.toString()

                tvStrikeDirectRead.text = it.strike_direct.toString()
                tvStrikeVector.text = it.strike_vector.toString()
                tvStrikeRotate.text = it.strike_rotation.toString()
                tvStrikeTrigono.text = it.strike_trigono.toString()

                tvTimeDirectRead.text = it.time_direct.toString()
                tvTimeVector.text = it.time_vector.toString()
                tvTimeRotate.text = it.time_rotation.toString()
                tvTimeTrigono.text = it.time_trigono.toString()
            }
        }
    }

    companion object{
        const val EXTRA_ID = "extra_id"
        fun start(context: Context, id: Int){
            val intent = Intent(context, DetailStrikeDipActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            context.startActivity(intent)
        }
    }
}