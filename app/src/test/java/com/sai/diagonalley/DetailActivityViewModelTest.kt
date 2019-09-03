package com.sai.diagonalley

import androidx.lifecycle.Observer
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.viewmodel.DetailActivityViewModel
import com.sai.diagonalley.viewmodel.livedata.LiveDataWrapper
import com.sai.diagonalley.viewmodel.livedata.ResourceStatus
import io.reactivex.Single
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

/**
 * Unit Tests for DetailActivityViewModel
 *
 * @see DetailActivityViewModel
 */
@RunWith(JUnit4::class)
class DetailActivityViewModelTest : DAViewModelTest() {

    lateinit var viewmodel: DetailActivityViewModel

    @Before
    override fun setup() {
        super.setup()
        viewmodel = DetailActivityViewModel(repository)
    }

    /**
     * Tests that the live data observer is updated correctly based on the data received from the repository
     *
     * Expected results:
     * 1. LiveData status = ResourceStatus.Success
     * 2. LiveData data id = fake item id (returned from the repository)
     * 3. LiveData exception = null
     */
    @Test
    fun `test_fetch_item_successful`() {
        Mockito.`when`(this.repository.getItemById(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(mutableListOf(fakeItem)))

        val observer = Mockito.mock(Observer::class.java) as Observer<LiveDataWrapper<ItemEntity, Exception>>
        this.viewmodel.itemLiveData.observeForever(observer)

        this.viewmodel.fetchItem(ArgumentMatchers.anyString())

        assertNotNull(this.viewmodel.itemLiveData)
        assertNotNull(this.viewmodel.itemLiveData.value)
        assertNull(this.viewmodel.itemLiveData.value?.exception)
        assertNotNull(this.viewmodel.itemLiveData.value?.data)
        assertEquals(this.viewmodel.itemLiveData.value?.data?.id, "fake_item_id")
        assertEquals(ResourceStatus.SUCCESS, this.viewmodel.itemLiveData.value?.status)
    }

    /**
     * Tests that the live data observer is updated correctly based on the data received from the repository
     *
     * Expected results:
     * 1. LiveData status = ResourceStatus.Error
     * 2. LiveData data = null
     * 3. LiveData exception = Not null
     */
    @Test
    fun `test_fetch_item_failed`() {
        val exceptionMessage = "Error fetching item"
        Mockito.`when`(this.repository.getItemById(ArgumentMatchers.anyString()))
            .thenReturn(Single.error(Exception(exceptionMessage)))

        val observer = Mockito.mock(Observer::class.java) as Observer<LiveDataWrapper<ItemEntity, Exception>>
        this.viewmodel.itemLiveData.observeForever(observer)

        this.viewmodel.fetchItem(ArgumentMatchers.anyString())

        assertNotNull(this.viewmodel.itemLiveData)
        assertNotNull(this.viewmodel.itemLiveData.value)
        assertNotNull(this.viewmodel.itemLiveData.value?.exception)
        assertEquals(this.viewmodel.itemLiveData.value?.exception?.message, exceptionMessage)
        assertNull(this.viewmodel.itemLiveData.value?.data)

        assertEquals(ResourceStatus.ERROR, this.viewmodel.itemLiveData.value?.status)
    }

}
