package com.android.shashavs.guardianclient.repository

import android.arch.paging.PageKeyedDataSource
import com.android.shashavs.guardianclient.repository.data_objects.News

class NetDataSource(private val repository: Repository,
                    private val apiKey: String,
                    private val query: String?) : PageKeyedDataSource<Int, News>() {

    private val TAG = "NetDataSource"

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, News>) {
        repository.search(apiKey, 1, query) { list: List<News>, i: Int ->
            callback.onResult(list, null, i)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        repository.search(apiKey, params.key.plus(1), query) { list: List<News>, i: Int ->
            callback.onResult(list, i)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) { }
}