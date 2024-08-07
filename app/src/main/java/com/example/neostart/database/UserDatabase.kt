package com.example.neostart.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.neostart.model.Address
import com.example.neostart.model.Info
import com.example.neostart.model.Register

@Database(entities = [Register::class, Info::class, Address::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun registerDao(): RegisterDao
    abstract fun infoDao(): InfoDao
    abstract fun addressDao(): AddressDao

    companion object{
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase{
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}