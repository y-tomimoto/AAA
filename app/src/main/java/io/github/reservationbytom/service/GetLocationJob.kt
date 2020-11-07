package io.github.reservationbytom.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context

// スケジューラに登録する処理をまとめたclass
class GetLocationJob : JobService() {

  override fun onStopJob(params: JobParameters?): Boolean {
    println("Job stopping ...")
    return false // リトライする際は、trueを設定する
  }

  override fun onStartJob(params: JobParameters?): Boolean {

    // ここでThreadでwrapしている実装がある: https://qiita.com/naoi/items/f1d00a79196d3d2d3a81
    // Threadについて: https://developer.android.com/guide/components/processes-and-threads?hl=ja
    // JobShejulerはmainスレッドで走る: https://techium.hatenablog.com/entry/2016/03/09/003314
    // ここに実行したい処理を記載する
    // Runnableについて: https://developer.android.com/training/multiple-threads/define-runnable?hl=ja
    // Thread().start() // Threadの run() と start()の違い: https://ja.stackoverflow.com/questions/6118/thread-%E3%82%92-start-%E3%81%A8-run-%E3%81%A7%E5%AE%9F%E8%A1%8C%E3%81%99%E3%82%8B%E3%81%A8%E3%81%8D%E3%81%AE%E9%81%95%E3%81%84
    // ここでCoroutineを設定する

    // 取得した値を控え、それをsharedに保存する
    println("Job working ...")
    return true // 処理が完了している場合はfalseを返す
  }
}
