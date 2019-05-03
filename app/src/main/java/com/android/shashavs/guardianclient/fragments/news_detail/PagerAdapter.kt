package com.android.shashavs.guardianclient.fragments.news_detail

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.android.shashavs.guardianclient.repository.data_objects.News

class PagerAdapter(fm: FragmentManager, private val newsList: List<News>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) =
        NewsFragment().apply { arguments = Bundle().apply{
            putSerializable("news", newsList[position])
            putInt("position", position)
        } }

    override fun getCount() = newsList.size
}