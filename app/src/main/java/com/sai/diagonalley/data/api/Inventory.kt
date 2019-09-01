package com.sai.diagonalley.data.api

import com.google.gson.annotations.SerializedName

/**
 * Data Class Representing the inventory API model
 */
data class Inventory(@SerializedName("inventory") val items: List<Item>)
