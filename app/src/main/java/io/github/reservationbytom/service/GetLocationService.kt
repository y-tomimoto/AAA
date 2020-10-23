package io.github.reservationbytom.service

import android.app.job.JobParameters
import android.app.job.JobService

// スケジューラに登録する処理をまとめたclass
class GetLocationService:JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        println("getLocationService not running ...")
        return true // リトライする際は、trueを設定する
     }

    override fun onStartJob(p0: JobParameters?): Boolean {
        // ここに実行したい処理を記載する
        println("getLocationService running ...")
        return false // 処理が完了している場合はfalseを返す
    }
}