package com.sai.diagonalley.data.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CategoryDao {

    @Query("SELECT * from categories")
    fun getCategories(): Single<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategories(categories: List<CategoryEntity>)

    @Update
    fun updateCategories(categories: List<CategoryEntity>)

    @Query("DELETE from categories")
    fun deleteAllCategories()
}
