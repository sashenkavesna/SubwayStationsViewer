package com.aliaksandramolchan.subwaystationsviewer.di.module

import com.aliaksandramolchan.subwaystationsviewer.view.StationsFragment
import dagger.Module
import dagger.Provides


@Module
//(includes = arrayOf(AppModule::class))
object StationsFragmentModule {

    /* @Provides
      fun provideLocationService(): LocationService {
          return LocationService()
      }*/


    @Provides
    internal fun provideStationsFragment(stationsFragment: StationsFragment): StationsFragment {
        return stationsFragment
    }

    /* @Provides
     fun provideStationsViewModule(appModule: AppModule,
                                   isConnected: Boolean): StationsViewModel {
         return StationsViewModel(=, isConnected)
     }



     @Provides
     fun provideStationsAdapter(): StationsAdapter {
         return StationsAdapter()
     }*/
}

