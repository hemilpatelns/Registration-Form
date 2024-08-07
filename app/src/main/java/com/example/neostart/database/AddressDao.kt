package com.example.neostart.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import com.example.neostart.model.Address

@Dao
interface AddressDao {

    @Insert
    suspend fun insert(entity: Address)

    @Query("SELECT * FROM address WHERE id = :id")
    suspend fun getById(id: Int): Address?
}