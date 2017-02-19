package ru.roscha_akademii.medialib.mainscreen.view

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.roscha_akademii.medialib.R
import ru.roscha_akademii.medialib.book.showlist.list.view.ListOfBooksFragment
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoFragment

class MainScreenAdapter(fm: FragmentManager, val context: Context) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 2

    val first: Fragment by lazy {
        ListOfVideoFragment.getInstance()
    }

    val second: Fragment by lazy {
        ListOfBooksFragment.getInstance()
    }

    override fun getItem(position: Int): Fragment = when(position) {
        0 -> first
        1 -> second
        else -> throw IllegalArgumentException("Wrong position")
    }

    override fun getPageTitle(position: Int): CharSequence = when(position) {
        0 -> context.getString(R.string.video)
        1 -> context.getString(R.string.books)
        else -> throw IllegalArgumentException("Wrong position")
    }
}


