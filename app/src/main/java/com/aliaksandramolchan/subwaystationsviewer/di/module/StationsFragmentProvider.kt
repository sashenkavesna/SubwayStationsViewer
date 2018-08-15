package com.aliaksandramolchan.subwaystationsviewer.di.module

import com.aliaksandramolchan.subwaystationsviewer.view.StationsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class StationsFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(StationsFragmentModule::class))
    internal abstract fun provideDetailFragmentFactory(): StationsFragment
}