package com.company.takitate.service

import com.company.takitate.domain.entity.RecruitAPIResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
  @GET("hotpepper/gourmet/v1")
  suspend fun getRestaurantsByGeocode(
    @Query("key") key:String,
    @Query("lat") lat:Double,
    @Query("lng") lng:Double,
    @Query("range") range:Int,
    @Query("format") format:String,
    @Query("order") order:Int,
    @Query("count") count:Int,
  ): RecruitAPIResponse
}
