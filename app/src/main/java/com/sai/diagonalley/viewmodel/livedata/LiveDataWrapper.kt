package com.sai.diagonalley.viewmodel.livedata

/**
 *  A wrapper around the android.arch.LiveData object that gives a little more info about
 *  the data we are trying to set.
 *  The @ResourceStatus is an enum which quickly informs about the state of the data:
 *  LOADING, SUCCESS or ERROR
 *  The data is the actual Data we set on the LiveData object
 *  The exception is to set an Exception in case there was an error
 */
data class LiveDataWrapper<out T, out W>(
    val status: ResourceStatus,
    val data: T?,
    val exception: W?
)

enum class ResourceStatus {
    SUCCESS,
    LOADING,
    ERROR
}
