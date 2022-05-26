package com.example.strikedip

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "object")
data class ObjectEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var name: String,
    var note: String
)