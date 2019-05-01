package com.android.shashavs.guardianclient.fragments.news_list

import android.arch.paging.PagedListAdapter
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.shashavs.guardianclient.R
import com.android.shashavs.guardianclient.repository.data_objects.News
import com.android.shashavs.guardianclient.utils.DateTimeUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_item.view.*
import java.lang.Exception

class NewsListAdapter(private val listener: (Int, ImageView?) -> Unit) :
    PagedListAdapter<News, NewsListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<News>() {

        override fun areItemsTheSame(p0: News, p1: News) = p0.id == p1.id

        override fun areContentsTheSame(p0: News, p1: News) = p0 == p1
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null) holder.bind(item)
    }

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

        private val thumbnail: ImageView = mView.thumbnail
        private val title: TextView = mView.title
        private val sectionName: TextView = mView.sectionName

        private val test: TextView = mView.test

        fun bind(news: News) {
            title.text = news.webTitle
            val date = DateTimeUtil.getDate(news.webPublicationDate)
            sectionName.text = String.format(mView.context.getString(R.string.date_name), date, news.sectionName)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                thumbnail.transitionName = news.id
            } else {
                ViewCompat.setTransitionName(thumbnail, news.id)
            }

            Picasso.get()
                .load(news.fields?.thumbnail)
                .into(thumbnail, object : Callback {
                    override fun onSuccess() {
                        listener(adapterPosition, null)
                    }
                    override fun onError(e: Exception?) {
                        listener(adapterPosition, null)
                    }
                })

            mView.setOnClickListener { listener(adapterPosition, thumbnail) }

            test.text = news.currentPage.toString().plus("/").plus(adapterPosition)
        }
    }

}
