package com.android.shashavs.guardianclient.view_model

import android.arch.lifecycle.*

import android.arch.paging.LivePagedListBuilder
import com.android.shashavs.guardianclient.repository.AppDataSourceFactory
import com.android.shashavs.guardianclient.repository.data_objects.News
import javax.inject.Inject
import android.arch.paging.PagedList
import com.android.shashavs.guardianclient.repository.Repository
import com.android.shashavs.guardianclient.repository.data_objects.Descripton

class NewsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val TAG = "NewsListViewModel"

    var factory: AppDataSourceFactory? = null
    var position: Int = 0
    var pagedListLiveData: LiveData<PagedList<News>>? = null

    private val pagedConfig = PagedList.Config.Builder()
        .setPrefetchDistance(15)
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

    fun initRefresh(apiKey : String) = repository.refresh(apiKey, 1)

    fun refreshLiveData() = repository.getRefreshLiveData()

    fun search(query: String? = null) = factory?.search(query)

    fun getDescription(apiKey : String, id: String): LiveData<Descripton> = repository.getDescription(apiKey, id)

    fun clear() {
        repository.clear()
        factory = null
        pagedListLiveData = null
    }

    override fun onCleared() {
        clear()
        super.onCleared()
    }
}
