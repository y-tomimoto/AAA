package io.github.reservationbytom.service.model

data class Access(
    val line: String,
    val note: String,
    val station: String,
    val station_exit: String,
    val walk: String
)