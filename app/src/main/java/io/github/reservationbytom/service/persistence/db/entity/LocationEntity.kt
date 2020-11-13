package io.github.reservationbytom.service.persistence.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Location(
  @ColumnInfo(name = "longitude") val longitude: String?,
  @ColumnInfo(name = "latitude") val latitude: String?
)
