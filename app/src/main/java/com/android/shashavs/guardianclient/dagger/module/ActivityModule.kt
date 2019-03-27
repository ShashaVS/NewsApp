package com.android.shashavs.guardianclient.dagger.module

import com.android.shashavs.guardianclient.MainActivity
import com.android.shashavs.guardianclient.dagger.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ViewModelFactoryModule::class
    ])
    abstract fun contributeActivityAndroidInjector(): MainActivity
}