package io.github.reservationbytom.java

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import io.github.reservationbytom.BuildConfig
import io.github.reservationbytom.LoginActivity
import io.github.reservationbytom.model.Attributes
import io.github.reservationbytom.model.GNaviResponse
import io.github.reservationbytom.model.Rest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

interface GnaviAPIService {
    @GET("RestSearchAPI/v3/")
    fun getRestaurants(
        @Query("keyid") keyid: String,
        @Query("range") range: Int,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ): Call<GNaviResponse>
    // ): Call<Model.GnaviRes>
}

class NetworkConnector {

    fun create(): GnaviAPIService {

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .baseUrl("https://api.gnavi.co.jp/")
            .build()

        return retrofit.create(GnaviAPIService::class.java)
    }


    fun call_api() {

        println("runnning ...")

        val bbb: GnaviAPIService = create()
        val ddd = bbb.getRestaurants(
            BuildConfig.GNAVI_API_KEY,
            1,
            139.6353565,
            35.6994197
        )
        val result = ddd.execute().body()
        println(result)
        println("fin")
    }
}
