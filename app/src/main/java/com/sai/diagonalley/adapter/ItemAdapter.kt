package com.sai.diagonalley.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sai.diagonalley.R
import com.sai.diagonalley.data.db.ItemEntity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*


/**
 * Adapter for the recycler view displaying the list of items
 */
class ItemAdapter(private val list: List<ItemEntity>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val clickSubject = PublishSubject.create<ItemImageData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ItemViewHolder(LayoutInflater.from(parent.context), parent, clickSubject)

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun getClickEvent(): Observable<ItemImageData> = clickSubject

    /**
     * View Holder for each item in the items recycler view
     */
    class ItemViewHolder(inflater: LayoutInflater, private val parent: ViewGroup,
                         private val clickSubject: PublishSubject<ItemImageData>)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {

        private var itemCardView = itemView.findViewById<CardView>(R.id.item_layout)
        private var itemNameTextView = itemView.findViewById<TextView>(R.id.item_name_text_view)
        private var itemPriceTextView = itemView.findViewById<TextView>(R.id.item_price_text_view)
        private var itemImageView = itemView.findViewById<ImageView>(R.id.item_image_view)
        private var itemCategoryTextView = itemView.findViewById<TextView>(R.id.item_category_text_view)

        fun bind(item: ItemEntity) {
            itemNameTextView.text = item.displayName
            itemPriceTextView.text = if (item.isForRent)
                parent.context.getString(R.string.str_item_for_sale_rent)
            else
                parent.context.getString(R.string.str_item_for_sale)
            itemCategoryTextView.text = item.category
            ViewCompat.setTransitionName(itemImageView, item.displayName)
            Glide.with(itemImageView)
                .load(item.imageUrl)
                .fitCenter()
                .into(itemImageView)

            itemCardView.setOnClickListener { clickSubject.onNext(ItemImageData(item, itemImageView)) }
        }
    }

    data class ItemImageData(val item: ItemEntity, val imageView: ImageView)
}
