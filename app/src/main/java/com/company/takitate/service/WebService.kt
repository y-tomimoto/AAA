package com.company.takitate.service

import com.company.takitate.domain.entity.Results
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
  @GET("hotpepper/gourmet/v1")
  fun getRestaurantsByGeocode(
    @Query("key") key:String,
    @Query("lat") lat:Double,
    @Query("lng") lng:Double,
    @Query("range") range:Int
  ): Call<Results>
}
