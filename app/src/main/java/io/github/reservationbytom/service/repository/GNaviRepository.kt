package io.github.reservationbytom.service.repository

import com.google.gson.GsonBuilder
import io.github.reservationbytom.service.model.GNaviResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HTTPS_API_GNAVI_URL = "https://api.gnavi.co.jp/"

class GNaviRepository {
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().serializeNulls().create()
            )
        )
        .baseUrl(HTTPS_API_GNAVI_URL)
        .build()

    private val gNaviService = retrofit.create(GNaviService::class.java) // Declaration by var ?

    suspend fun getRestaurants(
        keyid: String, // アクセスキー: BuildConfig.GNAVI_API_KEY,
        range: Int, // Range: 1,
        longitude: Double, // 緯度: 139.6353565,
        latitude: Double // 軽度: 35.6994197
    ): Response<GNaviResponse> = gNaviService.getRestaurants(keyid, range, longitude, latitude)

    companion object Factory { // TODO: 明示的にFactoryを呼び出すことがなさそうでされば、`companion object` とする
        val instance: GNaviRepository
            @Synchronized get() { // @Synchronized を用いてスレッドセーフにインスタンスを作成する。singleton インスタンスを生成するときのtips
                return GNaviRepository()
            }
    }

}