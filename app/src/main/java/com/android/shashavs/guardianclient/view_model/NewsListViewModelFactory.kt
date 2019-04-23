package com.android.shashavs.guardianclient.view_model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.shashavs.guardianclient.repository.Repository
import javax.inject.Inject
import javax.inject.Provider

class NewsListViewModelFactory @Inject constructor(private val repository: Provider<Repository>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NewsListViewModel(repository.get()) as T
}