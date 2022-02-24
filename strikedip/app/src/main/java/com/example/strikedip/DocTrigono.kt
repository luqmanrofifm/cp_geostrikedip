package com.example.strikedip

import android.util.Log
import kotlin.math.*

class DocTrigono {


    //trigono function
  private fun measureDipTrigono(pitch: Double, roll: Double): Double{
      val sinPitch = sin(pitch)
      val sinRoll = sin(roll)
      //Log.d("sin pitch",sinPitch.toString())
      //Log.d("sin roll", sinRoll.toString())

      val sqrt = sqrt(1/sinPitch/sinPitch+1/sinRoll/sinRoll)
      //Log.d("sqrt",sqrt.toString())

      val sinAngle = (sqrt * sinPitch * sinRoll * 100000.0).roundToLong() /100000.0
      //Log.d("sin angle", sinAngle.toString())

      var dipAngle: Double
      if(abs(sinAngle) >= 1.1){
          dipAngle = abs(Math.toDegrees(asin(1.0-(sinAngle-1.0))))
      }
      else if(abs(sinAngle)>=1.0 && abs(sinAngle)<1.1){
          dipAngle = 90.0
      }
      else{
          dipAngle = abs(Math.toDegrees(asin(sinAngle)))
      }

      /* jaga jaga code
      val dipAngle = Math.abs(Math.toDegrees(Math.asin(Math.sqrt(1/sinPitch/sinPitch+1/sinRoll/sinRoll)*sinPitch*sinRoll)))
      val dipAngle = abs(Math.toDegrees(asin(sinAngle)))*/

      return  dipAngle
  }

  private fun measureStrikeOfDipTrigono(pitch: Double, roll: Double, azimuth:Double):Double {
      val sinPitch = sin(pitch)
      val sinRoll = sin(roll)
      val sinDipAngle = sqrt(sinPitch * sinPitch + sinRoll * sinRoll)
          //sqrt(1 / sinPitch / sinPitch + 1 / sinRoll / sinRoll) * sinPitch * sinRoll

      Log.d("sin pitch", sinPitch.toString())
      Log.d("sin dip angle", sinDipAngle.toString())

      val epsilon1 = Math.toDegrees(acos(sinPitch / sinDipAngle))
      var strikeOfDip1: Double
      var strikeOfDip2: Double
      var strikeOfDip3: Double

      Log.d("azimuth 1", Math.toDegrees(azimuth).toString())
      Log.d("epsilon 1", epsilon1.toString())

      //first try
      if (Math.toDegrees(roll) > 0 && Math.toDegrees(pitch) > 0) {
          val epsilon2 = Math.toDegrees(acos(sinPitch / sinDipAngle))
          Log.d("epsilon 2", epsilon2.toString())

          strikeOfDip1 = Math.toDegrees(azimuth) - epsilon1
          strikeOfDip2 = Math.toDegrees(azimuth) - epsilon2
          Log.d("arah dip 1", strikeOfDip1.toString())
          Log.d("arah dip 2", strikeOfDip2.toString())
      } else if (Math.toDegrees(roll) > 0 && Math.toDegrees(pitch) < 0) {
          val epsilon2 = 180 - Math.toDegrees(acos(sinPitch / sinDipAngle))
          Log.d("epsilon 2", epsilon2.toString())

          strikeOfDip1 = Math.toDegrees(azimuth) - epsilon1
          strikeOfDip2 = Math.toDegrees(azimuth) - epsilon2
          Log.d("arah dip 1", strikeOfDip1.toString())
          Log.d("arah dip 2", strikeOfDip2.toString())
      } else if (Math.toDegrees(roll) < 0 && Math.toDegrees(pitch) < 0) {
          val epsilon2 = 180 - Math.toDegrees(acos(sinPitch / sinDipAngle))
          Log.d("epsilon 2", epsilon2.toString())

          strikeOfDip1 = Math.toDegrees(azimuth) - epsilon1
          strikeOfDip2 = Math.toDegrees(azimuth) - epsilon2
          Log.d("arah dip 1", strikeOfDip1.toString())
          Log.d("arah dip 2", strikeOfDip2.toString())
      } else {
          val epsilon2 = Math.toDegrees(acos(sinPitch / sinDipAngle))
          Log.d("epsilon 2", epsilon2.toString())

          strikeOfDip1 = Math.toDegrees(azimuth) - epsilon1
          strikeOfDip2 = Math.toDegrees(azimuth) - epsilon2
          Log.d("arah dip 1", strikeOfDip1.toString())
          Log.d("arah dip 2", strikeOfDip2.toString())
      }

      // second try
      if(Math.toDegrees(roll)>0){
          val epsilon = Math.toDegrees(acos(sinPitch / sinDipAngle))
          //Log.d("epsilon 2", epsilon.toString())
          strikeOfDip3 = (Math.toDegrees(azimuth)-epsilon+720) % 360
          Log.d("arah dip 3", strikeOfDip2.toString())
      }
      else {
          val epsilon = Math.toDegrees(acos(sinPitch / sinDipAngle))
          //Log.d("epsilon 2", epsilon.toString())
          strikeOfDip3 = (Math.toDegrees(azimuth)+epsilon+720) % 360
          Log.d("arah dip 3", strikeOfDip2.toString())
      }

      return strikeOfDip3
  }

    /* cek sensor
            Log.d(
                "Radian",
                "value[0] is " + orientationAngles[0] + ", " +
                        "value[1] is " + orientationAngles[1] + ", " +
                        "value[2] is " + orientationAngles[2]
            );
            Log.d(
                "Derajat",
                "value[0] is " + Math.toDegrees(orientationAngles[0].toDouble()) + ", " +
                        "value[1] is " + Math.toDegrees(orientationAngles[1].toDouble()) + ", " +
                        "value[2] is " + Math.toDegrees(orientationAngles[2].toDouble())
            );*/

    // oncreate btn click

    /* trigono function
            val dipAngle = measureDip(orientationAngles[1].toDouble(),orientationAngles[2].toDouble())
            val strikeOfDip = measureStrikeOfDip(orientationAngles[1].toDouble(),orientationAngles[2].toDouble(),orientationAngles[0].toDouble())
             */

    /*val dip = measureDipRotation(
        orientationAngles[1].toDouble(),
        orientationAngles[2].toDouble(),
        orientationAngles[0].toDouble()
    )
    binding.tvSudutDip.setText(dip[0].toString())
    binding.tvArahDip.setText(dip[1].toString())*/

}