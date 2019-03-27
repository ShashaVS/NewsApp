package com.android.shashavs.guardianclient.dagger.module

import com.android.shashavs.guardianclient.dagger.scope.ActivityScope
import com.android.shashavs.guardianclient.view_model.NewsListViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule() {

    @ActivityScope
    @Binds
    abstract fun getNewsListViewModelFactory(newsListViewModelFactory: NewsListViewModelFactory) : NewsListViewModelFactory
}