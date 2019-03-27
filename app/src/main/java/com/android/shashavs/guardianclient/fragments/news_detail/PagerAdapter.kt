package com.android.shashavs.guardianclient.fragments.news_detail

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.android.shashavs.guardianclient.retrofit.objects.News

class PagerAdapter(fm: FragmentManager, private val newsList: MutableList<News>) : FragmentStatePagerAdapter(fm) {

    fun updateData(data: List<News>) {
        newsList.clear()
        newsList.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int) =
        NewsFragment().apply { arguments = Bundle().apply{
            putString("id", newsList[position].id)
            putInt("position", position)
        } }

    override fun getCount() = newsList.size
}