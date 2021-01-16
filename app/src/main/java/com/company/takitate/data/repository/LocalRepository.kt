package com.company.takitate.data.repository

import android.content.Context
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.company.takitate.data.repository.driver.MyDatabase
import com.company.takitate.domain.entity.*

class LocalRepository constructor(private val context: Context): MyDatabase() {

  private val reviewDao: ReviewDao by lazy { MyDatabase.getInstance(context).reviewDao() }
  private val reviewerDao: ReviewerDao by lazy { MyDatabase.getInstance(context).reviewerDao() }

  suspend fun getAll(): List<Review> {
     return MyDatabase.getInstance(context).reviewDao().getAll()
  }

  override fun reviewDao(): ReviewDao {
    TODO("Not yet implemented")
  }

  override fun reviewerDao(): ReviewerDao {
    TODO("Not yet implemented")
  }

  override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
    TODO("Not yet implemented")
  }

  override fun createInvalidationTracker(): InvalidationTracker {
    TODO("Not yet implemented")
  }

  override fun clearAllTables() {
    TODO("Not yet implemented")
  }

}
