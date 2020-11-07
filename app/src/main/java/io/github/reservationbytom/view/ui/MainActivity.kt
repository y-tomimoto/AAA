package io.github.reservationbytom.view.ui

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.reservationbytom.R
import io.github.reservationbytom.R.id
import io.github.reservationbytom.service.GetLocationJob
import io.github.reservationbytom.service.GetLocationService
import io.github.reservationbytom.service.model.Rest
import java.util.*

class MainActivity : AppCompatActivity() {
  val MY_BACKGROUND_JOB = 0

  // Jobをset : https://developer.android.com/topic/performance/background-optimization?hl=ja
  fun scheduleJob(context: Context) {
    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    val job = JobInfo.Builder(
      MY_BACKGROUND_JOB,
      ComponentName(context, GetLocationJob::class.java)
    )
      .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
      .setRequiresCharging(true)
      .setPeriodic(Calendar.MINUTE.toLong() * 15) // 15分ごとしか無理: https://medium.com/@yonatanvlevin/the-minimum-interval-for-periodicwork-is-15-minutes-same-as-jobscheduler-periodic-job-eb2d63716d1f
      .build()
    jobScheduler.schedule(job)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    scheduleJob(this)

    // savedInstanceState == null について : https://qiita.com/Nkzn/items/c09629d91d5cf42ff05d
    if (savedInstanceState == null) {

      val fragment = RestListFragment()

      supportFragmentManager
        .beginTransaction()
        .add(
          id.fragment_container, fragment,
          TAG_OF_REST_LIST_FRAGMENT
        )
        .commit()
    }
  }

  //詳細画面への遷移
  fun show(rest: Rest) {
    val restFragment = RestFragment.forRest(rest.name)

    supportFragmentManager
      .beginTransaction()
      .addToBackStack("rest")
      .replace(id.fragment_container, restFragment, null)
      .commit()
  }
}
