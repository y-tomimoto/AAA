package io.github.reservationbytom.service.repository

import com.google.gson.GsonBuilder
import io.github.reservationbytom.service.model.GNaviResponse
import io.github.reservationbytom.service.model.Rest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val HTTPS_API_GNAVI_URL = "https://api.gnavi.co.jp/"

class GNaviRepository {
  private val retrofit: Retrofit = Retrofit.Builder()
    // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(
      // TODO: kotlinx.serialization を採用する
      GsonConverterFactory.create(
        GsonBuilder().serializeNulls().create()
      )
    )
    .baseUrl(HTTPS_API_GNAVI_URL)
    .build()

  private val gNaviService = retrofit.create(GNaviService::class.java) // Declaration by var ?

  suspend fun getRests(
    keyid: String, // アクセスキー: BuildConfig.GNAVI_API_KEY,
    range: Int, // Range: 1,
    longitude: Double, // 緯度: 139.6353565,
    latitude: Double // 軽度: 35.6994197
  ): Response<GNaviResponse> {
    println("Lets connecting ...")
    val call = gNaviService.getRests(keyid, range, longitude, latitude)
    val responce: Response<GNaviResponse> = call.execute()
    println("Finish connecting ...")
    return responce
  }

  suspend fun getTest(
    keyid: String, // アクセスキー: BuildConfig.GNAVI_API_KEY,
    range: Int, // Range: 1,
    longitude: Double, // 緯度: 139.6353565,
    latitude: Double // 軽度: 35.6994197
  ): Response<GNaviResponse> {
    println("Lets connecting ...")
    val call = gNaviService.getTest(keyid, range, longitude, latitude)
    val responce: Response<GNaviResponse> = call.execute()
    return responce
  }

  suspend fun getRest(
    keyid: String, // アクセスキー: BuildConfig.GNAVI_API_KEY,
    id: Int // id: 7656770
  ): Response<Rest> {
    val call = gNaviService.getRest(keyid, id)
    val responce: Response<Rest> = call.execute()
    return responce
  }


  // 他の設計も見てみる
  companion object Factory { // TODO: 明示的にFactoryを呼び出すことがなさそうでされば、`companion object` とする
    val instance: GNaviRepository
      @Synchronized get() { // @Synchronized を用いてスレッドセーフにインスタンスを作成する。singleton インスタンスを生成するときのtips
        return GNaviRepository()
      }
  }
}
