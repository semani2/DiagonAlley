package com.sai.diagonalley.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface ItemDao {

    @Query("SELECT * from items")
    fun getItems(): Single<List<ItemEntity>>

    @Query("SELECT * from items where id = :id")
    fun getItemById(id: String): Single<List<ItemEntity>>

    @Query("SELECT * FROM items where category = :category")
    fun getItemsByCategory(category: String): Single<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllItems(items: List<ItemEntity>)
}
