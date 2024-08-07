package com.example.neostart.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import com.example.neostart.model.Info

@Dao
interface InfoDao {

    @Insert
    suspend fun insert(entity: Info)

    @Query("SELECT * FROM info WHERE id = :id")
    suspend fun getById(id: Int): Info?
}