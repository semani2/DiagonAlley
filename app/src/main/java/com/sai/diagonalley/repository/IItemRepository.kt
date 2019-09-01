package com.sai.diagonalley.repository

import com.sai.diagonalley.data.db.ItemEntity
import io.reactivex.Single

interface IItemRepository {

    fun getItems(useOnlyCache: Boolean): Single<List<ItemEntity>>

    fun getItemById(id: String): Single<List<ItemEntity>>

    fun getItemsByCategory(category: String): Single<List<ItemEntity>>
}
