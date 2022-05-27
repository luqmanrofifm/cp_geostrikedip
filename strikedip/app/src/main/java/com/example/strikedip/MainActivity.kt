package com.example.strikedip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.strikedip.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var objectAdapter = ObjectAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val datasourceObject = StrikeDipDatabase.getInstance(this).objectDao
        val dataSourceStrikeDip = StrikeDipDatabase.getInstance(this).strikeDipDao
        val viewModelFactory = MainViewModelFactory(datasourceObject, dataSourceStrikeDip, application)

        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        objectAdapter.onOpenListStrikeDipActivity = {
            ListStrikeDipActivity.start(this, it.id)
        }

        objectAdapter.onDeleteObject = {
            mainViewModel.deleteObject(it.id)
        }

        binding.rvObject.adapter = objectAdapter

        mainViewModel.getObject()

        mainViewModel.listObject.observe(this){
            it.let {
                objectAdapter.submitList(it)
            }
        }

        binding.btnExportCSV.setOnClickListener {
            createCSV()
        }


        binding.btnAddObject.setOnClickListener{
            NewObjectActivity.start(this)
        }
    }

    private fun createCSV() {
        mainViewModel.getDataList()

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        val objectFile = File.createTempFile("object", ".csv", storageDir)
        val strikeDipFile = File.createTempFile("strikedip", ".csv", storageDir)

        objectFile.appendText("id,name,note"+"\n")
        strikeDipFile.appendText(
            "id,objectId," +
                    "accelerometer_x,accelerometer_y,accelerometer_z," +
                    "magnetometer_x,magnetometer_y,magnetometer_z," +
                    "orientation_x,orientation_y,orientation_z," +
                    "orientation_rad_x,orientation_rad_y,orientation_rad_z," +
                    "strike_direct,arah_dip_direct,sudut_dip_direct," +
                    "strike_vector,arah_dip_vector,sudut_dip_vector," +
                    "strike_rotation,arah_dip_rotation,sudut_dip_rotation," +
                    "strike_trigono,arah_dip_trigono,sudut_dip_trigono," +
                    "time_direct,time_vector,time_rotation,time_trigono"+"\n"
        )

        mainViewModel.listObject.observe(this){
            it.forEach { data ->
                objectFile.appendText(data.id.toString()+","+data.name+","+data.note+"\n")
            }
        }

        mainViewModel.listStrikeDip.observe(this){
            it.forEach { data ->
                strikeDipFile.appendText(
                    data.id.toString()+","+data.objectId+"," +
                            data.accelerometer_x+","+data.accelerometer_y+","+data.accelerometer_z+"," +
                            data.magnetometer_x+","+data.magnetometer_y+","+data.magnetometer_z+"," +
                            data.orientation_x+","+data.orientation_y+","+data.orientation_z+"," +
                            data.orientation_rad_x+","+data.orientation_rad_y+","+data.orientation_rad_z+"," +
                            data.strike_direct+","+data.arah_dip_direct+","+data.sudut_dip_direct+"," +
                            data.strike_vector+","+data.arah_dip_vector+","+data.sudut_dip_vector+"," +
                            data.strike_rotation+","+data.arah_dip_rotation+","+data.sudut_dip_rotation+","+
                            data.strike_trigono+","+data.arah_dip_trigono+","+data.sudut_dip_trigono+","+
                            data.time_direct+","+data.time_vector+","+data.time_rotation+","+data.time_trigono+"\n"
                )
            }
        }

        SuccessActivity.start(this)
    }

}