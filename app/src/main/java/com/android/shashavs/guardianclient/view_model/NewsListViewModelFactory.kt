package com.android.shashavs.guardianclient.view_model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.shashavs.guardianclient.retrofit.ApiService
import javax.inject.Inject
import javax.inject.Provider

class NewsListViewModelFactory @Inject constructor(private val apiService: Provider<ApiService>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NewsListViewModel(apiService.get()) as T
}