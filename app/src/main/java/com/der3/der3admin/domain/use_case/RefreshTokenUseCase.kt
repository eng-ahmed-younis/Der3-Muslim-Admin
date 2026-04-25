package com.der3.der3admin.domain.use_case


import com.der3.der3admin.domain.models.TokenResult
import com.der3.der3admin.domain.repo.TokenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Result<TokenResult> {
        return tokenRepository.refreshToken()
    }
}