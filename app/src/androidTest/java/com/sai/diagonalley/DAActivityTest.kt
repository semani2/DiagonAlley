package com.sai.diagonalley

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.truth.content.IntentSubject.assertThat
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import com.linkedin.android.testbutler.TestButler
import com.sai.diagonalley.activity.DAActivity
import com.sai.diagonalley.activity.DetailActivity
import com.sai.diagonalley.recycleview_helper.RecyclerViewItemCountAssertion
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


private const val PACKAGE_NAME = "com.sai.diagonalley"

@LargeTest
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class DAActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(DAActivity::class.java)

    /**
     * Tests the start state of the application
     */
    @Test
    fun `test_start_state`() {
        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(not((ViewMatchers.isDisplayed()))))
    }

    /**
     * Tests that all items are fetched when the default category All is selected
     *
     * Expected Results
     * 1. Progress bar is displayed
     * 2. Wait for two seconds (fake delay added)
     * 3. Progress bar is not visible any more
     * 4. Recycler view with all items is displayed
     * 5. Number of items in recycler view is 25
     */
    @Test
    fun `test_category_all_items_fetched`() {
        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view)).check(
            RecyclerViewItemCountAssertion(25)
        )
    }

    /**
     * Tests that all items are fetched when the Wands category All is selected
     *
     * Expected Results
     * 1. Progress bar is displayed
     * 2. Wait for two seconds (fake delay added)
     * 3. Progress bar is not visible any more
     * 4. Recycler view with all items is displayed
     * 5. Click on the menu filter icon
     * 6. Wait for the filter dialog to be displayed
     * 7. Click on wands filter text
     * 8. Wait for results to be filtered
     * 9. Verify 10 items are displayed
     */
    @Test
    fun `test_category_wands_items_fetched`() {
        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Wands")).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view)).check(
            RecyclerViewItemCountAssertion(10)
        )

        // Reset to default category
        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("All")).perform(ViewActions.click())
    }


    /**
     * Tests that all items are fetched when the Cauldrons category All is selected
     *
     * Expected Results
     * 1. Progress bar is displayed
     * 2. Wait for two seconds (fake delay added)
     * 3. Progress bar is not visible any more
     * 4. Recycler view with all items is displayed
     * 5. Click on the menu filter icon
     * 6. Wait for the filter dialog to be displayed
     * 7. Click on cauldrons filter text
     * 8. Wait for results to be filtered
     * 9. Verify 5 items are displayed
     */
    @Test
    fun `test_category_cauldrons_items_fetched`() {
        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Cauldrons")).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view)).check(
            RecyclerViewItemCountAssertion(5)
        )

        // Reset to default category
        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("All")).perform(ViewActions.click())
    }

    /**
     * Tests that all items are fetched when the Books category All is selected
     *
     * Expected Results
     * 1. Progress bar is displayed
     * 2. Wait for two seconds (fake delay added)
     * 3. Progress bar is not visible any more
     * 4. Recycler view with all items is displayed
     * 5. Click on the menu filter icon
     * 6. Wait for the filter dialog to be displayed
     * 7. Click on Books filter text
     * 8. Wait for results to be filtered
     * 9. Verify 5 items are displayed
     */
    @Test
    fun `test_category_books_items_fetched`() {
        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Books")).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view)).check(
            RecyclerViewItemCountAssertion(5)
        )

        // Reset to default category
        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("All")).perform(ViewActions.click())
    }

    /**
     * Tests that all items are fetched when the Brooms category All is selected
     *
     * Expected Results
     * 1. Progress bar is displayed
     * 2. Wait for two seconds (fake delay added)
     * 3. Progress bar is not visible any more
     * 4. Recycler view with all items is displayed
     * 5. Click on the menu filter icon
     * 6. Wait for the filter dialog to be displayed
     * 7. Click on Brooms filter text
     * 8. Wait for results to be filtered
     * 9. Verify 5 items are displayed
     */
    @Test
    fun `test_category_brooms_items_fetched`() {
        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Brooms")).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view)).check(
            RecyclerViewItemCountAssertion(5)
        )

        // Reset to default category
        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("All")).perform(ViewActions.click())
    }

    /**
     * Tests that an item click triggers an intent for the Detail Activity
     */
    @Test
    fun `test_item_click_triggers_intent`() {
        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                    ViewActions.click()
                ))

        val receivedIntent = Iterables.getOnlyElement(Intents.getIntents())
        assertThat(receivedIntent).hasComponentPackage(PACKAGE_NAME)
        assertThat(receivedIntent).hasComponentClass(DetailActivity::class.java)
        assertThat(receivedIntent).extras().containsKey(DetailActivity.paramItemId)
        assertThat(receivedIntent).extras().string(DetailActivity.paramItemId)
            .isEqualTo("5a992056-1b9f-4159-83d7-0405dabf3184")
    }

    /**
     * Tests that once the user has used the app when there is a network connection and
     * items have been cached, they can continue to use the app when there is no network
     *
     * Expected Results
     *
     * Turn off network connection
     *
     * 1. Progress bar is displayed
     * 2. Wait for two seconds (fake delay added)
     * 3. Progress bar is not visible any more
     * 4. Recycler view with all items is displayed
     */
    @Test
    fun `test_no_network_connection_items_fetched`() {
        toggleConnectivity(false)

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view)).check(
            RecyclerViewItemCountAssertion(25)
        )
    }

    /**
     * Tests that all items are fetched when the Brooms category All is selected
     *
     * Expected Results
     *
     * Turn off network connection
     *
     * 1. Progress bar is displayed
     * 2. Wait for two seconds (fake delay added)
     * 3. Progress bar is not visible any more
     * 4. Recycler view with all items is displayed
     * 5. Click on the menu filter icon
     * 6. Wait for the filter dialog to be displayed
     * 7. Click on Brooms filter text
     * 8. Wait for results to be filtered
     * 9. Verify 5 items are displayed
     */
    @Test
    fun `test_no_network_connection_category_brooms_items_fetched`() {
        toggleConnectivity(false)

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Brooms")).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_recycler_view)).check(
            RecyclerViewItemCountAssertion(5)
        )

        // Reset to default category
        Espresso.onView(ViewMatchers.withId(R.id.menu_action_filter)).perform(ViewActions.click())

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("All")).perform(ViewActions.click())
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

