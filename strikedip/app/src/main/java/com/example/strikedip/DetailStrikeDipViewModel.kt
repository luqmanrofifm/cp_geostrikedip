package com.example.strikedip

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class DetailStrikeDipViewModel(
    val strikeDipDao: StrikeDipDao,
    application: Application
) : AndroidViewModel(application){

    lateinit var dataStrikeDip : LiveData<StrikeDipEntity>

    fun getStrikeDip(id: Int){
        viewModelScope.launch{
            dataStrikeDip = strikeDipDao.getDataStrikeDipById(id)
        }
    }
}

class DetailStrikeDipViewModelFactory(
    private val dataSource: StrikeDipDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailStrikeDipViewModel::class.java)) {
            return DetailStrikeDipViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}