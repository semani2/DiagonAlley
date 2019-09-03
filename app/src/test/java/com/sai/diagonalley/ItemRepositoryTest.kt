package com.sai.diagonalley

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sai.diagonalley.data.db.CategoryDao
import com.sai.diagonalley.data.db.CategoryEntity
import com.sai.diagonalley.data.db.ItemDao
import com.sai.diagonalley.data.db.ItemDatabase
import com.sai.diagonalley.module.ApiModule
import com.sai.diagonalley.module.DbModule
import com.sai.diagonalley.repository.IItemRepository
import com.sai.diagonalley.repository.ItemRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Unit Tests for the ItemRepository
 *
 * @see ItemRepository
 */
@RunWith(JUnit4::class)
class ItemRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var apiModule: ApiModule

    @Mock
    lateinit var dbModule: DbModule

    @Mock
    lateinit var itemDatabase: ItemDatabase

    @Mock
    lateinit var itemDao: ItemDao

    @Mock
    lateinit var categoryDao: CategoryDao

    lateinit var itemRepository: IItemRepository


    private var fakeCategory = CategoryEntity(
        UUID.randomUUID().toString(),
        "All"
        ,"all",
        true
    )

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        itemRepository = ItemRepository(apiModule, dbModule)
    }

    @Test
    fun `test_fetches_items_from_cache_no_network`() {
        Mockito.`when`(itemDatabase.itemDao()).thenReturn(itemDao)
        Mockito.`when`(this.dbModule.getItemDatabase()).thenReturn(itemDatabase)

        this.itemRepository.getItems(true, "all")

        verify(apiModule, times(0)).readInventoryFromFile()
        verify(dbModule, times(1)).getItemDatabase()
    }

    @Test
    fun `test_fetch_categories_tries_to_fetch_from_cache_first`() {
        Mockito.`when`(categoryDao.getCategories()).thenReturn(Single.just(mutableListOf(fakeCategory)))
        Mockito.`when`(itemDatabase.categoryDao()).thenReturn(categoryDao)
        Mockito.`when`(this.dbModule.getItemDatabase()).thenReturn(itemDatabase)

        this.itemRepository.getCategories()

        verify(apiModule, times(0)).readInventoryFromFile()
        verify(dbModule, times(1)).getItemDatabase()
    }

}
