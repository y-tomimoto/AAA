
package com.company.takitate.domain.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReviewerDao {
  @Query("SELECT * FROM reviewers")
  suspend fun getAll(): List<Reviewer>

  @Insert
  suspend fun insertReviewer(vararg reviewer: Reviewer)

  @Delete
  suspend fun delete(reviewer: Reviewer)
}
