package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.domain.repository.ProfileRepository
import dev.esbi.mizan.feature.profile.data.repository.ProfileRepositoryImpl

@Module
abstract class ProfileModule {

    @Binds
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
}
