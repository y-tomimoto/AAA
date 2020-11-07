package io.github.reservationbytom.service

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.location.*
import com.google.gson.GsonBuilder
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.reservationbytom.BuildConfig
import io.github.reservationbytom.R
import io.github.reservationbytom.view.ui.MainActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.IOException
import java.util.concurrent.TimeUnit

class GetLocationService : AppCompatActivity() { // TODO: AppCompatActivityを継承せずPermissionを確認

  // 位置情報を取得できるクラス
  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private val REQUEST_CODE = 1000

  override fun onCreate(savedInstanceState: Bundle?) { // 関数っぽく位置情報を取得したい
    super.onCreate(savedInstanceState)

    // 1. Permissionの許諾を取る: https://developer.android.com/training/location/retrieve-current#permissions
    if (Build.VERSION.SDK_INT >= 23) { // 23以降厳格なPermission許諾が求められる
      println("permissions checking ...")
      if (ActivityCompat.checkSelfPermission(
          this,
          Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
          this,
          Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
          this,
          Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        val permissions = arrayOf(
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        // permissions が指定されていない場合、permissionsをrequestする
        println("request permissions ...")
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
      }
    }

    // 2. 位置情報サービス クライアントを作成する: https://developer.android.com/training/location/retrieve-current#play-services
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    // 3. 接続方法を指定
    val locationRequest = LocationRequest().apply {
      // 精度重視(電力大)と省電力重視(精度低)を両立するため2種類の更新間隔を指定
      // 今回は公式のサンプル通りにする。
      interval = 10000                                   // 最遅の更新間隔(但し正確ではない。)
      fastestInterval = 5000                             // 最短の更新間隔
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY  // 精度重視
    }

    // 4. 実行時の挙動を指定
    val locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        // 更新直後の位置が格納されているはず
        val location = locationResult?.lastLocation ?: return
        println(location)
        println(location.latitude)
        println(location.longitude)
      }
    }

    // 5. 位置情報を取得
    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
  }
}

