package com.aliaksandramolchan.subwaystationsviewer.di.component

import android.app.Application
import com.aliaksandramolchan.subwaystationsviewer.App
import com.aliaksandramolchan.subwaystationsviewer.di.module.ActivityBuilder
import com.aliaksandramolchan.subwaystationsviewer.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton


@Component(modules = arrayOf(AndroidSupportInjectionModule::class, AppModule::class, ActivityBuilder::class))

@Singleton
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

    override fun inject(instance: DaggerApplication)
}
