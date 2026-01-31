package dev.esbi.mizan.feature.managecategories.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.managecategories.ManageCategoriesViewModel

@ScreenScope
@Subcomponent(modules = [ManageCategoriesModule::class])
interface ManageCategoriesComponent {

    val viewModel: ManageCategoriesViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): ManageCategoriesComponent
    }
}
