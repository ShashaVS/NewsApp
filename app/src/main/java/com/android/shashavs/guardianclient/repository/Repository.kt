package com.android.shashavs.guardianclient.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.android.shashavs.guardianclient.retrofit.ApiService
import com.android.shashavs.guardianclient.retrofit.objects.News
import com.android.shashavs.guardianclient.retrofit.objects.PageResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import org.json.JSONException
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService,
                                     private val appDatabase: AppDatabase) {
    private val TAG = "Repository"
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun refresh(apiKey : String, page: Int? = null) {
        apiService.getNewsList(page, null,"thumbnail", apiKey)
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
                                    results.forEach { news: News -> news.currentPage = pageResponse.currentPage }
                                    appDatabase.newsDao().insert(results)
                                }
                            }
                        } catch (e: JSONException) {
                            Log.e(TAG, "refresh response JSONException: ", e)
                        }
                    }
                },
                { error -> Log.e(TAG, "refresh error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
    }

    fun search(apiKey : String, page: Int? = null, query: String?, listener : (List<News>, Int) -> Unit) {
        apiService.getNewsList(page, query,"thumbnail", apiKey)
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
                                    results.forEach { news: News -> news.currentPage = pageResponse.currentPage }
                                    listener(results, pageResponse.currentPage)
                                }
                            }
                        } catch (e: JSONException) {
                            Log.e(TAG, "search response JSONException: ", e)
                        }
                    }
                },
                { error -> Log.e(TAG, "search error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
    }

    fun getDescription(apiKey : String, id: String) : LiveData<String> {
        val descLiveData = MutableLiveData<String>()
        Observable.fromCallable { appDatabase.newsDao().description(id) }
            .flatMap { news: News ->
                val description = news.fields?.body
                if(description == null) {
                    apiService.getNews(id, "body,thumbnail", apiKey)
                } else {
                    Observable.fromCallable {description}
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { response: Any? ->
                    if(response != null) {
                        if(response is String) {
                            descLiveData.postValue(response)
                        } else if (response is Response<*>) {
                            try {
                                if(response.code() == 200) {
                                    val pageResponse = (response.body() as PageResponse<*>).response
                                    val content = pageResponse.content as News
                                    val desc = content.fields?.body
                                    if(desc != null) {
                                        appDatabase.newsDao().addDesc(id, desc)
                                        descLiveData.postValue(desc)
                                    }
                                }
                            } catch (e: JSONException) {
                                Log.e(TAG, "getDescription response JSONException: ", e)
                            }
                        }
                    }
                },
                { error -> Log.e(TAG, "getDescription error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
        return descLiveData
    }

    fun cacheDataSource() = appDatabase.newsDao().getNews().create()

    fun clear() {
        compositeDisposable.clear()
    }
}