package com.example.neostart.repository

import androidx.room.Dao
import androidx.room.Entity
import com.example.neostart.database.AddressDao
import com.example.neostart.database.InfoDao
import com.example.neostart.database.RegisterDao
import com.example.neostart.model.Address
import com.example.neostart.model.Info
import com.example.neostart.model.Register

class UserRepository(
    private val registerDao: RegisterDao,
    private val infoDao: InfoDao,
    private val addressDao: AddressDao
) {

    suspend fun insert(register: Register, info: Info, address: Address) {
        registerDao.insert(register)
        infoDao.insert(info)
        addressDao.insert(address)
    }
}