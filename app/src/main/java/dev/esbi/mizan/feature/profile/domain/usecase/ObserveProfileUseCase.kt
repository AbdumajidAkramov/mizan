package dev.esbi.mizan.feature.profile.domain.usecase

import dev.esbi.mizan.feature.profile.domain.model.UserProfile
import dev.esbi.mizan.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe user profile
 */
class ObserveProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<UserProfile> {
        return repository.observeProfile()
    }
}
