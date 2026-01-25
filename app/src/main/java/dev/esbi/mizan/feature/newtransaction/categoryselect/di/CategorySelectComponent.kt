package dev.esbi.mizan.feature.newtransaction.categoryselect.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.newtransaction.categoryselect.CategorySelectViewModel

/**
 * Dagger Subcomponent for CategorySelect screen.
 * This component is scoped to the screen lifecycle, ensuring that
 * each time the screen is entered, a new instance of scoped dependencies is created.
 */
@ScreenScope
@Subcomponent(modules = [CategorySelectModule::class])
internal interface CategorySelectComponent {

    val viewModel: CategorySelectViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): CategorySelectComponent
    }
}
