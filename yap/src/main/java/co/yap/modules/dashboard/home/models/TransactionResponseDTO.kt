package co.yap.modules.dashboard.home.models

import co.yap.modules.onboarding.models.TransactionModel
import co.yap.networking.models.ApiResponse

data class TransactionResponseDTO( val data: ArrayList<TransactionModel>):ApiResponse() {
}