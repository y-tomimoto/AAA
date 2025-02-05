package com.company.takitate.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "reviewers")
data class Reviewer(
    @PrimaryKey(autoGenerate = true) // TODO: 後にautoGenerateを外す
    val reviewer_id: Int,
    val birthday: ZonedDateTime?,
    val handle: String
)
