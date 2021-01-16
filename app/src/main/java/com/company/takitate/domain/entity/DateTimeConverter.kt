package com.company.takitate.domain.entity

import androidx.room.TypeConverter
import org.joda.time.DateTime

class DateTimeConverter {
  @TypeConverter
  fun fromTimestamp(value: Long?): DateTime? { // なぜjodaを採用するか? https://qiita.com/tomoima525/items/6d41093faf5df8bf5b77
    return if (value == null) null else DateTime(value)
  }

  @TypeConverter
  fun toTimestamp(date: DateTime?): Long? {
    return date?.toInstant()?.millis
  }
}
