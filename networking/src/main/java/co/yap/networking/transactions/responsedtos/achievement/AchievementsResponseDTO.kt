package co.yap.networking.transactions.responsedtos.achievement

import co.yap.networking.models.ApiResponse

data class AchievementsResponseDTO(
    val data: List<Achievement>?
):ApiResponse()