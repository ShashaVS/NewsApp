package com.android.shashavs.guardianclient.retrofit

import android.arch.paging.DataSource
import com.android.shashavs.guardianclient.retrofit.objects.News
import io.reactivex.disposables.CompositeDisposable

class NetDataSourceFactory(private val apiService: ApiService,
                           val apiKey: String,
                           val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, News>() {

    private val TAG = "NetDataSourceFactory"

    override fun create(): DataSource<Int, News> {
        return NetDataSource(apiService, apiKey, compositeDisposable)
    }
}