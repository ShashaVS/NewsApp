package com.android.shashavs.guardianclient.fragments.news_detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.support.v4.app.Fragment
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.shashavs.guardianclient.R
import com.android.shashavs.guardianclient.base.BaseFragment
import com.android.shashavs.guardianclient.view_model.NewsListViewModel
import com.android.shashavs.guardianclient.view_model.NewsListViewModelFactory
import com.android.shashavs.guardianclient.retrofit.objects.News
import kotlinx.android.synthetic.main.fragment_pager.*
import javax.inject.Inject

class PagerFragment : BaseFragment() {

    private lateinit var viewModel: NewsListViewModel
    @Inject
    lateinit var viewModelFactory: NewsListViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(NewsListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.pagedList != null) init(viewModel.pagedList!!)
    }

    private fun init(newsList: List<News>) {
        prepareTransitions()

        val adapter = PagerAdapter(childFragmentManager, newsList)
        viewPager.adapter = adapter
        viewPager.currentItem = viewModel.position

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(position: Int) { }

            override fun onPageScrolled(position: Int, p1: Float, p2: Int) { }

            override fun onPageSelected(position: Int) {
                viewModel.position = viewPager.currentItem
            }
        })
    }

    private fun prepareTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
        postponeEnterTransition()

        setEnterSharedElementCallback(object : SharedElementCallback() {

            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                val currentFragment = viewPager.adapter?.instantiateItem(viewPager, viewModel.position) as Fragment
                val view = currentFragment.view ?: return
                sharedElements?.put(names?.get(0)!!, view.findViewById(R.id.thumbnail))
            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(viewPager != null) {
            viewModel.position = viewPager.currentItem
        }
        super.onSaveInstanceState(outState)
    }

}
