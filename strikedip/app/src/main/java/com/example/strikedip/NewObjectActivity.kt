package com.example.strikedip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.strikedip.databinding.ActivityNewObjectBinding

class NewObjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewObjectBinding
    private lateinit var newObjectViewModel: NewObjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewObjectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val datasource = StrikeDipDatabase.getInstance(this).objectDao
        val viewModelFactory = NewObjectViewModelFactory(datasource, application)

        newObjectViewModel = ViewModelProvider(this, viewModelFactory).get(NewObjectViewModel::class.java)

        binding.btnCreateNewFieldActivity.setOnClickListener{
            val data = ObjectEntity(
                name = binding.edtNamaObject.text.toString(),
                note = binding.edtNote.text.toString()
            )

            newObjectViewModel.addObject(data)
            finish()
        }
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context, NewObjectActivity::class.java)
            context.startActivity(intent)
        }
    }
}