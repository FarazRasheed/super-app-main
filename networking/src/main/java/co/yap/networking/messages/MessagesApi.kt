package co.yap.networking.messages


import co.yap.networking.messages.requestdtos.*
import co.yap.networking.messages.responsedtos.FaqsResponse
import co.yap.networking.messages.responsedtos.HelpDeskResponse
import co.yap.networking.messages.responsedtos.OtpValidationResponse
import co.yap.networking.models.ApiResponse
import co.yap.networking.models.RetroApiResponse

interface MessagesApi {

    suspend fun createOtpOnboarding(createOtpOnboardingRequest: CreateOtpOnboardingRequest): RetroApiResponse<ApiResponse>
    suspend fun verifyOtpOnboarding(verifyOtpOnboardingRequest: VerifyOtpOnboardingRequest): RetroApiResponse<OtpValidationOnBoardingResponse>
    suspend fun createOtpGeneric(createOtpGenericRequest: CreateOtpGenericRequest): RetroApiResponse<ApiResponse>
    suspend fun createOtpGenericWithPhone(
        phone: String,
        createOtpGenericRequest: CreateOtpGenericRequest
    ): RetroApiResponse<ApiResponse>

    suspend fun verifyOtpGeneric(verifyOtpGenericRequest: VerifyOtpGenericRequest): RetroApiResponse<ApiResponse>
    suspend fun verifyOtpGenericWithPhone(
        phone: String,
        verifyOtpGenericRequest: VerifyOtpGenericRequest
    ): RetroApiResponse<ApiResponse>

    suspend fun createForgotPasscodeOTP(createForgotPasscodeOtpRequest: CreateForgotPasscodeOtpRequest): RetroApiResponse<ApiResponse>
    suspend fun verifyForgotPasscodeOtp(verifyForgotPasscodeOtpRequest: VerifyForgotPasscodeOtpRequest): RetroApiResponse<ApiResponse>
    suspend fun getHelpDeskContact(): RetroApiResponse<HelpDeskResponse>
    suspend fun getFaqsUrl(): RetroApiResponse<FaqsResponse>
}
