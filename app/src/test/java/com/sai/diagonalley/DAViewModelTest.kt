package com.sai.diagonalley

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sai.diagonalley.data.db.CategoryEntity
import com.sai.diagonalley.data.db.ItemEntity
import com.sai.diagonalley.module.ConnectivityModule
import com.sai.diagonalley.repository.IItemRepository
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

open class DAViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var connectivityModule: ConnectivityModule

    @Mock
    lateinit var repository: IItemRepository

    protected var fakeItem = ItemEntity(
        "fake_item_id", "wand","Elder Wand", "desc",
        false, null, "uri", 1000f,
        null, "Galleons"
    )

    protected var fakeCategory1 = CategoryEntity(
        "fake_cat_1",
        "Wands",
        "wand",
        true
    )

    protected var fakeCategory2 = CategoryEntity(
        "fake_cat_2",
        "Books",
        "book",
        false
    )

    open fun setup() {
        MockitoAnnotations.initMocks(this)
    }
}
