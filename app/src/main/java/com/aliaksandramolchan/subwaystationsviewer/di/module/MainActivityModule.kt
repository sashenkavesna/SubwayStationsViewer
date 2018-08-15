package com.aliaksandramolchan.subwaystationsviewer.di.module

import com.aliaksandramolchan.subwaystationsviewer.view.MainActivity
import dagger.Module
import dagger.Provides


@Module
class MainActivityModule {

    @Provides
    internal fun provideMainActivity(mainActivity: MainActivity): MainActivity {
        return mainActivity
    }

    /*@Provides
    internal fun provideMainPresenter(mainView: MainView, apiService: ApiService): MainPresenter {
        return MainPresenterImpl(mainView, apiService)
    }*/
}