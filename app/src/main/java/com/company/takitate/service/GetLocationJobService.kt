package com.company.takitate.service

import android.Manifest
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.gms.location.*
import com.company.takitate.service.persistence.db.entity.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class GetLocationJobService : JobService() {

  // 位置情報を取得できるクラス
  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var context: Context
  private lateinit var db: AppDatabase

  override fun onCreate() {
    super.onCreate()
    context = applicationContext // For this Problem: https://stackoverflow.com/questions/21994612/get-application-context-returns-null
    db = Room.databaseBuilder(
      context,
      AppDatabase::class.java, "database-name3"
    ).build()
  }

  override fun onStopJob(params: JobParameters?): Boolean {
    println("Job stopping ...")
    return false // リトライする際は、trueを設定する
  }

  override fun onStartJob(params: JobParameters?): Boolean {

    // 要はここでactivityを採用したいかもーーー
    // ここでThreadでwrapしている実装がある: https://qiita.com/naoi/items/f1d00a79196d3d2d3a81
    // Threadについて: https://developer.android.com/guide/components/processes-and-threads?hl=ja
    // JobShejulerはmainスレッドで走る: https://techium.hatenablog.com/entry/2016/03/09/003314
    // ここに実行したい処理を記載する
    // Runnableについて: https://developer.android.com/training/multiple-threads/define-runnable?hl=ja
    // Thread().start() // Threadの run() と start()の違い: https://ja.stackoverflow.com/questions/6118/thread-%E3%82%92-start-%E3%81%A8-run-%E3%81%A7%E5%AE%9F%E8%A1%8C%E3%81%99%E3%82%8B%E3%81%A8%E3%81%8D%E3%81%AE%E9%81%95%E3%81%84

    // 位置情報を取得する (関数として呼び出す)

    // 2. 位置情報サービス クライアントを作成する: https://developer.android.com/training/location/retrieve-current#play-services
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return false
    }

    // 3. 位置情報を取得する: https://developer.android.com/training/location/retrieve-current#last-known
    fusedLocationClient.lastLocation
      .addOnSuccessListener { location: Location? ->
        if (location != null) {
          println(location.latitude)
          println(location.longitude)
          val uuid = UUID.randomUUID().toString()
          val tsLong = System.currentTimeMillis() / 1000
          val ts = tsLong.toString()
          val loca = com.company.takitate.service.persistence.db.entity.Location(
            uuid,
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = ts
          )

          Thread(
            Runnable {
              println("test")
              db.locationDao().insertAll(loca)
            }
          ).start()

        } else {
          // https://qiita.com/outerlet/items/78941b0b352c7003c01f
          val request = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(500)
            .setFastestInterval(300)

          fusedLocationClient
            .requestLocationUpdates(
              request,
              object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                  println(result)
                  // (b)LocationUpdates で得た時の処理
                  // 現在地だけ欲しいので、1回取得したらすぐに外す
                  fusedLocationClient.removeLocationUpdates(this)
                }
              },
              null
            )
        }
      }
      .addOnFailureListener {
        println("location error")
      }

    jobFinished(params, false)

    return true // 処理が完了している場合はfalseを返す
  }
}
