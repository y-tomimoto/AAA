package io.github.reservationbytom.service.persistence.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.sql.Date

@Entity
data class Location(
  @ColumnInfo(name = "longitude") val longitude: Double?,
  @ColumnInfo(name = "latitude") val latitude: Double?,
  @ColumnInfo(name = "creation_date")
  @SerializedName(value = "creation_date")
  var creationDate: Date = Date(System.currentTimeMillis())
)
