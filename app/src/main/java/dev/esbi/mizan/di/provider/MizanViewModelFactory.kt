package dev.esbi.mizan.di.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class MizanViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 1. To'g'ridan-to'g'ri mos keluvchi provayderni qidirish
        var creator = creators[modelClass]
        
        // 2. Agar topilmasa, subclasslarni tekshirish (masalan, inherit bo'lsa)
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        
        if (creator == null) {
            throw IllegalArgumentException("unknown model class $modelClass")
        }
        
        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}