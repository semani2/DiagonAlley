package com.sai.diagonalley

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.linkedin.android.testbutler.TestButler
import com.sai.diagonalley.activity.DetailActivity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(DetailActivity::class.java, false, false)

    /**
     * Tests that a wand item is successfully displayed
     */
    @Test
    fun `test_wand_displayed`() {

        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, UITestData.WAND_ITEM_ID)
        activityRule.launchActivity(intent)

        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(withText(UITestData.WAND_NAME)))
        Espresso.onView(ViewMatchers.withId(R.id.item_available_text_view))
            .check(matches(withText(UITestData.PURCHASE_AVAILABILITY_TEXT)))
        Espresso.onView(ViewMatchers.withId(R.id.item_purchase_button))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_rent_button))
            .check(matches(not(ViewMatchers.isDisplayed())))
    }

    /**
     * Tests that a cauldron item is successfully displayed
     */
    @Test
    fun `test_cauldron_displayed`() {

        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, UITestData.CAULDRON_ITEM_ID)
        activityRule.launchActivity(intent)

        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(withText(UITestData.CAULDRON_NAME)))
        Espresso.onView(ViewMatchers.withId(R.id.item_available_text_view))
            .check(matches(withText(UITestData.PURCHASE_RENT_AVAILABILITY_TEXT)))
        Espresso.onView(ViewMatchers.withId(R.id.item_purchase_button))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_rent_button))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Tests that a book item is successfully displayed
     */
    @Test
    fun `test_book_displayed`() {

        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, UITestData.BOOK_ITEM_ID)
        activityRule.launchActivity(intent)

        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(withText(UITestData.BOOK_NAME)))
        Espresso.onView(ViewMatchers.withId(R.id.item_available_text_view))
            .check(matches(withText(UITestData.PURCHASE_RENT_AVAILABILITY_TEXT)))
        Espresso.onView(ViewMatchers.withId(R.id.item_purchase_button))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_rent_button))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Tests that a wand item is successfully displayed when the device is offline
     */
    @Test
    fun `test_wand_displayed_offline`() {
        toggleConnectivity(enable = false)

        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, UITestData.WAND_ITEM_ID)
        activityRule.launchActivity(intent)

        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(withText(UITestData.WAND_NAME)))
        Espresso.onView(ViewMatchers.withId(R.id.item_available_text_view))
            .check(matches(withText(UITestData.PURCHASE_AVAILABILITY_TEXT)))
        Espresso.onView(ViewMatchers.withId(R.id.item_purchase_button))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_rent_button))
            .check(matches(not(ViewMatchers.isDisplayed())))
    }

    /**
     * Tests that clicking on the purchase button displays a toast message with the required text
     */
    @Test
    fun `test_purchase_button_clicked_displays_toast`() {
        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, UITestData.WAND_ITEM_ID)
        activityRule.launchActivity(intent)

        val activity = activityRule.activity

        Espresso.onView(ViewMatchers.withId(R.id.item_purchase_button))
            .perform(click())

        onView(withText(R.string.str_purchase_coming_soon))
            .inRoot(withDecorView(not(`is`(activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    /**
     * Tests that clicking on the rent button displays a toast message with the required text
     */
    @Test
    fun `test_rent_button_clicked_displays_toast`() {
        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, UITestData.BOOK_ITEM_ID)
        activityRule.launchActivity(intent)

        val activity = activityRule.activity

        Espresso.onView(ViewMatchers.withId(R.id.item_rent_button))
            .perform(click())

        onView(withText(R.string.str_rent_coming_soon))
            .inRoot(withDecorView(not(`is`(activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @After
    fun teardown() {
        toggleConnectivity(true)
    }

    private fun toggleConnectivity(enable: Boolean) {
        TestButler.setGsmState(enable)
        TestButler.setWifiState(enable)
    }
}
