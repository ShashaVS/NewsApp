package com.android.shashavs.guardianclient.view_model

import android.arch.lifecycle.*

import android.arch.paging.LivePagedListBuilder
import com.android.shashavs.guardianclient.repository.AppDataSourceFactory
import com.android.shashavs.guardianclient.repository.retrofit.objects.News
import javax.inject.Inject
import android.arch.paging.PagedList
import com.android.shashavs.guardianclient.repository.Repository

class NewsListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val TAG = "NewsListViewModel"

    private var factory: AppDataSourceFactory? = null
    var pagedList: PagedList<News>? = null
    var position: Int = 0
    var pagedListLiveData: LiveData<PagedList<News>>? = null

    private val pagedConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(10)
        .build()

    fun initDataSourceLiveData(apiKey : String) {
        factory = AppDataSourceFactory(repository, apiKey)

        pagedListLiveData = LivePagedListBuilder(factory!!, pagedConfig)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<News>() {

                override fun onItemAtEndLoaded(itemAtEnd: News) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    repository.refresh(apiKey, itemAtEnd.currentPage?.plus(1))
                }

            })
            .build()
    }

    fun refresh(apiKey : String) = repository.refresh(apiKey, 1)

    fun search(query: String? = null) {
        if(factory?.query == query) return
        factory?.query = query
        pagedList?.dataSource?.invalidate()
    }

    fun getDescription(apiKey : String, id: String): LiveData<String> = repository.getDescription(apiKey, id)

    fun refreshLiveData() = repository.getRefreshLiveData()

    override fun onCleared() {
        repository.clear()
        pagedList = null
        super.onCleared()
    }
}
