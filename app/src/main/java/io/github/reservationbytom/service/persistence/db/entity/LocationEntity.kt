package io.github.reservationbytom.service.persistence.db.entity

import androidx.room.*
import java.util.*

// Entityでは、プリミティブ型しか受け付けていない。その他の型はConverterを活用する: https://qiita.com/tkt989/items/d600cf995a5ea41598f6
class Converters {
  @TypeConverter
  fun fromTimestamp(value: Long?): Date? {
    return value?.let { Date(it) }
  }

  @TypeConverter
  fun dateToTimestamp(date: Date?): Long? {
    return date?.time?.toLong()
  }
}

@Entity
data class Location(
  // @TypeConverters(Converters::class)
  @PrimaryKey val uuid: String,
  @ColumnInfo(name = "longitude") val longitude: Double?,
  @ColumnInfo(name = "latitude") val latitude: Double?,
  @ColumnInfo(name = "timestamp") val timestamp: String?, // TODO: TypeConverterを採用
)
