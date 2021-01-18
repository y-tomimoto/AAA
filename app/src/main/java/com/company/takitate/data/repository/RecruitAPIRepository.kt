package com.company.takitate.data.repository

import com.company.takitate.domain.entity.RecruitAPIResponse
import com.company.takitate.service.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RecruitAPIRepository: WebService {

  private val HTTP_API_RECRUIT_URL = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1"

  private val webService = Retrofit.Builder()
    .baseUrl(HTTP_API_RECRUIT_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
    .create(WebService::class.java)

  override suspend fun getRestaurantsByGeocode(
    key: String,
    lat: Double,
    lng: Double,
    range: Int
  ): RecruitAPIResponse {
    // Callbackを引数として受け取っている
    return webService.getRestaurantsByGeocode(key=key,lat=lat,lng=lng,range=range)
  }
}
