package com.example.strikedip

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ObjectDao {
    @Insert
    suspend fun insertObject(data: ObjectEntity)

    @Query("SELECT * FROM object")
    fun getObject(): LiveData<List<ObjectEntity>>

    @Query("SELECT * FROM object")
    fun getObjectList(): List<ObjectEntity>

    @Query("DELETE FROM object WHERE id =:id")
    suspend fun deleteData(id: Int)
}