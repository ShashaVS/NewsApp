package com.android.shashavs.guardianclient.dagger.module

import android.content.Context
import com.android.shashavs.guardianclient.App
import com.android.shashavs.guardianclient.dagger.scope.AppScope
import com.android.shashavs.guardianclient.retrofit.ApiService
import com.android.shashavs.guardianclient.retrofit.Api
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @AppScope
    @Provides
    fun getContext(app: App) = app.applicationContext

    @AppScope
    @Provides
    fun getApiService(context: Context): ApiService = Api(context).getApiService()

}