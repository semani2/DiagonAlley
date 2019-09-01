package com.sai.diagonalley.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sai.diagonalley.R
import com.sai.diagonalley.adapter.ItemAdapter
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.viewmodel.MainActivityViewModel
import com.sai.diagonalley.viewmodel.livedata.LiveDataWrapper
import com.sai.diagonalley.viewmodel.livedata.ResourceStatus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val viewmodel: MainActivityViewModel by viewModel()

    private val itemList = mutableListOf<ItemEntity>()
    private val itemAdapter = ItemAdapter(itemList)

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initItemClick()
        initLiveDataObservers()

        viewmodel.fetchItems()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    /* Section - Data */
    private fun initLiveDataObservers() {
        viewmodel.itemLiveData.observe(this,
            Observer<LiveDataWrapper<List<ItemEntity>, Exception>> {
                livedataWrapper ->
                when (livedataWrapper.status) {
                    ResourceStatus.LOADING -> toggleBusy(true)

                    ResourceStatus.ERROR -> {
                        toggleBusy(false)
                        Timber.e(livedataWrapper.exception, "Error fetching items")
                        Toast.makeText(this, "Error fetching items", Toast.LENGTH_LONG).show()
                    }

                    ResourceStatus.SUCCESS -> {
                        toggleBusy(false)

                        itemList.clear()
                        itemList.addAll(livedataWrapper.data!!)
                        itemAdapter.notifyDataSetChanged()
                    }
                }
            })
    }

    /* Section - UI Helpers */

    private fun initRecyclerView() {
        item_recycler_view.apply {
            layoutManager = GridLayoutManager(this@MainActivity, calculateNumColumns())
            adapter = itemAdapter
        }
    }

    private fun initItemClick() {
        val disposable = itemAdapter.getClickEvent()
            .subscribeWith(object: DisposableObserver<ItemEntity>() {
                override fun onComplete() {
                    /* no op */
                }

                override fun onNext(item: ItemEntity) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.paramItemId, item.id)
                    startActivity(intent)
                }

                override fun onError(e: Throwable) {
                    /* no op */
                }

            })

        compositeDisposable.add(disposable)
    }

    private fun toggleBusy(isBusy: Boolean) {
        progressBar.visibility = if (isBusy) View.VISIBLE else View.GONE
    }

    private fun calculateNumColumns() : Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / 180 + 0.5).toInt()
    }
}
