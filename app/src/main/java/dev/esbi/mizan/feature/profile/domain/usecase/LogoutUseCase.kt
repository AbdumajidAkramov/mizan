package dev.esbi.mizan.feature.profile.domain.usecase

import dev.esbi.mizan.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to logout user
 */
class LogoutUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
