package com.sai.diagonalley.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sai.diagonalley.data.db.CategoryEntity
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.module.ConnectivityModule
import com.sai.diagonalley.repository.IItemRepository
import com.sai.diagonalley.viewmodel.livedata.LiveDataWrapper
import com.sai.diagonalley.viewmodel.livedata.ResourceStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * ViewModel for the DAActivity
 *
 * @see DAActivity
 */
class DAActivityViewModel(private val repository: IItemRepository,
                          private val connectivityModule: ConnectivityModule) : DAViewModel() {



    val itemLiveData: MutableLiveData<LiveDataWrapper<List<ItemEntity>, Exception>> by lazy {
        MutableLiveData<LiveDataWrapper<List<ItemEntity>, Exception>>()
    }

    val categoryLiveData: MutableLiveData<LiveDataWrapper<List<CategoryEntity>, Exception>> by lazy {
        MutableLiveData<LiveDataWrapper<List<CategoryEntity>, Exception>>()
    }

    private var filterCategory: String? = null
    var scrollPosition = 0

    /**
     * Method to fetch items from the repository
     *
     * @param category: Category to filter items
     */
    fun fetchItems(category: String?) {
        if (category.equals(filterCategory, true) && itemLiveData.value != null
            && itemLiveData.value?.status == ResourceStatus.SUCCESS) {
            itemLiveData.value = LiveDataWrapper(
                ResourceStatus.SUCCESS,
                itemLiveData.value?.data,
                null
            )
            return
        }

        filterCategory = category

        itemLiveData.value = LiveDataWrapper(
            ResourceStatus.LOADING,
            null,
            null
        )

        val disposable = repository.getItems(!connectivityModule.isNetworkAvailable(), category)
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<List<ItemEntity>>() {
                override fun onSuccess(data: List<ItemEntity>) {
                    if (data.isNullOrEmpty()) {
                        itemLiveData.value = LiveDataWrapper(
                            ResourceStatus.ERROR,
                            null,
                            Exception("Error fetching items")
                        )
                        return
                    }
                    itemLiveData.value = LiveDataWrapper(
                        ResourceStatus.SUCCESS,
                        data,
                        null
                    )
                }

                override fun onError(e: Throwable) {
                    itemLiveData.value = LiveDataWrapper(
                        ResourceStatus.ERROR,
                        null,
                        Exception(e.localizedMessage)
                    )
                }

            })

        compositeDisposable.add(disposable)
    }

    /**
     * Method to fetch categories from the repository
     */
    fun fetchCategories() {
        categoryLiveData.value = LiveDataWrapper(
            ResourceStatus.LOADING,
            null,
            null
        )

        val disposable = repository.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<List<CategoryEntity>>() {
                override fun onSuccess(data: List<CategoryEntity>) {
                    categoryLiveData.value = LiveDataWrapper(
                        ResourceStatus.SUCCESS,
                        data,
                        null
                    )
                }

                override fun onError(e: Throwable) {
                    categoryLiveData.value = LiveDataWrapper(
                        ResourceStatus.ERROR,
                        null,
                        Exception(e.localizedMessage)
                    )
                }
            })

        compositeDisposable.add(disposable)
    }

    /**
     * Method to update the categories using the repository
     *
     * @param categories: Updated list of categories
     *
     * @see CategoryEntity
     */
    fun updateCategories(categories: List<CategoryEntity>) {
        val disposable = repository.updateCategory(categories)
            .subscribeOn(Schedulers.io())
            .subscribe()

        compositeDisposable.add(disposable)
    }
}
