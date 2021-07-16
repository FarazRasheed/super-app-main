package co.yap.networking.messages

import co.yap.networking.messages.responsedtos.OtpValidation
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class OtpValidationOnBoardingResponse(@SerializedName("data") var data: OtpValidation? = OtpValidation()) :
    ApiResponse()