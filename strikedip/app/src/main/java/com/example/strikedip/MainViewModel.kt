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
    lateinit var listStrikeDip: LiveData<List<StrikeDipEntity>>

    var objectList: List<ObjectEntity>  = emptyList()
    var strikeDipList: List<StrikeDipEntity> = emptyList()

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
            //objectList = objectDao.getObjectList()
            listStrikeDip = strikeDipDao.getAllDataStrikeDip()
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