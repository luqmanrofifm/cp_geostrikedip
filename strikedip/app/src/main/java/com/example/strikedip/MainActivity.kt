package com.example.strikedip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            //createCSV()
        }


        binding.btnAddObject.setOnClickListener{
            NewObjectActivity.start(this)
        }
    }

//    private fun createCSV() {
//        val path = filesDir
//        val letDirectory = File(path, "DATA_CSV")
//        letDirectory.mkdirs()
//
//        val objectFile = File(letDirectory, "object.txt")
//        val strikeDipFile = File(letDirectory, "strikedip.txt")
//
//        objectFile.appendText("record goes here")
//    }

}