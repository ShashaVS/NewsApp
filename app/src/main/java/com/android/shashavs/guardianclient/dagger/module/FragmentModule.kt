package com.android.shashavs.guardianclient.dagger.module

import com.android.shashavs.guardianclient.dagger.scope.FragmentScope
import com.android.shashavs.guardianclient.fragments.news_detail.NewsFragment
import com.android.shashavs.guardianclient.fragments.news_detail.PagerFragment
import com.android.shashavs.guardianclient.fragments.news_list.NewsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNewsListFragment(): NewsListFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePagerFragment(): PagerFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNewsFragment(): NewsFragment
}