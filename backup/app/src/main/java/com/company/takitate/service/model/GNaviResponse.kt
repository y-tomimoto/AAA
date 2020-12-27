package com.company.takitate.service.model

import com.google.gson.annotations.SerializedName

data class GNaviResponse(
    @SerializedName("@attributes")
    val attributes: Attributes,
    val hit_per_page: Int,
    val page_offset: Int,
    val rest: List<Rest>,
    val total_hit_count: Int
)
