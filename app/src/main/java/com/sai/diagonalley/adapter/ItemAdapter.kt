package com.sai.diagonalley.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sai.diagonalley.R
import com.sai.diagonalley.data.db.ItemEntity
import io.reactivex.subjects.PublishSubject

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
    class ItemViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val clickSubject: PublishSubject<ItemEntity>)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {

        private var itemCardView = itemView.findViewById<CardView>(R.id.item_layout)
        private var itemNameTextView = itemView.findViewById<TextView>(R.id.item_name_text_view)
        private var itemPriceTextView = itemView.findViewById<TextView>(R.id.item_price_text_view)
        private var itemImageTextView = itemView.findViewById<ImageView>(R.id.item_image_view)

        fun bind(item: ItemEntity) {
            itemNameTextView.text = item.displayName
            itemPriceTextView.text = "${item.purchaseCost} to buy"


            itemCardView.setOnClickListener { clickSubject.onNext(item) }
        }
    }
}
