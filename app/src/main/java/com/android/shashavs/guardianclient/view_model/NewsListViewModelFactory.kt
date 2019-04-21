package com.android.shashavs.guardianclient.view_model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.shashavs.guardianclient.repository.AppDatabase
import com.android.shashavs.guardianclient.retrofit.ApiService
import javax.inject.Inject
import javax.inject.Provider

class NewsListViewModelFactory @Inject constructor(private val apiService: Provider<ApiService>,
                                                   private val appDatabase: Provider<AppDatabase>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NewsListViewModel(apiService.get(), appDatabase.get()) as T
}