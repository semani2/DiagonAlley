package com.sai.diagonalley

import androidx.lifecycle.Observer
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.viewmodel.DAActivityViewModel
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
import org.mockito.Mockito.mock

/**
 * Unit Tests for DAActivityViewModelTest
 *
 * @see DAActivityViewModel
 */
@RunWith(JUnit4::class)
class DAActivityViewModelTest : DAViewModelTest() {

    lateinit var viewmodel: DAActivityViewModel

    @Before
    override fun setup() {
        super.setup()
        viewmodel = DAActivityViewModel(repository, connectivityModule)
    }

    /**
     * Tests that the live data observer is updated correctly based on the data received from the repository
     *
     * Expected results:
     * 1. LiveData status = ResourceStatus.Success
     * 2. LiveData data size = 1 (fake item returned from repository)
     * 3. LiveData exception = null
     */
    @Test
    fun `test_fetch_items_successful`() {
        Mockito.`when`(this.repository.getItems(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString()))
            .thenReturn(Single.just(mutableListOf(fakeItem)))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<ItemEntity>, Exception>>
        this.viewmodel.itemLiveData.observeForever(observer)

        this.viewmodel.fetchItems(ArgumentMatchers.anyString())

        assertNotNull(this.viewmodel.itemLiveData)
        assertNotNull(this.viewmodel.itemLiveData.value)
        assertNull(this.viewmodel.itemLiveData.value?.exception)
        assertNotNull(this.viewmodel.itemLiveData.value?.data)
        assertEquals(this.viewmodel.itemLiveData.value?.data?.size, 1)
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
    fun `test_fetch_items_failed_with_exception`() {
        val exceptionMessage = "Error fetching items"
        Mockito.`when`(this.repository.getItems(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString()))
            .thenReturn(Single.error(Exception(exceptionMessage)))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<ItemEntity>, Exception>>
        this.viewmodel.itemLiveData.observeForever(observer)

        this.viewmodel.fetchItems(ArgumentMatchers.anyString())

        assertNotNull(this.viewmodel.itemLiveData)
        assertNotNull(this.viewmodel.itemLiveData.value)
        assertNotNull(this.viewmodel.itemLiveData.value?.exception)
        assertNull(this.viewmodel.itemLiveData.value?.data)
        assertEquals(this.viewmodel.itemLiveData.value?.exception?.message, exceptionMessage)

        assertEquals(ResourceStatus.ERROR, this.viewmodel.itemLiveData.value?.status)
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
    fun `test_fetch_items_failed_when_empty`() {
        Mockito.`when`(this.repository.getItems(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString()))
            .thenReturn(Single.just(mutableListOf()))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<ItemEntity>, Exception>>
        this.viewmodel.itemLiveData.observeForever(observer)

        this.viewmodel.fetchItems(ArgumentMatchers.anyString())

        assertNotNull(this.viewmodel.itemLiveData)
        assertNotNull(this.viewmodel.itemLiveData.value)
        assertNotNull(this.viewmodel.itemLiveData.value?.exception)
        assertNull(this.viewmodel.itemLiveData.value?.data)

        assertEquals(ResourceStatus.ERROR, this.viewmodel.itemLiveData.value?.status)
    }

}
