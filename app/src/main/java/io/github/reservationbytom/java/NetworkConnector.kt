package io.github.reservationbytom.java

import android.util.Log
import com.google.gson.GsonBuilder
import io.github.reservationbytom.BuildConfig
import io.github.reservationbytom.LoginActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException


object Model {
    data class Result(val rest: Rest)
    data class Rest(val restaurants: ArrayList<GnaviRestaurant>)
    data class GnaviRestaurant(
        val latitude: Int,
        val longitude: Int,
        val id: Int,
        val update_date: String
    )
}


interface GnaviAPIService {
    @GET("RestSearchAPI/v3/")
    fun getRestaurants(
        @Query("keyid") keyid: String,
        @Query("range") range: Int,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    // ): Call<Model.GnaviRestaurant>
    ): Call<ResponseBody>
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
            "XXXX",
            1,
            139.6353565,
            35.6994197
        )
        val result = ddd.execute().body()
        println(result)
        println("fin")
    }
}
