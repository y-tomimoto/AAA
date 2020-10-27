package io.github.reservationbytom.service.repository

import com.google.gson.GsonBuilder
import io.github.reservationbytom.service.model.GNaviResponse
import io.github.reservationbytom.service.model.Rest
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HTTPS_API_GNAVI_URL = "https://api.gnavi.co.jp/"

class GNaviRepository {
    private val retrofit: Retrofit = Retrofit.Builder()
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
    ): Response<GNaviResponse> = gNaviService.getRests(keyid, range, longitude, latitude)

    suspend fun getRest(
        keyid: String, // アクセスキー: BuildConfig.GNAVI_API_KEY,
        id: Int // id: 7656770
    ): Response<Rest> = gNaviService.getRest(keyid, id)


    // 他の設計も見てみる
    companion object Factory { // TODO: 明示的にFactoryを呼び出すことがなさそうでされば、`companion object` とする
        val instance: GNaviRepository
            @Synchronized get() { // @Synchronized を用いてスレッドセーフにインスタンスを作成する。singleton インスタンスを生成するときのtips
                return GNaviRepository()
            }
    }
}