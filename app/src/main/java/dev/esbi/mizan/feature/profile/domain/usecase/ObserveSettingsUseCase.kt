package dev.esbi.mizan.feature.profile.domain.usecase

import dev.esbi.mizan.feature.profile.domain.model.AppSettings
import dev.esbi.mizan.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe app settings
 */
class ObserveSettingsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<AppSettings> {
        return repository.observeSettings()
    }
}
