package io.github.reservationbytom.service.persistence.db.entity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Location::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun locationDao(): LocationDao
}

