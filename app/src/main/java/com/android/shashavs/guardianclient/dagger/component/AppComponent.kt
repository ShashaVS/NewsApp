package com.android.shashavs.guardianclient.dagger.component

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import com.android.shashavs.guardianclient.App
import com.android.shashavs.guardianclient.dagger.module.*
import com.android.shashavs.guardianclient.dagger.scope.AppScope
import dagger.BindsInstance

@AppScope
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityModule::class,
    FragmentModule::class,
    AppModule::class
])
interface AppComponent : AndroidInjector<App> {

    override fun inject(app: App)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun applicationBind(app: App): Builder

        @BindsInstance
        fun appModule(appModule: AppModule): Builder
    }
}

