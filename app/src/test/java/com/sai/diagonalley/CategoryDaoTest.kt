package com.sai.diagonalley

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sai.diagonalley.data.db.CategoryDao
import com.sai.diagonalley.data.db.CategoryEntity
import com.sai.diagonalley.data.db.ItemDatabase
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit Tests for Category DAO
 *
 * @see CategoryDao
 */
@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private lateinit var categoryDao: CategoryDao
    private lateinit var db: ItemDatabase

    private var fakeCategory1 = CategoryEntity(
        "fake_cat_1",
        "Wands",
        "wand",
        true
    )

    private var fakeCategory2 = CategoryEntity(
        "fake_cat_2",
        "Books",
        "book",
    false
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        categoryDao = db.categoryDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    /**
     * Tests that Category DAO returns an empty list for getCategories()
     * when no data has been inserted
     */
    @Test
    fun `test_get_categories_returns_empty_list_when_no_data_inserted`() {
        val cats = categoryDao.getCategories().blockingGet()
        assertEquals(cats.size, 0)
    }

    /**
     * Tests that Category DAO returns inserted data for getCategories()
     */
    @Test
    fun `test_get_categories_returns_inserted_data`() {
        val cats = mutableListOf<CategoryEntity>()
        cats.add(fakeCategory1)
        cats.add(fakeCategory2)

        categoryDao.insertAllCategories(cats)

        val dbCats = categoryDao.getCategories().blockingGet()
        assertEquals(dbCats.size, cats.size)

        for (i in 0 until dbCats.size) {
            assertEquals(cats[i].id, dbCats[i].id)
        }
    }

    /**
     * Tests that CategoryDAO updates data correctly in the database
     */
    @Test
    fun `test_update_categories_updates_data_in_db`() {
        val cats = mutableListOf<CategoryEntity>()
        cats.add(fakeCategory1)
        cats.add(fakeCategory2)

        categoryDao.insertAllCategories(cats)

        val dbCats = categoryDao.getCategories().blockingGet()
        assertEquals(dbCats.size, cats.size)

        for (i in 0 until dbCats.size) {
            assertEquals(cats[i].isSelected, dbCats[i].isSelected)
        }

        fakeCategory1.isSelected = false
        fakeCategory2.isSelected = true

        cats.clear()

        cats.add(fakeCategory1)
        cats.add(fakeCategory2)

        categoryDao.updateCategories(cats)
        val dbUpdatedCats = categoryDao.getCategories().blockingGet()
        assertEquals(dbUpdatedCats.size, cats.size)

        for (i in 0 until dbUpdatedCats.size) {
            assertEquals(cats[i].isSelected, dbUpdatedCats[i].isSelected)
        }
    }

    /**
     * Tests that Category DAO deletes all categories from the database
     */
    @Test
    fun `test_delete_categories_removes_data_from_db`() {
        val cats = mutableListOf<CategoryEntity>()
        cats.add(fakeCategory1)
        cats.add(fakeCategory2)

        categoryDao.insertAllCategories(cats)

        val dbCats = categoryDao.getCategories().blockingGet()
        assertEquals(dbCats.size, cats.size)

        categoryDao.deleteAllCategories()

        val dbDeletedCats = categoryDao.getCategories().blockingGet()
        assertEquals(dbDeletedCats.size, 0)
    }

}
