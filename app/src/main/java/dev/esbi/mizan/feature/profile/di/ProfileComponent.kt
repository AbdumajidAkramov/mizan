package dev.esbi.mizan.feature.profile.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModel

@ScreenScope
@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {

    val viewModel: ProfileViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }
}
