package com.android.shashavs.guardianclient

import android.app.Activity
import android.app.Application
import com.android.shashavs.guardianclient.dagger.component.DaggerAppComponent
import com.android.shashavs.guardianclient.dagger.module.AppModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
         DaggerAppComponent.builder()
             .applicationBind(this)
             .appModule(AppModule())
             .build()
             .inject(this)
    }
}