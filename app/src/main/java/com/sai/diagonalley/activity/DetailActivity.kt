package com.sai.diagonalley.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sai.diagonalley.GlideApp
import com.sai.diagonalley.R
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.viewmodel.DetailActivityViewModel
import com.sai.diagonalley.viewmodel.livedata.LiveDataWrapper
import com.sai.diagonalley.viewmodel.livedata.ResourceStatus
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class DetailActivity : DAActivity() {

    companion object {
        val paramItemId = "${DetailActivity::class.java}.param_item_id"
        val paramAnimationName = "${DetailActivity::class.java}.animation_name"
    }

    private var itemId: String? = null

    val viewmodel: DetailActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportPostponeEnterTransition()

        itemId = intent.getStringExtra(paramItemId)
        if (itemId == null) {
            Timber.d("No item id to display, closing activity!")
            finish()
            return
        }

        if (intent.hasExtra(paramAnimationName)) {
            val imageTransitionName = intent.getStringExtra(paramAnimationName)
            item_image_view.transitionName = imageTransitionName
        }

        initLiveDataObservers()
        viewmodel.fetchItem(itemId as String)
    }

    /* Section - Data */
    private fun initLiveDataObservers() {
        viewmodel.itemLiveData.observe(this, Observer<LiveDataWrapper<ItemEntity, Exception>> {
                livedataWrapper ->
                when (livedataWrapper.status) {
                    ResourceStatus.LOADING -> toggleProgress(true)

                    ResourceStatus.ERROR -> {
                        toggleProgress(false)
                        Timber.e(livedataWrapper.exception, "Error fetching item")
                        Toast.makeText(this, "Error fetching item", Toast.LENGTH_SHORT).show()
                    }

                    ResourceStatus.SUCCESS -> {
                        toggleProgress(false)

                        livedataWrapper.data?.let { item ->
                            item_name_text_view.text = item.displayName
                            item_description_text_view.text = item.description
                            item_available_text_view.text = if (item.isForRent)
                                getString(R.string.str_item_for_sale_rent)
                            else
                                getString(R.string.str_item_for_sale)
                            GlideApp.with(item_image_view)
                                .load(item.imageUrl)
                                .fitCenter()
                                .listener(object: RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        supportStartPostponedEnterTransition()
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        supportStartPostponedEnterTransition()
                                        return false
                                    }
                                })
                                .into(item_image_view)

                            if (item.isForRent) {
                                item_rent_button.visibility = View.VISIBLE
                                item_rent_button.text = getString(R.string.str_rent_button, item.rentalCost)
                            }

                            item_purchase_button.text = getString(R.string.str_purchase_button, item.purchaseCost)
                        }

                    }
                }
        })
    }

    /* Section - UI Handling */
    private fun toggleProgress(isBusy: Boolean) {
        detail_progress_bar.visibility = if (isBusy) View.VISIBLE else View.GONE
    }
}
