package com.example.strikedip

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StrikeDipEntity::class, ObjectEntity::class], version = 4, exportSchema = false)
abstract class StrikeDipDatabase:RoomDatabase() {
    abstract val strikeDipDao: StrikeDipDao
    abstract val objectDao: ObjectDao

    companion object{
        @Volatile
        private var INSTANCE: StrikeDipDatabase? = null

        fun getInstance(context: Context): StrikeDipDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StrikeDipDatabase::class.java,
                        "strikedip_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}