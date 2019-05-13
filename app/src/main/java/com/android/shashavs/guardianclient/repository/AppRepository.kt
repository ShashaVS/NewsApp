package com.android.shashavs.guardianclient.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.android.shashavs.guardianclient.repository.data_objects.Descripton
import com.android.shashavs.guardianclient.repository.room.AppDatabase
import com.android.shashavs.guardianclient.repository.retrofit.ApiService
import com.android.shashavs.guardianclient.repository.data_objects.News
import com.android.shashavs.guardianclient.repository.data_objects.PageResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import org.json.JSONException
import retrofit2.Response
import javax.inject.Inject

class AppRepository @Inject constructor(private val apiService: ApiService,
                                        private val appDatabase: AppDatabase) : Repository {
    private val TAG = "Repository"
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val refreshLiveData = MutableLiveData<Boolean>()

    override fun refresh(apiKey : String, page: Int?) {
        apiService.getNewsList(page, null,"thumbnail", apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { refreshLiveData.postValue(true) }
            .doFinally { refreshLiveData.postValue(false) }
            .subscribe(
                { response: Response<PageResponse<News>>? ->
                    if(response != null) {
                        try {
                            if(response.code() == 200) {
                                val pageResponse = response.body()?.response
                                val results = pageResponse?.results
                                if(results != null) {
                                    results.forEach { news: News -> news.currentPage = pageResponse.currentPage }
                                    val ids = appDatabase.newsDao().insert(results)
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

    override fun search(apiKey : String, page: Int?, query: String?, listener : (List<News>, Int, Int) -> Unit) {
        apiService.getNewsList(page, query,"thumbnail", apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { refreshLiveData.postValue(true) }
            .doFinally { refreshLiveData.postValue(false) }
            .subscribe(
                { response: Response<PageResponse<News>>? ->
                    if(response != null) {
                        try {
                            if(response.code() == 200) {
                                val pageResponse = response.body()?.response
                                val results = pageResponse?.results
                                if(results != null) {
                                    results.forEach { news: News -> news.currentPage = pageResponse.currentPage }
                                    listener(results, pageResponse.currentPage, pageResponse.total)
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

    override fun getDescription(apiKey : String, id: String) : LiveData<Descripton> {
        val descLiveData = MutableLiveData<Descripton>()
        var description: String? = null

        Observable.fromCallable { appDatabase.newsDao().description(id) }
            .flatMap { newsList: List<News> ->
                description = newsList.firstOrNull()?.fields?.body
                if(description == null) {
                    apiService.getNews(id, "body,thumbnail", apiKey)
                } else {
                    Observable.empty()
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { descLiveData.postValue(Descripton(true)) }
            .doFinally { descLiveData.postValue(Descripton(false, description)) }
            .subscribe(
                { response: Response<PageResponse<News>>? ->
                    if(response != null) {
                        try {
                            if(response.code() == 200) {
                                val pageResponse = response.body()?.response
                                description = pageResponse?.content?.fields?.body
                                if(description != null) {
                                    appDatabase.newsDao().addDesc(id, description!!)
                                }
                            }
                        } catch (e: JSONException) {
                            Log.e(TAG, "getDescription response JSONException: ", e)
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

    override fun getRefreshLiveData(): LiveData<Boolean> = refreshLiveData

    override fun cacheDataSource() = appDatabase.newsDao().getNews().create()

    override fun clear() {
        compositeDisposable.clear()
    }
}