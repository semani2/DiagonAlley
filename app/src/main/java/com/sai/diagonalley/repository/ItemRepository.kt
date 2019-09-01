package com.sai.diagonalley.repository

import com.google.gson.Gson
import com.sai.diagonalley.data.api.Inventory
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.module.ApiModule
import com.sai.diagonalley.module.DbModule
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the IItemRepository.
 *
 * @param apiModule
 * @param dbModule
 */
class ItemRepository(val apiModule: ApiModule, val dbModule: DbModule) : IItemRepository {

    /**
     * Function to fetch items from inventory
     *
     * @param useOnlyCache: Boolean flag to decide whether to only use cache or not
     * @param category: Category filter for items
     *
     * @return list of ItemEntity
     *
     * @see ItemEntity
     */
    override fun getItems(useOnlyCache: Boolean, category: String?): Single<List<ItemEntity>> {
        return if (useOnlyCache)
            getItemsFromDb(category)
        else
            getItemsFromApi(category)
    }

    /**
     * Function to fetch item by ID from the database
     *
     * @param id: Item id
     *
     * @return list of ItemEntity
     *
     * @see ItemEntity
     */
    override fun getItemById(id: String): Single<List<ItemEntity>>
            = dbModule.getItemDatabase().itemDao().getItemById(id)

    /**
     * Helper method to fetch items from the API
     *
     * @param category: Category filter for items
     *
     * @return list of ItemEntity
     */
    private fun getItemsFromApi(category: String?): Single<List<ItemEntity>> {
        return Single.fromCallable { apiModule.readInventoryFromFile() }
            .subscribeOn(Schedulers.io())
            .flatMap { jsonString ->
                val inventory = Gson().fromJson(jsonString, Inventory::class.java)
                val itemEntityList = mutableListOf<ItemEntity>()

                for (item in inventory.items) {
                    val itemEntity = ItemEntity(
                        item.id,
                        item.category,
                        item.displayName,
                        item.description,
                        item.isForRent,
                        item.rentalPeriod,
                        item.imageUrl,
                        item.purchaseCost,
                        item.rentalCost,
                        item.currency
                    )
                    itemEntityList.add(itemEntity)
                }
                dbModule.getItemDatabase().itemDao().insertAllTodos(itemEntityList)

                if (category != null) {
                    itemEntityList.filter { item -> item.category.equals(category, true) }
                }
                Single.just(itemEntityList)
            }
    }

    /**
     * Helper method to fetch items from the database
     *
     * @param category: Category filter for items
     *
     * @return list of item entity
     */
    private fun getItemsFromDb(category: String?): Single<List<ItemEntity>> {
        return if (category == null) {
            dbModule.getItemDatabase().itemDao()
                .getItems()
        } else {
            dbModule.getItemDatabase().itemDao()
                .getItemsByCategory(category)
        }
    }
}
