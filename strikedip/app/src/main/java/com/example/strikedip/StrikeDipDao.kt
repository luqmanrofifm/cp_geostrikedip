package com.example.strikedip

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StrikeDipDao {
    @Insert
    suspend fun insertStrikeDip(data: StrikeDipEntity)

    @Query("SELECT * FROM strike_dip WHERE objectId =:objectId")
    fun getDataStrikeDip(objectId: Int): LiveData<List<StrikeDipEntity>>

    @Query("SELECT * FROM strike_dip WHERE id =:id")
    fun getDataStrikeDipById(id: Int): LiveData<StrikeDipEntity>

    @Query("SELECT * FROM strike_dip")
    fun getAllDataStrikeDip(): LiveData<List<StrikeDipEntity>>

    @Query("SELECT * FROM strike_dip")
    fun getDataStrikeDipList(): List<StrikeDipEntity>

    @Query("DELETE FROM strike_dip WHERE id =:id")
    suspend fun deleteData(id: Int)

    @Query("DELETE FROM strike_dip WHERE objectId =:objectId")
    suspend fun deleteDataByObjectId(objectId: Int)
}