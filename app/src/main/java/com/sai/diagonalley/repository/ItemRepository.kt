package com.sai.diagonalley.repository

import com.google.gson.Gson
import com.sai.diagonalley.data.api.Inventory
import com.sai.diagonalley.data.db.CategoryEntity
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.module.ApiModule
import com.sai.diagonalley.module.DbModule
import com.sai.diagonalley.module.SharedPreferencesModule
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*

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

                if (category != null && !category.equals("all", true)) {
                    val filteredList = itemEntityList.filter { it.category == category}
                    Single.just(filteredList)
                } else {
                    Single.just(itemEntityList)
                }
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

    /**
     * Function fetch categories
     *
     * @param useOnlyCache : Should use only cache
     *
     * @return List of Category Entities
     */
    override fun getCategories(useOnlyCache: Boolean): Single<List<CategoryEntity>> {
        return getCategoriesFromDb()
            .subscribeOn(Schedulers.io())
            .flatMap { dbList ->
                if (dbList.isNullOrEmpty()) {
                    getCategoriesFromApi()
                } else {
                    Single.just(dbList)
                }
            }
    }

    override fun updateCategory(categories: List<CategoryEntity>): Completable {
        return Completable.fromCallable {
            dbModule.getItemDatabase().categoryDao().updateCategories(categories)
        }
    }

    /**
     * Helper method to fetch categories from the API
     *
     * @return List of category entity
     */
    private fun getCategoriesFromApi(): Single<List<CategoryEntity>> {
        return Single.fromCallable { apiModule.readInventoryFromFile() }
            .subscribeOn(Schedulers.io())
            .flatMap { jsonString ->
                val inventory = Gson().fromJson(jsonString, Inventory::class.java)
                val categoryEntityList = mutableListOf<CategoryEntity>()

                for (category in inventory.categories) {
                    val categoryEntity = CategoryEntity(
                        category.id,
                        category.name,
                        category.type,
                        false
                    )
                    categoryEntityList.add(categoryEntity)
                }
                categoryEntityList.add(
                    0,
                    CategoryEntity(
                        UUID.randomUUID().toString(),
                        "All",
                        "all",
                        true
                    )
                )

                dbModule.getItemDatabase().categoryDao().insertAllCategories(categoryEntityList)

                Single.just(categoryEntityList)
            }
    }

    /**
     * Helper method to fetch categories from the db
     *
     * @return List of category entity
     */
    private fun getCategoriesFromDb(): Single<List<CategoryEntity>> {
        return dbModule.getItemDatabase().categoryDao().getCategories()
    }
}
