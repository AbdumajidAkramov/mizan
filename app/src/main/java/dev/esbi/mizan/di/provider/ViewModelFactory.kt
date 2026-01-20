package dev.esbi.mizan.di.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.esbi.mizan.navigation.NavRoute
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ViewModelFactory @Inject constructor(
    private val viewModelFactories: Set<@JvmSuppressWildcards ViewModelProvider.Factory>
) {
    @Suppress("UNCHECKED_CAST")
    fun <T : ViewModel> create(key: NavRoute, modelClass: Class<T>): T {
        val viewModelProvider = viewModelFactories.find { it.javaClass.simpleName == key.javaClass.simpleName }
            ?: throw IllegalArgumentException("Unknown ViewModel key: $key")
        return modelClass.cast(viewModelProvider.create(modelClass))
    }
}
