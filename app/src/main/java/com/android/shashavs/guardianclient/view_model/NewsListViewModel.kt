package com.android.shashavs.guardianclient.view_model

import android.arch.lifecycle.*

import android.arch.paging.LivePagedListBuilder
import com.android.shashavs.guardianclient.retrofit.AppDataSourceFactory
import com.android.shashavs.guardianclient.retrofit.objects.News
import javax.inject.Inject
import android.arch.paging.PagedList
import com.android.shashavs.guardianclient.repository.Repository

class NewsListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val TAG = "NewsListViewModel"

    private var factory: AppDataSourceFactory? = null
    var pagedList: PagedList<News>? = null
    var position: Int = 0

    val pagedConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(10)
        .build()

    fun initDataSourceLiveData(apiKey : String) : LiveData<PagedList<News>> {
        factory = AppDataSourceFactory(repository, apiKey)

        return LivePagedListBuilder(factory!!, pagedConfig)
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

/*    fun loadDescription(id: String, apiKey : String): LiveData<String> {
        val descLiveData: MutableLiveData<String> = MutableLiveData()
        apiService.getNews(id, "body", apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { response: Response<PageResponse<News>>? ->
                    if(response != null) {
                        try {
                            if(response.code() == 200) {
                                val pageResponse = response.body()?.response
                                val content = pageResponse?.content
                                if(content != null) {
                                    val description = content.fields?.body
                                    descLiveData.postValue(description)
                                }
                            }
                        } catch (e: JSONException) {
                            Log.e(TAG, "loadDescription response JSONException: ", e)
                        }
                    }
                },
                { error -> Log.e(TAG, "loadDescription error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
        return descLiveData
    }*/

    override fun onCleared() {
        repository.clear()
        pagedList = null
        super.onCleared()
    }
}
