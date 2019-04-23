package com.android.shashavs.guardianclient.fragments.news_detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.android.shashavs.guardianclient.R
import com.android.shashavs.guardianclient.base.BaseFragment
import com.android.shashavs.guardianclient.view_model.NewsListViewModel
import com.android.shashavs.guardianclient.view_model.NewsListViewModelFactory
import com.android.shashavs.guardianclient.retrofit.objects.News
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_news.*
import java.lang.Exception
import javax.inject.Inject

class NewsFragment : BaseFragment() {

    private lateinit var viewModel: NewsListViewModel
    @Inject
    lateinit var viewModelFactory: NewsListViewModelFactory
    private var id: String? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(NewsListViewModel::class.java)
        arguments?.let {
            id = it.getString("id")
            position = it.getInt("position")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        if(position != null) {
            val news = viewModel.pagedList?.get(position!!)
            if(news != null) init(news)
        }
    }

    private fun init(news: News) {
        toolbar.title = news.webTitle

        if(!initDescription(news.fields?.thumbnail)) {
            if(!id.isNullOrEmpty()) {
//                viewModel.loadDescription(id!!, getString(R.string.api_key)).observe(viewLifecycleOwner, Observer { desc: String? ->
//                    if(desc != null) {
//                        news.fields?.body = desc
//                        initDescription(desc)
//                    }
//                })
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thumbnail.transitionName = news.id
        } else {
            ViewCompat.setTransitionName(thumbnail, news.id)
        }
        Picasso.get()
            .load(news.fields?.thumbnail)
            .into(thumbnail, object : Callback {
                override fun onSuccess() {
                    if(position == viewModel.position)
                        parentFragment?.startPostponedEnterTransition()
                }
                override fun onError(e: Exception?) {
                    if(position == viewModel.position)
                        parentFragment?.startPostponedEnterTransition()
                }
            })
    }

    private fun initDescription(bodyText: String?) : Boolean {
        if(bodyText != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                body.text = Html.fromHtml(bodyText, Html.FROM_HTML_MODE_LEGACY)
            } else {
                body.text = Html.fromHtml(bodyText)
            }
            return true
        }
        return false
    }

}
