package io.github.reservationbytom.service.model

data class Code(
    val areacode: String,
    val areacode_s: String,
    val areaname: String,
    val areaname_s: String,
    val category_code_l: List<String>,
    val category_code_s: List<String>,
    val category_name_l: List<String>,
    val category_name_s: List<String>,
    val prefcode: String,
    val prefname: String
)