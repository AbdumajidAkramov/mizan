package dev.esbi.mizan.feature.profile.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModel
import dev.esbi.mizan.feature.profile.presentation.store.ProfileStoreFactory

@Module
object ProfileModule {

    @Provides
    @ScreenScope
    fun provideProfileViewModel(
        storeFactory: ProfileStoreFactory
    ): ProfileViewModel {
        return ProfileViewModel(storeFactory)
    }
}
