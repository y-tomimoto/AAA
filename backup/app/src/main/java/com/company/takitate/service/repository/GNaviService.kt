package com.company.takitate.service.repository

import com.company.takitate.service.model.GNaviResponse
import com.company.takitate.service.model.Rest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface GNaviService {
  @GET("RestSearchAPI/v3/")
  suspend fun getRests(
    @Query("keyid") keyid: String,
    @Query("range") range: Int,
    @Query("longitude") longitude: Double,
    @Query("latitude") latitude: Double
  ): GNaviResponse // Call<GNaviResponse> ?

  @GET("RestSearchAPI/v3/")
  suspend fun getTest(
    @Query("keyid") keyid: String,
    @Query("range") range: Int,
    @Query("longitude") longitude: Double,
    @Query("latitude") latitude: Double
  ): Call<GNaviResponse> // Call<GNaviResponse> ?

  @GET("RestSearchAPI/v3/")
  suspend fun getRest(
    @Query("keyid") keyid: String,
    @Query("id") id: Int
  ): Call<Rest> // Call<GNaviResponse> ?
}
