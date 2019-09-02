package com.sai.diagonalley

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sai.diagonalley.data.db.CategoryEntity
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.module.ConnectivityModule
import com.sai.diagonalley.repository.IItemRepository
import com.sai.diagonalley.viewmodel.DAActivityViewModel
import com.sai.diagonalley.viewmodel.livedata.LiveDataWrapper
import com.sai.diagonalley.viewmodel.livedata.ResourceStatus
import io.reactivex.Single
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.*


@RunWith(JUnit4::class)
class DAActivityViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var repository: IItemRepository

    @Mock
    lateinit var connectivityModule: ConnectivityModule

    private var fakeCategory = CategoryEntity(
        UUID.randomUUID().toString(),
        "All"
    ,"all",
        true
    )

    private var fakeItem = ItemEntity(
        UUID.randomUUID().toString(), "wand","Elder Wand", "desc",
        false, null, "uri", 1000f,
        null, "Galleons"
    )

    lateinit var viewmodel: DAActivityViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewmodel = DAActivityViewModel(repository, connectivityModule)
    }

    @Test
    fun fetchItemsSuccessful() {
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

    @Test
    fun fetchItemsFailedWhenException() {
        Mockito.`when`(this.repository.getItems(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString()))
            .thenReturn(Single.error(Exception("Error fetching items")))

        val observer = mock(Observer::class.java) as Observer<LiveDataWrapper<List<ItemEntity>, Exception>>
        this.viewmodel.itemLiveData.observeForever(observer)

        this.viewmodel.fetchItems(ArgumentMatchers.anyString())

        assertNotNull(this.viewmodel.itemLiveData)
        assertNotNull(this.viewmodel.itemLiveData.value)
        assertNotNull(this.viewmodel.itemLiveData.value?.exception)
        assertNull(this.viewmodel.itemLiveData.value?.data)

        assertEquals(ResourceStatus.ERROR, this.viewmodel.itemLiveData.value?.status)
    }

    @Test
    fun fetchItemsFailedWhenNoItems() {
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
