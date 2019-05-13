package com.android.shashavs.guardianclient.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.android.shashavs.guardianclient.repository.data_objects.Descripton
import com.android.shashavs.guardianclient.repository.data_objects.News

interface Repository {
    fun refresh(apiKey : String, page: Int? = null)
    fun search(apiKey : String, page: Int? = null, query: String?, listener : (List<News>, Int, Int) -> Unit)
    fun getDescription(apiKey : String, id: String) : LiveData<Descripton>
    fun getRefreshLiveData(): LiveData<Boolean>
    fun cacheDataSource() : DataSource<Int, News>
    fun clear()
}