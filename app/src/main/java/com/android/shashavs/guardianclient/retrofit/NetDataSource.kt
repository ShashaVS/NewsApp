package com.android.shashavs.guardianclient.retrofit

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.android.shashavs.guardianclient.retrofit.objects.News
import com.android.shashavs.guardianclient.retrofit.objects.PageResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import retrofit2.Response

class NetDataSource(private val apiService: ApiService,
                    private val apiKey: String,
                    private val compositeDisposable: CompositeDisposable,
                    private val query: String?) : PageKeyedDataSource<Int, News>() {

    private val TAG = "NetDataSource"

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, News>) {
        apiService.getNewsList(1, query,"thumbnail", apiKey)
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
                                    callback.onResult(results, null, pageResponse.currentPage)
                                }
                            }
                        } catch (e: JSONException) {
                            Log.e(TAG, "loadInitial response JSONException: ", e)
                        }
                    }
                },
                { error -> Log.e(TAG, "loadInitial error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        apiService.getNewsList(params.key.plus(1), query,"thumbnail", apiKey)
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
                                    callback.onResult(results, pageResponse.currentPage)
                                }
                            }
                        } catch (e: JSONException) {
                            Log.e(TAG, "loadInitial response JSONException: ", e)
                        }
                    }
                },
                { error -> Log.e(TAG, "loadInitial error: ", error) }
            )
            .apply {
                compositeDisposable.add(this)
            }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) { }

}