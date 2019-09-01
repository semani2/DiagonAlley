package com.sai.diagonalley.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.module.ConnectivityModule
import com.sai.diagonalley.repository.IItemRepository
import com.sai.diagonalley.viewmodel.livedata.LiveDataWrapper
import com.sai.diagonalley.viewmodel.livedata.ResourceStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class DetailActivityViewModel(private val repository: IItemRepository) : DAViewModel() {

    val itemLiveData: MutableLiveData<LiveDataWrapper<ItemEntity, Exception>> by lazy {
        MutableLiveData<LiveDataWrapper<ItemEntity, Exception>>()
    }

    fun fetchItem(id: String) {
        itemLiveData.value = LiveDataWrapper(
            ResourceStatus.LOADING,
            null,
            null
        )

        val disposable = repository.getItemById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<List<ItemEntity>>() {
                override fun onSuccess(data: List<ItemEntity>) {
                    if (data.isEmpty() || data.size > 1) {
                        itemLiveData.value = LiveDataWrapper(
                            ResourceStatus.ERROR,
                            null,
                            Exception("Error fetching item with id $id")
                        )
                        return
                    }

                    itemLiveData.value = LiveDataWrapper(
                        ResourceStatus.SUCCESS,
                        data[0],
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
}
