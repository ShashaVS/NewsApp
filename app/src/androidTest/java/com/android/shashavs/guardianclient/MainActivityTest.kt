package com.android.shashavs.guardianclient

import androidx.navigation.fragment.NavHostFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityRule= ActivityTestRule(MainActivity::class.java)

    @Test
    fun testActivity() {
        val fragment = activityRule.activity.supportFragmentManager?.fragments?.firstOrNull()
        assertTrue(fragment is NavHostFragment)
    }
}