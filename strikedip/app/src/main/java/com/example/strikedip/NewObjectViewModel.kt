package com.example.strikedip

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class NewObjectViewModel(
        val objectDao: ObjectDao,
        application: Application
) : AndroidViewModel(application){

    fun addObject(data: ObjectEntity){
        viewModelScope.launch{
            objectDao.insertObject(data)
        }
    }
}

class NewObjectViewModelFactory(
    private val dataSource: ObjectDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewObjectViewModel::class.java)) {
            return NewObjectViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}