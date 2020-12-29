package com.company.takitate.domain.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MyLocationManager (private val context: Context, private val activity: Activity) {

  private val REQUEST_CODE = 1000
  private var fusedLocationClient: FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(context)

  fun getLocation(callback: LocationCallback){

    // Permission Check
    if (ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {

      // Permissionの許諾を取る: https://developer.android.com/training/location/retrieve-current#permissions
      if (Build.VERSION.SDK_INT >= 23) { // 23以降厳格なPermission許諾が求められる
        if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
          ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
          ) != PackageManager.PERMISSION_GRANTED
        ) {
          val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
          )
          // permissions が指定されていない場合、permissionsをrequestする
          ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
        }
      }
    }

    fusedLocationClient.lastLocation
      .addOnSuccessListener { location: Location? ->
        // requestを生成
        val request = LocationRequest.create()
          .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
          .setInterval(500)
          .setFastestInterval(300)

        // 生成したrequestを用いてLocationを取得
        fusedLocationClient
          .requestLocationUpdates(
            request,
            callback, // getLocationから受け取ったcallback
//            object : LocationCallback() {
//              override fun onLocationResult(result: LocationResult) {
//                println("dadada")
//                fusedLocationClient.removeLocationUpdates(this)
//              }
//            },
            null
          )
      }
      .addOnFailureListener {
        println("failed")
      }
  }
}



