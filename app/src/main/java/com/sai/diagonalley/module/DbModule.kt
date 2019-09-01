package com.sai.diagonalley.module

import android.app.Application
import androidx.room.Room
import com.sai.diagonalley.data.db.ItemDatabase

/**
 * This class is responsible for building the Room database
 */
class DbModule(private val application: Application) {

    /**
     * Returns the Item Database instance
     *
     * @return instance of ItemDatabase
     * @see ItemDatabase
     */
    fun getItemDatabase(): ItemDatabase =
        Room.databaseBuilder(application, ItemDatabase::class.java, "items.db")
            .fallbackToDestructiveMigration()
            .build()
}
