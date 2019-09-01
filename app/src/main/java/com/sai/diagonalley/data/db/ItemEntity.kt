package com.sai.diagonalley.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "display_name") var displayName: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "is_for_rent") var isForRent: Boolean,
    @ColumnInfo(name = "rental_period") var rentalPeriod: String?,
    @ColumnInfo(name = "image_url") var imageUrl: String?,
    @ColumnInfo(name = "purchase_cost") var purchaseCost: Float?,
    @ColumnInfo(name = "rental_cost") var rentalCost: Float?,
    @ColumnInfo(name = "currency") var currency: String
)
