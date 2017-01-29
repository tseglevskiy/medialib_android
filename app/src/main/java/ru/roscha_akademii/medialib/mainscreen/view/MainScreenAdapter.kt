package ru.roscha_akademii.medialib.mainscreen.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainScreenAdapter constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val first: DemoFragment by lazy {
        DemoFragment.getInstance("first")
    }

    val second: DemoFragment by lazy {
        DemoFragment.getInstance("second")
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment = when(position) {
        0 -> first
        1 -> second
        else -> throw IllegalArgumentException("Wrong position")
    }

    override fun getPageTitle(position: Int): CharSequence = when(position) {
        0 -> "first"
        1 -> "second"
        else -> throw IllegalArgumentException("Wrong position")
    }
}


