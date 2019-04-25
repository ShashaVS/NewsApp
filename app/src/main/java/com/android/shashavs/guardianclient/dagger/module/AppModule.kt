package com.android.shashavs.guardianclient.dagger.module

import android.arch.persistence.room.Room
import android.content.Context
import com.android.shashavs.guardianclient.App
import com.android.shashavs.guardianclient.dagger.scope.AppScope
import com.android.shashavs.guardianclient.repository.room.AppDatabase
import com.android.shashavs.guardianclient.repository.Repository
import com.android.shashavs.guardianclient.repository.retrofit.ApiService
import com.android.shashavs.guardianclient.repository.retrofit.Api
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

    @AppScope
    @Provides
    fun getAppDatabase(context: Context): AppDatabase
            = Room.databaseBuilder(context, AppDatabase::class.java,"news_database.db").build()

    @AppScope
    @Provides
    fun getRepository(apiService: ApiService, appDatabase: AppDatabase): Repository = Repository(apiService, appDatabase)

}