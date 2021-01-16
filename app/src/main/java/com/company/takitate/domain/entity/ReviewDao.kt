
package com.company.takitate.domain.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReviewDao {
  @Query("SELECT * FROM reviews")
  suspend fun getAll(): List<Review>

  @Query("SELECT * FROM reviews WHERE restaurant_id IN (:restaurant_id)")
  suspend fun getAllByRestaurantId(restaurant_id: String): List<Review>

  @Insert
  suspend fun insertReview(vararg review: Review)

  @Delete
  suspend fun delete(review: Review)
}
