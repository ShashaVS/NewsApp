package com.android.shashavs.guardianclient.view_model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.android.shashavs.guardianclient.retrofit.ApiService
import com.android.shashavs.guardianclient.retrofit.NetDataSourceFactory
import com.android.shashavs.guardianclient.retrofit.objects.News
import com.android.shashavs.guardianclient.retrofit.objects.PageResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import retrofit2.Response
import javax.inject.Inject
import android.arch.paging.PagedList

class NewsListViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {
    private val TAG = "NewsListViewModel"

    private val newsLiveData: MutableLiveData<List<News>> = MutableLiveData()
    private val newsList: MutableList<News> = mutableListOf()
    private val newsMap: MutableMap<String, MutableLiveData<News>> = mutableMapOf()

    private var currentPage: Int? = null
    var position: Int = 0

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var factory: NetDataSourceFactory? = null
    var pagedList: PagedList<News>? = null

    fun getNewsLiveData(): LiveData<List<News>> = newsLiveData

    fun getNewsSize() = newsList.size

    fun initDataSourceLiveData(apiKey : String) : LiveData<PagedList<News>> {
        factory = NetDataSourceFactory(apiService, apiKey, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        return LivePagedListBuilder(factory!!, config)
            .build()
    }

    fun search(query: String? = null) {
        if(factory?.query == query) return
        factory?.query = query
        pagedList?.dataSource?.invalidate()
    }

    fun getNewsList(apiKey : String) {
        apiService.getNewsList(currentPage?.plus(1), null, "thumbnail", apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { response: Response<PageResponse<News>>? ->
                    if(response != null) {
                        try {
                            if(response.code() == 200) {
                                val pageResponse = response.body()?.response
                                val results = pageResponse?.results
                                if(results != null) {
                                    updateData(results, pageResponse.currentPage)
                                }
                            }
                        } catch (e: JSONException) {
                            Log.e(TAG, "getNewsList response JSONException: ", e)
                        }
                    }
                },
                { error -> Log.e(TAG, "getNewsList error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
    }

    fun getNews(id: String, apiKey : String): LiveData<News>? {
        val newsLiveData: MutableLiveData<News>?
        if(newsMap.containsKey(id)) {
            newsLiveData = newsMap[id]
        } else {
            newsLiveData = MutableLiveData()
            newsMap[id] = newsLiveData
        }
        val news = newsList.find { it.id == id }
        val body = news?.fields?.body

        if(body != null) {
            newsLiveData?.postValue(news)
        } else {
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
                                        news?.fields?.body = content.fields?.body
                                        newsLiveData?.postValue(news)
                                    }
                                }
                            } catch (e: JSONException) {
                                Log.e(TAG, "getNews response JSONException: ", e)
                            }
                        }
                    },
                    { error -> Log.e(TAG, "getNews error: ", error) }
                )
                .apply {
                    compositeDisposable.add(this)
                }
        }
        return newsLiveData
    }

    private fun updateData(data: List<News>, page: Int) {
        currentPage = page
        if(page == 1) newsList.clear()
        newsList.addAll(data)
        newsLiveData.postValue(newsList)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        pagedList = null
        newsList.clear()
        newsMap.clear()
        super.onCleared()
    }
}
