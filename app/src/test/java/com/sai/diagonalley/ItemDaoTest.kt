package com.sai.diagonalley

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sai.diagonalley.data.db.ItemDao
import com.sai.diagonalley.data.db.ItemDatabase
import com.sai.diagonalley.data.db.ItemEntity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit Tests for the Item Dao
 *
 * @see ItemDao
 */
@RunWith(AndroidJUnit4::class)
class ItemDaoTest {

    private lateinit var itemDao: ItemDao
    private lateinit var db: ItemDatabase

    private var fakeItem1 = ItemEntity(
        "fake_item_id_1", "wand","Elder Wand", "desc",
        false, null, "uri", 1000f,
        null, "Galleons"
    )

    private var fakeItem2 = ItemEntity(
        "fake_item_id_2", "wand","Eldest Wand", "desc",
        false, null, "uri", 800f,
        null, "Galleons"
    )

    private var fakeItem3 = ItemEntity(
        "fake_item_id_3", "book","Book of Potions", "desc",
        false, null, "uri", 200f,
        null, "Galleons"
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        itemDao = db.itemDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    /**
     * Tests that the Item Dao returns an empty list for getItems()
     * when no data has been inserted
     */
    @Test
    fun `test_get_items_returns_empty_list_when_no_data_inserted`() {
        val items = itemDao.getItems().blockingGet()
        assertEquals(items.size, 0)
    }

    /**
     * Tests that the Item Dao returns an empty list for getItemById()
     * when no data has been inserted
     */
    @Test
    fun `test_get_item_by_id_returns_empty_when_no_data_inserted`() {
        val items = itemDao.getItems().blockingGet()
        assertEquals(items.size, 0)
    }

    /**
     * Tests that the Item Dao returns inserted data for getItems()
     */
    @Test
    fun `test_get_items_returns_inserted_data`() {
        val items = mutableListOf<ItemEntity>()
        items.add(fakeItem1)
        items.add(fakeItem2)

        itemDao.insertAllItems(items)

        val dbItems = itemDao.getItems().blockingGet()
        assertEquals(dbItems.size, items.size)

        for (i in 0 until dbItems.size) {
            assertEquals(dbItems[i].id, items[i].id)
        }
    }

    /**
     * Tests that Item DAO returns the requested item by id
     */
    @Test
    fun `test_get_item_by_id_returns_requested_item`() {
        val items = mutableListOf<ItemEntity>()
        items.add(fakeItem1)
        items.add(fakeItem2)

        itemDao.insertAllItems(items)

        val dbItems = itemDao.getItemById(fakeItem1.id).blockingGet()
        assertEquals(dbItems.size, 1)
        assertEquals(dbItems[0].id, fakeItem1.id)
    }

    /**
     * Tests that Item DAO returns an empty list when requested item id is invalid
     */
    @Test
    fun `test_get_item_by_id_returns_empty_for_invalid_id`() {
        val items = mutableListOf<ItemEntity>()
        items.add(fakeItem1)
        items.add(fakeItem2)

        itemDao.insertAllItems(items)

        val dbItems = itemDao.getItemById(fakeItem3.id).blockingGet()
        assertEquals(dbItems.size, 0)
    }

    /**
     * Tests that Item DAO returns items that match the requested category
     */
    @Test
    fun `test_get_items_by_category_returns_items_of_requested_category`() {
        val items = mutableListOf<ItemEntity>()
        items.add(fakeItem1)
        items.add(fakeItem2)
        items.add(fakeItem3)

        itemDao.insertAllItems(items)

        val dbItems = itemDao.getItemsByCategory("wand").blockingGet()
        assertEquals(dbItems.size, 2)

        for (i in 0 until dbItems.size) {
            assertEquals(dbItems[i].category, "wand")
        }
    }

    /**
     * Tests that Item DAO returns an empty list when requested category is invalid
     */
    @Test
    fun `test_get_items_by_category_returns_empty_list_for_invalid_category`() {
        val items = mutableListOf<ItemEntity>()
        items.add(fakeItem1)
        items.add(fakeItem2)
        items.add(fakeItem3)

        itemDao.insertAllItems(items)

        val dbItems = itemDao.getItemsByCategory("cauldron").blockingGet()
        assertEquals(dbItems.size, 0)
    }
 }
