package com.android.shashavs.guardianclient.fragments.news_list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.ImageView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.android.shashavs.guardianclient.R

import com.android.shashavs.guardianclient.base.BaseFragment
import com.android.shashavs.guardianclient.view_model.NewsViewModel
import com.android.shashavs.guardianclient.view_model.NewsListViewModelFactory
import com.android.shashavs.guardianclient.repository.data_objects.News
import kotlinx.android.synthetic.main.fragment_item_list.*
import javax.inject.Inject

class NewsListFragment : BaseFragment() {

    private lateinit var viewModel: NewsViewModel
    @Inject
    lateinit var viewModelFactory: NewsListViewModelFactory
    private var isTablet = false
    private var searchView: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isTablet = isTablet()
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(NewsViewModel::class.java)
        viewModel.initRefresh(getString(R.string.api_key))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        init()
    }

    private fun init() {
        NewsListAdapter {position: Int, imageView: ImageView? ->
            if(imageView != null) {
                viewModel.position = position

                val transitionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.transitionName
                } else {
                    ViewCompat.getTransitionName(imageView)
                }
                val extras = FragmentNavigatorExtras(imageView to transitionName!!)
                findNavController().navigate(R.id.action_newsListFragment_to_pagerFragment3,
                    null,
                    null,
                    extras)

            } else if(position == viewModel.position) {
                startPostponedEnterTransition()
            }
        }.apply {
            list.adapter = this
        }

        list.layoutManager = if(isTablet) GridLayoutManager(context, 2) else LinearLayoutManager(context)
        list.setHasFixedSize(true)

        if(viewModel.pagedListLiveData == null) {
            viewModel.initDataSourceLiveData(getString(R.string.api_key))
        } else {
            prepareTransitions()
            // scroll to position
            list.post {
                list.layoutManager?.scrollToPosition(viewModel.position)
            }
        }

        swipeRefresh.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        swipeRefresh.setOnRefreshListener {
            viewModel.initRefresh(getString(R.string.api_key))
        }

        viewModel.pagedListLiveData?.observe(viewLifecycleOwner, Observer { pagedList: PagedList<News>? ->
            (list.adapter as NewsListAdapter).submitList(pagedList)
        })

        viewModel.refreshLiveData().observe(viewLifecycleOwner, Observer { refresh: Boolean? ->
            swipeRefresh.isRefreshing = refresh ?: false
        })
    }

    private fun isTablet() =
        resources.configuration.screenLayout.and(Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

    private fun prepareTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
        postponeEnterTransition()

        setExitSharedElementCallback(object : SharedElementCallback() {

            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                val selectedViewHolder = list.findViewHolderForAdapterPosition(viewModel.position)
                val view = selectedViewHolder?.itemView ?: return
                sharedElements?.put(names?.get(0)!!, view.findViewById(R.id.thumbnail))
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(list != null) {
            viewModel.position = (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
        searchView = menu?.findItem(R.id.search)

        (searchView?.actionView as SearchView).setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query)
                return true
            }
            override fun onQueryTextChange(query: String?) = false
        })

        searchView?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.search(null)
                return true
            }
            override fun onMenuItemActionExpand(item: MenuItem?) = true
        })
    }

    private fun clear() {
        list.adapter = null
        setExitSharedElementCallback(null)
        swipeRefresh.setOnRefreshListener(null)
        (searchView?.actionView as SearchView).setOnQueryTextListener(null)
        searchView?.setOnActionExpandListener(null)
    }

    override fun onDestroyView() {
        clear()
        super.onDestroyView()
    }

}
