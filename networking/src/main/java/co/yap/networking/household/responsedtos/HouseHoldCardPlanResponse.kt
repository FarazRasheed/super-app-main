package co.yap.networking.household.responsedtos

import com.google.gson.annotations.SerializedName

data class HouseHoldCardPlanResponse(
    @SerializedName("data")
    var data: List<HouseHoldPlan>? = ArrayList()
)