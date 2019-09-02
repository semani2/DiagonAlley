package com.sai.diagonalley

import androidx.test.runner.AndroidJUnitRunner
import com.linkedin.android.testbutler.TestButler
import android.os.Bundle

class DATestRunner : AndroidJUnitRunner() {
    override fun onStart() {
        TestButler.setup(targetContext)
        super.onStart()
    }

    override fun finish(resultCode: Int, results: Bundle) {
        TestButler.teardown(targetContext)
        super.finish(resultCode, results)
    }
}
