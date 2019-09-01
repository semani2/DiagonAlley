package com.sai.diagonalley.module

import android.content.Context
import java.io.IOException

/**
 * Fake API service that mimics reading data from an api, instead reads the inventory json
 * file from the assets folder
 */
class ApiModule(private val context: Context) {

    /**
     * Reads the inventory json file and returns the json as a string
     *
     * @return String: JSON string
     */
    @Throws(IOException::class)
    fun readInventoryFromFile(): String? {
        val inputStream = context.assets.open("inventory.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        inputStream.close()
        return json
    }
}
