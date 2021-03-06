package com.android.shashavs.guardianclient.fragments.news_detail

import android.arch.lifecycle.Observer
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
import com.android.shashavs.guardianclient.repository.data_objects.Descripton
import com.android.shashavs.guardianclient.view_model.NewsViewModel
import com.android.shashavs.guardianclient.view_model.NewsListViewModelFactory
import com.android.shashavs.guardianclient.repository.data_objects.News
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_news.*
import java.lang.Exception
import javax.inject.Inject

class NewsFragment : BaseFragment() {

    private lateinit var viewModel: NewsViewModel
    @Inject
    lateinit var viewModelFactory: NewsListViewModelFactory
    private var position: Int? = null
    private var news: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(NewsViewModel::class.java)
        arguments?.let {
            position = it.getInt("position")
            news = it.getSerializable("news") as News?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener {
            viewModel.position = position ?: 0
            findNavController().popBackStack()
        }
        if(news != null) init()
    }

    private fun init() {
        toolbar.title = news?.webTitle

        viewModel.getDescription(getString(R.string.api_key), news?.id!!)
            .observe(viewLifecycleOwner, Observer { descripton: Descripton? ->
                if(descripton != null) initDescription(descripton)
            })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thumbnail.transitionName = news?.id
        } else {
            ViewCompat.setTransitionName(thumbnail, news?.id)
        }
        Picasso.get()
            .load(news?.fields?.thumbnail)
            .fit()
            .centerCrop()
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

        test.text = news?.currentPage.toString().plus("/").plus(position)
    }

    private fun initDescription(descripton: Descripton) {
        progressBar.visibility = if(descripton.refresh) View.VISIBLE else View.GONE
        if(descripton.data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                body.text = Html.fromHtml(descripton.data, Html.FROM_HTML_MODE_LEGACY)
            } else {
                body.text = Html.fromHtml(descripton.data)
            }
            body.visibility = View.VISIBLE
        }
    }

}
