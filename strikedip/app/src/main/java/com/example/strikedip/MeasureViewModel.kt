package com.example.strikedip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MeasureViewModel(
    val strikeDipDao: StrikeDipDao,
    application: Application
) : AndroidViewModel(application){

    fun addStrikeDip(data: StrikeDipEntity){
        viewModelScope.launch{
            strikeDipDao.insertStrikeDip(data)
        }
    }
}

class MeasureViewModelFactory(
    private val dataSource: StrikeDipDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeasureViewModel::class.java)) {
            return MeasureViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}