package com.android.shashavs.guardianclient.fragments.news_list

import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.shashavs.guardianclient.R
import com.android.shashavs.guardianclient.retrofit.objects.News
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_item.view.*
import java.lang.Exception

class NewsListAdapter(private val listener: (Int, ImageView?) -> Unit) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    private var mDiffer: AsyncListDiffer<News>? = AsyncListDiffer(this, object: DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(p0: News, p1: News) = p0.id == p1.id
        override fun areContentsTheSame(p0: News, p1: News) = p0 == p1
    })

    fun submitList(data: List<News>) {
        mDiffer?.submitList(ArrayList<News>(data))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDiffer?.currentList?.get(position)
        if(item != null) holder.bind(item)
    }

    override fun getItemCount(): Int = mDiffer?.currentList?.size ?: 0

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

        private val thumbnail: ImageView = mView.thumbnail
        private val title: TextView = mView.title
        private val sectionName: TextView = mView.sectionName

        fun bind(news: News) {
            title.text = news.webTitle
            sectionName.text = news.sectionName

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
        }
    }

}
