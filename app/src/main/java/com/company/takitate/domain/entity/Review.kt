package com.company.takitate.domain.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import java.sql.Timestamp

@Entity(tableName = "reviews")
data class Review(
  @PrimaryKey
  val review_id: String,
  val reviewer_id: String,
  val restaurant_id: String,
  val comment: String,
  val title: String,
  val datetime: DateTime
)
