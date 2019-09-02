package com.sai.diagonalley.recycleview_helper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.core.Is

/**
 * A custom view assertion class that asserts the number of items present in the recycler view.
 */
class RecyclerViewItemCountAssertion(private val countToVerify: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        ViewMatchers.assertThat(adapter?.itemCount, Is.`is`(countToVerify))
    }
}
