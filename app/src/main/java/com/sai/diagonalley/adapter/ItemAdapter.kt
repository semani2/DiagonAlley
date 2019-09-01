package com.sai.diagonalley.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sai.diagonalley.R
import com.sai.diagonalley.data.db.ItemEntity
import io.reactivex.subjects.PublishSubject
import androidx.palette.graphics.Palette


/**
 * Adapter for the recycler view displaying the list of items
 */
class ItemAdapter(private val list: List<ItemEntity>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val clickSubject = PublishSubject.create<ItemEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ItemViewHolder(LayoutInflater.from(parent.context), parent, clickSubject)

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    /**
     * View Holder for each item in the items recycler view
     */
    class ItemViewHolder(inflater: LayoutInflater, val parent: ViewGroup, private val clickSubject: PublishSubject<ItemEntity>)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {

        private var itemCardView = itemView.findViewById<CardView>(R.id.item_layout)
        private var itemNameTextView = itemView.findViewById<TextView>(R.id.item_name_text_view)
        private var itemPriceTextView = itemView.findViewById<TextView>(R.id.item_price_text_view)
        private var itemImageView = itemView.findViewById<ImageView>(R.id.item_image_view)

        fun bind(item: ItemEntity) {
            itemNameTextView.text = item.displayName
            itemPriceTextView.text = "${item.purchaseCost} to buy"
            Glide.with(itemImageView)
                .asBitmap()
                .load(item.imageUrl)
                .fitCenter()
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        itemImageView.setImageBitmap(resource)

                        Palette.from(resource).generate { palette ->
                            val darkVibrant = palette?.darkVibrantSwatch
                            if (darkVibrant != null) {
                                itemCardView.setCardBackgroundColor(darkVibrant.rgb)
                            } else {
                                itemCardView.setCardBackgroundColor(parent.context.resources
                                    .getColor(R.color.colorItemBgDef))
                            }
                        }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        /* no op */
                    }
                })

            itemCardView.setOnClickListener { clickSubject.onNext(item) }
        }
    }
}
