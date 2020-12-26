package com.company.takitate.service.model

import com.google.gson.annotations.SerializedName

data class Rest(
    @SerializedName("@attributes")
    val attributes: AttributesX,
    val access: Access,
    val address: String,
    val budget: Any,
    val category: String,
    val code: Code,
    val coupon_url: CouponUrl,
    val credit_card: String,
    val e_money: String,
    val fax: String,
    val flags: Flags,
    val holiday: String,
    val id: String,
    val image_url: ImageUrl,
    val latitude: String,
    val longitude: String,
    val lunch: Any,
    val name: String,
    val name_kana: String,
    val opentime: String,
    val parking_lots: String,
    val party: Any,
    val pr: Pr,
    val tel: String,
    val tel_sub: String,
    val update_date: String,
    val url: String,
    val url_mobile: String
)
