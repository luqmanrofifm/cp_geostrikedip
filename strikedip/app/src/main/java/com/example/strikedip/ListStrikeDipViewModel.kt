package com.example.strikedip

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ListStrikeDipViewModel(
    val strikeDipDao: StrikeDipDao,
    application: Application
) : AndroidViewModel(application){

    lateinit var listStrikeDip : LiveData<List<StrikeDipEntity>>

    fun getStrikeDip(objectId: Int){
        viewModelScope.launch{
            listStrikeDip = strikeDipDao.getDataStrikeDip(objectId)
        }
    }

    fun deleteStrikeDip(id: Int){
        viewModelScope.launch {
            strikeDipDao.deleteData(id)
        }
    }
}

class ListStrikeDipViewModelFactory(
    private val dataSource: StrikeDipDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListStrikeDipViewModel::class.java)) {
            return ListStrikeDipViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}