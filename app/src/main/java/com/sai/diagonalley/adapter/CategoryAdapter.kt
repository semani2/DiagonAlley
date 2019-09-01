package com.sai.diagonalley.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sai.diagonalley.R
import com.sai.diagonalley.data.db.CategoryEntity

class CategoryAdapter(private val context: Context, private val categories: List<CategoryEntity>)
    : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View? {
        val view: View?
        val viewholder: ListRowViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.filter_item_layout, viewGroup, false)
            viewholder = ListRowViewHolder(view)
            view.tag = viewholder
        } else {
            view = convertView
            viewholder = view.tag as ListRowViewHolder
        }

        viewholder.categoryNameTextView?.text = categories[position].name
        if (categories[position].isSelected) {
            viewholder.categorySelectedImageView?.visibility = View.VISIBLE
        } else {
            viewholder.categorySelectedImageView?.visibility = View.INVISIBLE
        }

        return view
    }

    override fun getItem(position: Int) = categories[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = categories.size

    class ListRowViewHolder(row: View?) {
        public var categoryNameTextView: TextView? = null
        public var categorySelectedImageView: ImageView? = null

        init {
            categoryNameTextView = row?.findViewById<TextView>(R.id.filter_text_view)
            categorySelectedImageView = row?.findViewById(R.id.filter_selected_image_view)
        }
    }
}
