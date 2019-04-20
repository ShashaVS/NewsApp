package com.android.shashavs.guardianclient.retrofit

import android.arch.paging.DataSource
import com.android.shashavs.guardianclient.retrofit.objects.News
import io.reactivex.disposables.CompositeDisposable

class NetDataSourceFactory(private val apiService: ApiService,
                           private val apiKey: String,
                           private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, News>() {

    private val TAG = "NetDataSourceFactory"
    var query: String? = null

    override fun create(): DataSource<Int, News> {
        return NetDataSource(apiService, apiKey, compositeDisposable, query)
    }
}