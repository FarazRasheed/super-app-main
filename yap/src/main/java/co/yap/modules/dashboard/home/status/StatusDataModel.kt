package co.yap.modules.dashboard.home.status

import android.graphics.drawable.Drawable

data class StatusDataModel(
    val stage: PaymentCardOnboardingStage? = null,
    val statusTitle: String? = null,
    val statusDescription: String? = null,
    val statusAction: String? = null,
    val progressStatus: StageProgress,
    val statusDrawable: Drawable,
    val hideLine: Boolean = false
)
