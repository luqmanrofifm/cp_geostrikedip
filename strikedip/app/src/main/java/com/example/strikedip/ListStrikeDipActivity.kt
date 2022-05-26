package com.example.strikedip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.strikedip.databinding.ActivityListStrikeDipBinding
import com.example.strikedip.databinding.ActivityMainBinding

class ListStrikeDipActivity : AppCompatActivity() {

    private val idObject by lazy {
        intent.getIntExtra(EXTRA_ID_OBJECT, 0)
    }

    private lateinit var binding: ActivityListStrikeDipBinding
    private lateinit var listStrikeDipViewModel: ListStrikeDipViewModel

    private var strikeDipAdapter = StrikeDipAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityListStrikeDipBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val datasource = StrikeDipDatabase.getInstance(this).strikeDipDao
        val viewModelFactory = ListStrikeDipViewModelFactory(datasource, application)

        listStrikeDipViewModel = ViewModelProvider(this, viewModelFactory).get(ListStrikeDipViewModel::class.java)

        strikeDipAdapter.onOpenDetailStrikeDip = {
            DetailStrikeDipActivity.start(this, it.id)
        }

        strikeDipAdapter.onDeleteStrikeDip = {
            listStrikeDipViewModel.deleteStrikeDip(it.id)
        }

        binding.rvStrikeDip.adapter = strikeDipAdapter

        listStrikeDipViewModel.getStrikeDip(idObject)

        listStrikeDipViewModel.listStrikeDip.observe(this){
            it.let {
                strikeDipAdapter.submitList(it)
            }
        }

        binding.btnAddStrikeDip.setOnClickListener{
            MeasureActivity.start(this, idObject)
        }

    }

    companion object{
        const val EXTRA_ID_OBJECT = "extra_id_object"

        fun start(context: Context, objectId: Int){
            val intent = Intent(context, ListStrikeDipActivity::class.java)
            intent.putExtra(EXTRA_ID_OBJECT, objectId)
            context.startActivity(intent)
        }
    }
}