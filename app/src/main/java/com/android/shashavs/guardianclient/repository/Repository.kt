package com.android.shashavs.guardianclient.repository

import android.util.Log
import com.android.shashavs.guardianclient.retrofit.ApiService
import com.android.shashavs.guardianclient.retrofit.objects.News
import com.android.shashavs.guardianclient.retrofit.objects.PageResponse
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

    fun cacheDataSource() = appDatabase.newsDao().getNews().create()

    fun clear() {
        compositeDisposable.clear()
    }
}