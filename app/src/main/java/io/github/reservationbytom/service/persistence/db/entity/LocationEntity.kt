package io.github.reservationbytom.service.persistence.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
  @PrimaryKey val uid: Int,
  @ColumnInfo(name = "longitude") val longitude: String?,
  @ColumnInfo(name = "latitude") val latitude: String?
)
