package com.sai.diagonalley

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.repository.IItemRepository
import com.sai.diagonalley.viewmodel.DetailActivityViewModel
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
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DetailActivityViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var repository: IItemRepository

    private var fakeItem = ItemEntity(
        "fake_item_id", "wand","Elder Wand", "desc",
        false, null, "uri", 1000f,
        null, "Galleons"
    )

    lateinit var viewmodel: DetailActivityViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewmodel = DetailActivityViewModel(repository)
    }

    @Test
    fun fetchItemSuccessful() {
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

    @Test
    fun fetchItemFailed() {
        Mockito.`when`(this.repository.getItemById(ArgumentMatchers.anyString()))
            .thenReturn(Single.error(Exception("Error fetching item")))

        val observer = Mockito.mock(Observer::class.java) as Observer<LiveDataWrapper<ItemEntity, Exception>>
        this.viewmodel.itemLiveData.observeForever(observer)

        this.viewmodel.fetchItem(ArgumentMatchers.anyString())

        assertNotNull(this.viewmodel.itemLiveData)
        assertNotNull(this.viewmodel.itemLiveData.value)
        assertNotNull(this.viewmodel.itemLiveData.value?.exception)
        assertNull(this.viewmodel.itemLiveData.value?.data)

        assertEquals(ResourceStatus.ERROR, this.viewmodel.itemLiveData.value?.status)
    }

}
