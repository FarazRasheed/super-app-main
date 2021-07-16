package co.yap.networking.messages.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

class CreateForgotPasscodeOtpResponse:ApiResponse() {
   @SerializedName("data") var data:String?=""
}