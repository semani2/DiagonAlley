package com.sai.diagonalley.module

import android.content.Context
import android.content.Context.MODE_PRIVATE

/**
 * This class provides utility methods to interact with the Shared Preferences
 */
class SharedPreferencesModule(private val context: Context) {

    companion object {
        private val sharedPreferencesName = "com.sai.diagonalley.shared_preferences"
        val spFilterKey = "filter_key"
        val onlyRentKey = "rent_key"

        val defaultFilter = "all"
    }

    /**
     * This method helps store a string value in the shared preferences
     *
     * @param key
     * @param value
     */
    fun putString(key: String, value: String) {
        val editor = getEditor()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * This method helps retrieve a stored string value from the shared preferences
     *
     * @param key
     * @param defValue
     */
    fun getString(key: String, defValue: String) = getSharedPreferences().getString(key, defValue)

    /**
     * This method helps store a boolean value in the shared preferences
     *
     * @param key
     * @param value
     */
    fun putBoolean(key: String , value: Boolean) {
        val editor = getEditor()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * This method helps retrieve a stored boolean value from the shared preferences
     *
     * @param key
     * @param defValue
     */
    fun getBoolean(key: String, defValue: Boolean) = getSharedPreferences().getBoolean(key, defValue)

    private fun getEditor() = getSharedPreferences().edit()

    private fun getSharedPreferences() = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE)

}
