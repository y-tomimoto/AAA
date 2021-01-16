package com.company.takitate.data.repository

import android.content.Context
import com.company.takitate.data.repository.driver.MyDatabase
import com.company.takitate.domain.entity.*

class LocalRepository constructor(private val context: Context) {

  private val reviewDao: ReviewDao by lazy { MyDatabase.getInstance(context).reviewDao() }
  private val reviewerDao: ReviewerDao by lazy { MyDatabase.getInstance(context).reviewerDao() }

  suspend fun getAll(): List<Review> {
     return MyDatabase.getInstance(context).reviewDao().getAll()
  }

}
