package io.github.reservationbytom.service.persistence.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.sql.Date

@Entity
data class Location(
  @PrimaryKey(autoGenerate = true) val id: Int,
  @ColumnInfo(name = "longitude") val longitude: String?,
  @ColumnInfo(name = "latitude") val latitude: String?,
  @ColumnInfo(name = "creation_date")
  @SerializedName(value = "creation_date")
  var creationDate: Date = Date(System.currentTimeMillis())
)
