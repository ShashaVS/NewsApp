package com.android.shashavs.guardianclient.repository

import android.arch.paging.PositionalDataSource
import com.android.shashavs.guardianclient.repository.data_objects.News

class NetPositionalDataSource(private val repository: Repository,
                              private val apiKey: String,
                              private val query: String?) : PositionalDataSource<News>() {

    private val TAG = "NetPositionalDataSource"

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<News>) {
        repository.search(apiKey, 1, query) { list: List<News>, position: Int, total: Int ->
            callback.onResult(list, params.requestedStartPosition, total)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<News>) {
        repository.search(apiKey, params.startPosition.div(params.loadSize).plus(1), query) {
                list: List<News>, position: Int, total: Int -> callback.onResult(list)
        }
    }

}