package com.example.strikedip

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class MainViewModel(
    val objectDao: ObjectDao,
    val strikeDipDao: StrikeDipDao,
    application: Application
) : AndroidViewModel(application){

    lateinit var listObject : LiveData<List<ObjectEntity>>
    lateinit var objectList: List<ObjectEntity>
    lateinit var strikeDipList: List<StrikeDipEntity>

    fun getObject(){
        viewModelScope.launch{
            listObject = objectDao.getObject()
        }
    }

    fun deleteObject(objectId: Int){
        viewModelScope.launch {
            strikeDipDao.deleteDataByObjectId(objectId)
            objectDao.deleteData(objectId)
        }
    }

    fun getDataList(){
        viewModelScope.launch {
            objectList = objectDao.getObjectList()
            strikeDipList = strikeDipDao.getDataStrikeDipList()
        }
    }
}

class MainViewModelFactory(
    private val dataSourceObject: ObjectDao,
    private val dataSourceStrikeDip: StrikeDipDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataSourceObject, dataSourceStrikeDip, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}