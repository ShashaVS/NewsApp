package com.android.shashavs.guardianclient.fragments.news_detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.support.v4.app.Fragment
import android.support.v4.app.SharedElementCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.shashavs.guardianclient.R
import com.android.shashavs.guardianclient.base.BaseFragment
import com.android.shashavs.guardianclient.view_model.NewsListViewModel
import com.android.shashavs.guardianclient.view_model.NewsListViewModelFactory
import com.android.shashavs.guardianclient.repository.data_objects.News
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

        prepareTransitions()

        viewModel.pagedListLiveData?.observe(viewLifecycleOwner, Observer { pagedList: PagedList<News>? ->
            if(viewPager.adapter == null) {
                // init
                viewPager.adapter = PagerAdapter(childFragmentManager).also {
                    it.submit(pagedList)
                }
                viewPager.currentItem = viewModel.position
            } else {
                (viewPager.adapter as PagerAdapter).submit(pagedList)
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
