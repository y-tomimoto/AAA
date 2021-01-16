package com.company.takitate.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ReviewerWithReviews(
  @Embedded val reviewer: Reviewer,
  @Relation(
    parentColumn = "reviewer_id",
    entityColumn = "reviewer_id"
  )
  val reviews: List<Review>
  )
