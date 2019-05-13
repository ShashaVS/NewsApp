package com.android.shashavs.guardianclient.repository

import android.arch.paging.DataSource
import com.android.shashavs.guardianclient.repository.data_objects.News

class AppDataSourceFactory(private val repository: Repository,
                           private val apiKey: String) : DataSource.Factory<Int, News>() {

    private val TAG = "NetDataSourceFactory"
    private var query: String? = null
    private var dataSource : DataSource<Int, News>? = null

    override fun create(): DataSource<Int, News> {
        dataSource = if(query.isNullOrEmpty())
            repository.cacheDataSource()
        else
            NetPositionalDataSource(repository, apiKey, query)

        return dataSource!!
    }

    fun search(query: String? = null) {
        if(this.query == query) return
        this.query = query
        dataSource?.invalidate()
    }
}