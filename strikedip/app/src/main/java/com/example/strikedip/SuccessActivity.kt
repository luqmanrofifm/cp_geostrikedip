package com.example.strikedip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context, SuccessActivity::class.java)
            context.startActivity(intent)
        }
    }
}