package com.android.shashavs.guardianclient.repository

import android.arch.paging.DataSource
import com.android.shashavs.guardianclient.repository.retrofit.objects.News

class AppDataSourceFactory(private val repository: Repository,
                           private val apiKey: String) : DataSource.Factory<Int, News>() {

    private val TAG = "NetDataSourceFactory"
    var query: String? = null

    override fun create(): DataSource<Int, News> {
        return if(query.isNullOrEmpty())
            repository.cacheDataSource()
        else
            NetDataSource(repository, apiKey, query)
    }
}