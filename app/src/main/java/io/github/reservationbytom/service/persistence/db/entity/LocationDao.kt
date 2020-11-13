package io.github.reservationbytom.service.persistence.db.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
  @Query("SELECT * FROM location")
  fun getAll(): List<Location>

  @Query("SELECT * FROM location WHERE uid IN (:userIds)")
  fun loadAllByIds(userIds: IntArray): List<Location>

  @Query("SELECT * FROM location WHERE first_name LIKE :first AND " +
    "last_name LIKE :last LIMIT 1")
  fun findByName(first: String, last: String): Location

  @Insert
  fun insertAll(vararg location: Location)

  @Delete
  fun delete(location: Location)
}
