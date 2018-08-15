package com.aliaksandramolchan.subwaystationsviewer.di.module

import android.app.Application
import android.content.Context
import com.aliaksandramolchan.subwaystationsviewer.di.component.MainActivityComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(subcomponents = arrayOf(MainActivityComponent::class))
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

}