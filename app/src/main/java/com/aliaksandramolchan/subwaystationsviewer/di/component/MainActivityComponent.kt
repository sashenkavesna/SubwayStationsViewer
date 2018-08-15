package com.aliaksandramolchan.subwaystationsviewer.di.component

import com.aliaksandramolchan.subwaystationsviewer.view.MainActivity
import dagger.android.AndroidInjector
import dagger.Subcomponent
import com.aliaksandramolchan.subwaystationsviewer.di.module.MainActivityModule


@Subcomponent(modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}