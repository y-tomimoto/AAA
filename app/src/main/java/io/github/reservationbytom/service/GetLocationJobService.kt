package io.github.reservationbytom.service

import android.Manifest
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GetLocationJobService : JobService() {

  // 位置情報を取得できるクラス
  private lateinit var fusedLocationClient: FusedLocationProviderClient

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

    // 3. 位置情報を取得する: https://developer.android.com/training/location/retrieve-current#last-known
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
    fusedLocationClient.lastLocation
      .addOnSuccessListener { location: Location? ->
        println(location)
        if (location != null) {
          println(location.latitude)
        }
        if (location != null) {
          println(location.longitude)
        }
      }


    jobFinished(params, false)

    return true // 処理が完了している場合はfalseを返す
  }
}
