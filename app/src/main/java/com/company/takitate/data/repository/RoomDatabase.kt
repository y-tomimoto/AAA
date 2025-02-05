package com.company.takitate.data.repository.driver

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.company.takitate.domain.entity.*

@Database(entities = [Review::class, Reviewer::class], version = 4, exportSchema = false)
@TypeConverters(DateTimeConverter::class) // TypeConverterについて: https://qiita.com/tkt989/items/d600cf995a5ea41598f6
abstract class MyDatabase: RoomDatabase() {

  abstract fun reviewDao(): ReviewDao
  abstract fun reviewerDao(): ReviewerDao

  companion object{
    @Volatile
    private var instance: MyDatabase? = null
    private const val databaseName = "RESTAURANT_REVIEW.db"

    val MIGRATION_2_3 = object : Migration(2, 3) {
      override fun migrate(database: SupportSQLiteDatabase) {
        println("calldadadada")
        database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, " +
          "PRIMARY KEY(`id`))")
      }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
      override fun migrate(database: SupportSQLiteDatabase) {
        // Create temporary table
        database.execSQL("CREATE TABLE `reviews_new` (`review_id` TEXT NOT NULL, `reviewer_id` TEXT NOT NULL, `restaurant_id` TEXT NOT NULL, `comment` TEXT NOT NULL, `title` TEXT NOT NULL, `datetime` INTEGER NOT NULL," +
          "PRIMARY KEY(`review_id`))")
        // Copy the data
        database.execSQL(
          "INSERT INTO reviews_new (review_id, reviewer_id, restaurant_id,comment, title, datetime ) SELECT review_id, reviewer_id, restaurant_id,comment,title, datetime  FROM reviews");
        // Remove the old table
        database.execSQL("DROP TABLE reviews");
        // Change the table name to the correct one
        database.execSQL("ALTER TABLE reviews_new RENAME TO reviews");
      }
    }

    // singleton にするためにcompanion: https://blog.mokelab.com/22/room.html
    fun getInstance(context: Context): MyDatabase =
      instance ?: synchronized(this) {
        Room.databaseBuilder(context,
          MyDatabase::class.java, databaseName)
          .build()
      }
  }
}
