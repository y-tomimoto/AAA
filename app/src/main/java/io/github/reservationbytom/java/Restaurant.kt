package io.github.reservationbytom.java

import com.google.gson.annotations.SerializedName

data class GnaviResponse(
    @SerializedName("@attributes")
    val attributes: String,
    val total_hit_count: Int,
    val hit_per_page: Int,
    val page_offset: Int,
    val rest: ArrayList<GnaviRestaurant>
)

data class CouponUrl(
    val pc: String,
    val mobile: String
)

data class ImageUrl(
    val shop_image1: String,
    val shop_image2: String,
    val qrcode: String
)

data class Access(
    val line: String,
    val station: String,
    val station_exit: String,
    val walk: String,
    val note: String
)

data class Pr(
    val pr_short: String,
    val pr_long: String
)

data class Code(
    val areacode: String,
    val areaname: String,
    val prefcode: String,
    val prefname: String,
    val areacode_s: String,
    val areaname_s: String,
    val category_code_l: ArrayList<String>,
    val category_name_l: ArrayList<String>,
    val category_code_s: ArrayList<String>,
    val category_name_s: ArrayList<String>
)

data class Flag(
    val mobile_site: Int,
    val mobile_coupon: Int,
    val pc_coupon: Int
)

data class GnaviRestaurant(
    val id: Int,
    val update_date: String,
    val name: String,
    val name_kana: String,
    val latitude: Int,
    val longitude: Int,
    val category: String,
    val url: String,
    val url_mobile: String,
    val coupon_url: CouponUrl,
    val image_url: ImageUrl,
    val address: String,
    val tel: String,
    val tel_sub: String,
    val fax: String,
    val opentime: String,
    val holiday: String,
    val access: Access,
    val line: String,
    val station: String,
    val station_exit: String,
    val walk: String,
    val note: String,
    val parking_lots: String,
    val pr: Pr,
    val code: Code,
    val budget: String,
    val party: String,
    val lunch: String,
    val credit_card: String,
    val e_money: String,
    val flags: Flag
)
