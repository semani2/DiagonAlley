package com.sai.diagonalley

import androidx.lifecycle.Observer
import com.sai.diagonalley.data.db.CategoryEntity
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

    /**
     * Tests that the category data observer is updated correctly based on the data received
     * from the repository
     *
     * Expected results:
     * 1. LiveData status = ResourceStatus.Success
     * 2. LiveData data = not null with size 2
     * 3. LiveData exception =null
     */
    @Test
    fun `test_fetch_categories_successful`() {
        val fakeCategories = mutableListOf<CategoryEntity>()
        fakeCategories.add(fakeCategory1)
        fakeCategories.add(fakeCategory2)

        Mockito.`when`(this.repository.getCategories())
            .thenReturn(Single.just(fakeCategories))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<CategoryEntity>, Exception>>
        this.viewmodel.categoryLiveData.observeForever(observer)

        this.viewmodel.fetchCategories()

        assertNotNull(this.viewmodel.categoryLiveData)
        assertNotNull(this.viewmodel.categoryLiveData.value)
        assertNull(this.viewmodel.categoryLiveData.value?.exception)
        assertNotNull(this.viewmodel.categoryLiveData.value?.data)
        assertEquals(this.viewmodel.categoryLiveData.value?.data?.size, fakeCategories.size)
        assertEquals(ResourceStatus.SUCCESS, this.viewmodel.categoryLiveData.value?.status)
    }

    /**
     * Tests that the category data observer is updated correctly based on the data received
     * from the repository
     *
     * Expected results:
     * 1. LiveData status = ResourceStatus.Error
     * 2. LiveData data = null
     * 3. LiveData exception = Not null
     */
    @Test
    fun `test_fetch_categories_failed_with_exception`() {
        val exceptionMessage = "Error fetching categories"
        Mockito.`when`(this.repository.getCategories())
            .thenReturn(Single.error(Exception(exceptionMessage)))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<CategoryEntity>, Exception>>
        this.viewmodel.categoryLiveData.observeForever(observer)

        this.viewmodel.fetchCategories()

        assertNotNull(this.viewmodel.categoryLiveData)
        assertNotNull(this.viewmodel.categoryLiveData.value)
        assertNotNull(this.viewmodel.categoryLiveData.value?.exception)
        assertNull(this.viewmodel.categoryLiveData.value?.data)
        assertEquals(this.viewmodel.categoryLiveData.value?.exception?.message, exceptionMessage)

        assertEquals(ResourceStatus.ERROR, this.viewmodel.categoryLiveData.value?.status)
    }

}
