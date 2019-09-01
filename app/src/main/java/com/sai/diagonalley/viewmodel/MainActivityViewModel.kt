package com.sai.diagonalley.viewmodel

import androidx.lifecycle.ViewModel
import com.sai.diagonalley.repository.IItemRepository
import timber.log.Timber

/**
 * ViewModel for the MainActivity
 *
 * @see MainActivity
 */
class MainActivityViewModel(private val repository: IItemRepository) : ViewModel() {

    fun hello() {
        Timber.d("Hello View model")
    }
}
