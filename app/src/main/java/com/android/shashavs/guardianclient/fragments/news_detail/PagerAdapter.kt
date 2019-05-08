package com.android.shashavs.guardianclient.fragments.news_detail

import android.arch.paging.AsyncPagedListDiffer
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.android.shashavs.guardianclient.repository.data_objects.News

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var mDiffer: AsyncPagedListDiffer<News> = AsyncPagedListDiffer(object : ListUpdateCallback {

        override fun onChanged(p0: Int, p1: Int, p2: Any?) { }

        override fun onMoved(p0: Int, p1: Int) { notifyDataSetChanged() }

        override fun onInserted(p0: Int, p1: Int) { notifyDataSetChanged() }

        override fun onRemoved(p0: Int, p1: Int) { notifyDataSetChanged() }

    }, AsyncDifferConfig.Builder<News>(object : DiffUtil.ItemCallback<News>() {

        override fun areItemsTheSame(p0: News, p1: News) = p0.id == p1.id

        override fun areContentsTheSame(p0: News, p1: News) = p0 == p1

    }).build())

    fun submit(pagedList: PagedList<News>?) {
        mDiffer.submitList(pagedList)
    }

    override fun getItem(position: Int) =
        NewsFragment().apply { arguments = Bundle().apply{
            putSerializable("news", mDiffer.getItem(position))
            putInt("position", position)
        } }

    override fun getCount() = mDiffer.itemCount

}