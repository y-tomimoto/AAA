package com.company.takitate.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.company.takitate.domain.entity.RecruitAPIResponse
import com.company.takitate.service.WebService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RecruitAPIRepository : WebService {

  private val HTTP_API_RECRUIT_URL = "http://webservice.recruit.co.jp/"

  private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory()) // Moshi for 1.9.0 ~
    .build()

  private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

  private val client = OkHttpClient.Builder().addInterceptor(interceptor).build();

  private val webService = Retrofit.Builder()
    .baseUrl(HTTP_API_RECRUIT_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi)) // Moshi Converter : https://gist.github.com/dangets/66dd7bed4b5babaa8cc6d6b7336ba76a
    .build()
    .create(WebService::class.java)

  override suspend fun getRestaurantsByGeocode(
    key: String,
    lat: Double,
    lng: Double,
    range: Int,
    format: String,
    order: Int,
    count: Int
  ): RecruitAPIResponse {
    return webService.getRestaurantsByGeocode(key=key,lat=lat,lng=lng,range=range,format=format,order=order,count=count)
  }
}
