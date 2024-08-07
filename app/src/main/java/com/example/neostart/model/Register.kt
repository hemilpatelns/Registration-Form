package com.example.neostart.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "register")
data class Register(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val gender: String,
    val password: String,
    val confirmPassword: String
)