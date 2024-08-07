package com.example.neostart.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "info")
data class Info(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val education: String,
    val yearOfPassing: String,
    val grade: String,
    val experience: String,
    val designation: String,
    val domain: String
)
