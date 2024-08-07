package com.example.neostart.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import com.example.neostart.model.Register

@Dao
interface RegisterDao {

    @Insert
    suspend fun insert(entity: Register)

    @Query("SELECT * FROM register WHERE id = :id")
    suspend fun getById(id: Int): Register?
}