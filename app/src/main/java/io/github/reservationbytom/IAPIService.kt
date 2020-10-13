package io.github.reservationbytom

import retrofit2.Call
import retrofit2.http.GET

data class ResponseGate(var firstname: String, var lastname: String, var age: Int)


interface IAPIService {
    @GET("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyCZBwJSll7JDlToNYO5uDuL3AREesXuxQo")
    fun apiDemo(): Call<ResponseGate>
}