package com.sai.diagonalley.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemEntity::class, CategoryEntity::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun categoryDao(): CategoryDao
}
