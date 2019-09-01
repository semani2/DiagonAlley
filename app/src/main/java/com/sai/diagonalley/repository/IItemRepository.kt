package com.sai.diagonalley.repository

import com.sai.diagonalley.data.db.CategoryEntity
import com.sai.diagonalley.data.db.ItemEntity
import io.reactivex.Single

interface IItemRepository {

    fun getItems(useOnlyCache: Boolean, category: String?): Single<List<ItemEntity>>

    fun getItemById(id: String): Single<List<ItemEntity>>

    fun getCategories(useOnlyCache: Boolean): Single<List<CategoryEntity>>
}
