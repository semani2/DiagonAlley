package com.sai.diagonalley.data

/**
 * Data Class representing the Item API Model
 */
data class Item(val id: String, val category: String, val displayName: String, val description: String,
                val isForRent: Boolean, val rentalPeriod: String, val imageUrl: String,
                val purchaseCost: Float, val rentalCost: Float, val currency: String)
