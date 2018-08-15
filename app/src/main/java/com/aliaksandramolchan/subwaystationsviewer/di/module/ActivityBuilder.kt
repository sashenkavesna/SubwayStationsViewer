package com.aliaksandramolchan.subwaystationsviewer.di.module

import com.aliaksandramolchan.subwaystationsviewer.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    //(modules = arrayOf(StationsFragmentProvider::class))
    internal abstract fun bindMainActivity(): MainActivity


}