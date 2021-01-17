package com.company.takitate.data.repository.driver

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.company.takitate.domain.entity.*

@Database(entities = [Review::class, Reviewer::class], version = 2)
@TypeConverters(DateTimeConverter::class) // TypeConverterについて: https://qiita.com/tkt989/items/d600cf995a5ea41598f6
abstract class MyDatabase: RoomDatabase() {

  abstract fun reviewDao(): ReviewDao
  abstract fun reviewerDao(): ReviewerDao

  companion object{
    @Volatile
    private var instance: MyDatabase? = null
    private const val databaseName = "RESTAURANT_REVIEW.db"

    // singleton にするためにcompanion: https://blog.mokelab.com/22/room.html
    fun getInstance(context: Context): MyDatabase =
      instance ?: synchronized(this) {
        Room.databaseBuilder(context,
          MyDatabase::class.java, databaseName)
          .build()
      }
  }
}
