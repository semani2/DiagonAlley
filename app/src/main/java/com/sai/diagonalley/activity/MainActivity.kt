package com.sai.diagonalley.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sai.diagonalley.R
import com.sai.diagonalley.adapter.CategoryAdapter
import com.sai.diagonalley.adapter.ItemAdapter
import com.sai.diagonalley.data.db.CategoryEntity
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.module.ConnectivityModule
import com.sai.diagonalley.module.SharedPreferencesModule
import com.sai.diagonalley.viewmodel.MainActivityViewModel
import com.sai.diagonalley.viewmodel.livedata.LiveDataWrapper
import com.sai.diagonalley.viewmodel.livedata.ResourceStatus
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : DAActivity() {

    val viewmodel: MainActivityViewModel by viewModel()

    private val itemList = mutableListOf<ItemEntity>()
    private val itemAdapter = ItemAdapter(itemList)

    private val categoryList = mutableListOf<CategoryEntity>()
    private val categoryAdapter = CategoryAdapter(this, categoryList)

    private val sharedPreferencesModule: SharedPreferencesModule by inject()
    private val connetivityModule: ConnectivityModule by inject()

    private var filterDialog: AlertDialog? = null

    /* Section - LifeCycle Methods */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initSwipeToRefresh()
        initItemClick()
        initCategoryClickEvent()
        initLiveDataObservers()

        viewmodel.fetchItems(sharedPreferencesModule.getString(SharedPreferencesModule.spFilterKey,
            SharedPreferencesModule.defaultFilter))
    }

    override fun onDestroy() {
        viewmodel.scrollPosition = (item_recycler_view.layoutManager as GridLayoutManager)
            .findFirstCompletelyVisibleItemPosition()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_action_filter -> {
                menuFilterClicked()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /* Section - Data */
    private fun initLiveDataObservers() {
        viewmodel.itemLiveData.observe(this,
            Observer<LiveDataWrapper<List<ItemEntity>, Exception>> {
                livedataWrapper ->
                when (livedataWrapper.status) {
                    ResourceStatus.LOADING -> toggleBusy(true)

                    ResourceStatus.ERROR -> {
                        item_swipe_refresh_layout.isRefreshing = false
                        toggleBusy(false)
                        Timber.e(livedataWrapper.exception, "Error fetching items")

                        displayItemsError()
                        item_swipe_refresh_layout.isRefreshing = false
                    }

                    ResourceStatus.SUCCESS -> {
                        item_swipe_refresh_layout.isRefreshing = false
                        toggleBusy(false)

                        if (livedataWrapper.data.isNullOrEmpty()) {
                            displayItemsError()
                            return@Observer
                        }

                        itemList.clear()
                        itemList.addAll(livedataWrapper.data)
                        itemAdapter.notifyDataSetChanged()

                        item_recycler_view.visibility = View.VISIBLE
                        empty_list_text_view.visibility = View.GONE
                        empty_list_image_view.visibility = View.GONE

                        item_recycler_view.smoothScrollToPosition(viewmodel.scrollPosition)
                    }
                }
            })

        viewmodel.categoryLiveData.observe(this,
            Observer<LiveDataWrapper<List<CategoryEntity>, Exception>> { livedataWrapper ->
                if (livedataWrapper == null) {
                    return@Observer
                }
                when (livedataWrapper.status) {
                    ResourceStatus.LOADING -> toggleBusy(true)

                    ResourceStatus.ERROR -> {
                        toggleBusy(false)
                        Timber.e(livedataWrapper.exception, "Error fetching categories")
                        Toast.makeText(
                            this,
                            "Could not fetch categories, please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    ResourceStatus.SUCCESS -> {
                        toggleBusy(false)

                        if (livedataWrapper.data.isNullOrEmpty()) {
                            Timber.e("Error fetching categories")
                            Toast.makeText(
                                this,
                                "Could not fetch categories, please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Observer
                        }

                        categoryList.clear()
                        categoryList.addAll(livedataWrapper.data)
                        displayFilterDialog(categoryList)
                    }
                }
            })
    }

    private fun displayItemsError() {
        item_recycler_view.visibility = View.GONE
        empty_list_image_view.visibility = View.VISIBLE
        if (!connetivityModule.isNetworkAvailable()) {
            empty_list_text_view.text = getString(R.string.str_empty_list_no_network)
        }
        empty_list_text_view.visibility = View.VISIBLE
    }

    /* Section - UI Helpers */

    private fun initRecyclerView() {
        item_recycler_view.apply {
            layoutManager = GridLayoutManager(this@MainActivity, calculateNumColumns())
            adapter = itemAdapter
        }
    }

    private fun initSwipeToRefresh() {
        item_swipe_refresh_layout.setOnRefreshListener {
            viewmodel.scrollPosition = 0
            viewmodel.fetchItems(sharedPreferencesModule.getString(SharedPreferencesModule.spFilterKey,
                SharedPreferencesModule.defaultFilter))
        }
    }

    private fun initItemClick() {
        val disposable = itemAdapter.getClickEvent()
            .subscribeWith(object: DisposableObserver<ItemAdapter.ItemImageData>() {
                override fun onComplete() {
                    /* no op */
                }

                override fun onNext(itemData: ItemAdapter.ItemImageData) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.paramItemId, itemData.item.id)
                    intent.putExtra(DetailActivity.paramAnimationName, ViewCompat.getTransitionName(itemData.imageView))
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity,
                        itemData.imageView,
                        ViewCompat.getTransitionName(itemData.imageView)!!
                    )

                    startActivity(intent, options.toBundle())
                }

                override fun onError(e: Throwable) {
                    /* no op */
                }

            })

        compositeDisposable.add(disposable)
    }

    private fun initCategoryClickEvent() {
        val disposable = categoryAdapter.getClickEvent()
            .subscribeWith(object: DisposableObserver<List<CategoryEntity>>() {
                override fun onComplete() {
                    /* no op */
                }

                override fun onNext(item: List<CategoryEntity>) {
                    viewmodel.updateCategories(item)
                    filterDialog?.dismiss()

                    val category = item.find { it.isSelected }
                    category?.let {
                        sharedPreferencesModule.putString(SharedPreferencesModule.spFilterKey, it.type)
                        viewmodel.fetchItems(it.type)
                    }
                }

                override fun onError(e: Throwable) {
                    /* no op */
                }

            })

        compositeDisposable.add(disposable)
    }

    private fun menuFilterClicked() {
        if (categoryList.isNullOrEmpty()) {
            viewmodel.fetchCategories()
            return
        }

        displayFilterDialog(categoryList)
    }

    private fun displayFilterDialog(list: List<CategoryEntity>) {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_filter, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)

        val categoryListView: ListView = dialogView.findViewById(R.id.filter_list_view)
        categoryListView.adapter = categoryAdapter

        categoryAdapter.notifyDataSetChanged()

        filterDialog = dialogBuilder.create()
        filterDialog?.show()
    }

    private fun toggleBusy(isBusy: Boolean) {
        if (item_swipe_refresh_layout.isRefreshing) {
            return
        }

        progressBar.visibility = if (isBusy) View.VISIBLE else View.GONE
    }

    private fun calculateNumColumns() : Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / 180 + 0.5).toInt()
    }
}
