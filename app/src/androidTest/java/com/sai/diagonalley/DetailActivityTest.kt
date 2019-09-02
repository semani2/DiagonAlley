package com.sai.diagonalley

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.sai.diagonalley.activity.DetailActivity
import org.junit.Rule
import org.junit.runner.RunWith
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.not
import org.junit.Test


@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(DetailActivity::class.java, false, false)

    @Test
    fun `test_wand_displayed`() {

        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, "5a992056-1b9f-4159-83d7-0405dabf3184")
        activityRule.launchActivity(intent)

        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(withText("Elder Wand")))
        Espresso.onView(ViewMatchers.withId(R.id.item_available_text_view))
            .check(matches(withText("Available for purchase only")))
        Espresso.onView(ViewMatchers.withId(R.id.item_purchase_button))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_rent_button))
            .check(matches(not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun `test_cauldron_displayed`() {

        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, "deb90c36-293c-449e-9a27-db7e99cb5aa6")
        activityRule.launchActivity(intent)

        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(withText("Brass Cauldron")))
        Espresso.onView(ViewMatchers.withId(R.id.item_available_text_view))
            .check(matches(withText("Available for purchase & rent")))
        Espresso.onView(ViewMatchers.withId(R.id.item_purchase_button))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_rent_button))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun `test_book_displayed`() {

        val intent = Intent()
        intent.putExtra(DetailActivity.paramItemId, "c234d621-abf2-4f6d-92e2-f1bb1460fbcc")
        activityRule.launchActivity(intent)

        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_name_text_view))
            .check(matches(withText("Book of Potions")))
        Espresso.onView(ViewMatchers.withId(R.id.item_available_text_view))
            .check(matches(withText("Available for purchase & rent")))
        Espresso.onView(ViewMatchers.withId(R.id.item_purchase_button))
            .check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.item_rent_button))
            .check(matches(ViewMatchers.isDisplayed()))
    }
}
