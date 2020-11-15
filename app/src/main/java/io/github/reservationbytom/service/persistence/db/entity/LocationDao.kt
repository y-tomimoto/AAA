package io.github.reservationbytom.service.persistence.db.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
  @Query("SELECT * FROM location")
  fun getAll(): List<Location>

  @Query("SELECT * FROM location WHERE uuid IN (:locationIds)")
  fun loadAllByIds(locationIds: IntArray): List<Location>

  @Insert
  fun insertAll(vararg location: Location)

  @Delete
  fun delete(location: Location)

}
