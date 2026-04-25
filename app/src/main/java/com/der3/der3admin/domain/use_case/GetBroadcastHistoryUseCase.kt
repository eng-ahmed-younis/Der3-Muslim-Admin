package com.der3.der3admin.domain.use_case

import com.der3.der3admin.domain.models.BroadcastNotification
import com.der3.der3admin.domain.repo.BroadcastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBroadcastHistoryUseCase @Inject constructor(
    private val broadcastRepository: BroadcastRepository
) {
    operator fun invoke(): Flow<List<BroadcastNotification>> =
        broadcastRepository.getBroadcastHistory()
}