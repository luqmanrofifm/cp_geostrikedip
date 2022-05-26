package com.example.strikedip

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strike_dip")
data class StrikeDipEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var objectId: Int = 0,

    var accelerometer_x: Double,
    var accelerometer_y: Double,
    var accelerometer_z: Double,

    var magnetometer_x: Double,
    var magnetometer_y: Double,
    var magnetometer_z: Double,

    var orientation_x: Double,
    var orientation_y: Double,
    var orientation_z: Double,

    var orientation_rad_x: Double,
    var orientation_rad_y: Double,
    var orientation_rad_z: Double,

    var strike_direct: Double,
    var arah_dip_direct: Double,
    var sudut_dip_direct: Double,

    var strike_vector: Double,
    var arah_dip_vector: Double,
    var sudut_dip_vector: Double,

    var strike_rotation: Double,
    var arah_dip_rotation: Double,
    var sudut_dip_rotation: Double,

    var strike_trigono: Double,
    var arah_dip_trigono: Double,
    var sudut_dip_trigono: Double,

    var time_direct: Long,
    var time_vector: Long,
    var time_rotation: Long,
    var time_trigono: Long
)