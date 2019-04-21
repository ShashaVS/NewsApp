package com.android.shashavs.guardianclient.fragments.news_detail

import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.android.shashavs.guardianclient.retrofit.objects.News

class PagerAdapter(fm: FragmentManager, private val pagedList: PagedList<News>) : FragmentStatePagerAdapter(fm) {

    private val newsList = MutableList(pagedList.size) {index -> pagedList[index] }

    fun loadNextPage() {
        pagedList.loadAround(newsList.size-1)

        pagedList.addWeakCallback(pagedList, object : PagedList.Callback() {
            override fun onChanged(position: Int, count: Int) { updateData() }

            override fun onInserted(position: Int, count: Int) { updateData() }

            override fun onRemoved(position: Int, count: Int) { updateData() }
        })
    }

    private fun updateData() {
        newsList.clear()
        newsList.addAll(pagedList.snapshot())
        notifyDataSetChanged()
    }

    override fun getItem(position: Int) =
        NewsFragment().apply { arguments = Bundle().apply{
            putString("id", newsList[position]?.id)
            putInt("position", position)
        } }

    override fun getCount() = newsList.size
}