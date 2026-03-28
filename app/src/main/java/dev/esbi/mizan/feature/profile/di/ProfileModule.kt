package dev.esbi.mizan.feature.profile.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.domain.repository.ProfileRepository
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModel
import dev.esbi.mizan.feature.profile.presentation.store.ProfileStoreFactory
import kotlinx.coroutines.CoroutineDispatcher

@Module
object ProfileModule {

    @Provides
    @ScreenScope
    fun provideProfileStoreFactory(
        storeFactory: StoreFactory,
        profileRepository: ProfileRepository,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): ProfileStoreFactory {
        return ProfileStoreFactory(storeFactory, profileRepository, mainDispatcher)
    }

    @Provides
    @ScreenScope
    fun provideProfileViewModel(
        storeFactory: ProfileStoreFactory
    ): ProfileViewModel {
        return ProfileViewModel(storeFactory)
    }
}
