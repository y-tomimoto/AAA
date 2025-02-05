package com.company.takitate.domain.entity

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): ZonedDateTime? { // なぜjodaを採用するか? https://qiita.com/tomoima525/items/6d41093faf5df8bf5b77
        return if (value == null) null else Instant.ofEpochMilli(value)
            .atZone(ZoneId.of("Asia/Tokyo"));


    }

    @TypeConverter
    fun toTimestamp(date: ZonedDateTime?): Long? {
        return date?.toInstant()?.toEpochMilli()
    }
}
