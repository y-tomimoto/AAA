package com.company.takitate.data.repository.driver

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.company.takitate.domain.entity.*

@Database(entities = [Review::class, Reviewer::class], version = 2)
abstract class MyDatabase: RoomDatabase() {

  abstract fun reviewDao(): ReviewDao
  abstract fun reviewerDao(): ReviewerDao

  companion object{
    @Volatile
    private var instance: MyDatabase? = null
    private const val databaseName = "RESTAURANT_REVIEW.db"

    fun getInstance(context: Context): MyDatabase =
      instance ?: synchronized(this) {
        Room.databaseBuilder(context,
          MyDatabase::class.java, databaseName)
          .build()
      }
  }
}
