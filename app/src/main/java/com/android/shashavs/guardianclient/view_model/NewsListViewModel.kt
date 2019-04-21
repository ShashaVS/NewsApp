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
import com.android.shashavs.guardianclient.repository.AppDatabase

class NewsListViewModel @Inject constructor(private val apiService: ApiService,
                                            private val appDatabase: AppDatabase) : ViewModel() {
    private val TAG = "NewsListViewModel"

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var factory: NetDataSourceFactory? = null
    var pagedList: PagedList<News>? = null
    var position: Int = 0

    fun initDataSourceLiveData(apiKey : String) : LiveData<PagedList<News>> {
        factory = NetDataSourceFactory(apiService, apiKey, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        return LivePagedListBuilder(factory!!, config).build()
    }

    fun search(query: String? = null) {
        if(factory?.query == query) return
        factory?.query = query
        pagedList?.dataSource?.invalidate()
    }

    fun loadDescription(id: String, apiKey : String): LiveData<String> {
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
    }

    override fun onCleared() {
        compositeDisposable.clear()
        pagedList = null
        super.onCleared()
    }
}
