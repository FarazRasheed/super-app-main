package co.yap.networking.messages

import co.yap.networking.messages.requestdtos.*
import co.yap.networking.messages.responsedtos.*
import co.yap.networking.models.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface MessagesRetroService {

    // Create otp for mobile number
    @POST(MessagesRepository.URL_CREATE_OTP_ONBOARDING)
    suspend fun createOtpOnboarding(@Body createOtpOnboardingRequest: CreateOtpOnboardingRequest): Response<ApiResponse>

    // Verify otp for mobile number
    @PUT(MessagesRepository.URL_VERIFY_OTP_ONBOARDING)
    suspend fun verifyOtpOnboarding(@Body verifyOtpOnboardingRequest: VerifyOtpOnboardingRequest): Response<OtpValidationOnBoardingResponse>

    // Create otp request
    @POST(MessagesRepository.URL_CREATE_OTP_GENERIC)
    suspend fun createOtpGeneric(@Body createOtpGenericRequest: CreateOtpGenericRequest): Response<ApiResponse>

    // Create otp request for change mobile number
    @POST(MessagesRepository.URL_CREATE_OTP_GENERIC_WITH_PHONE)
    suspend fun createOtpGenericWithPhone(@Path("phone") phone: String, @Body createOtpGenericRequest: CreateOtpGenericRequest): Response<ApiResponse>

    // Verify otp request
    @PUT(MessagesRepository.URL_VERIFY_OTP_GENERIC)
    suspend fun verifyOtpGeneric(@Body verifyOtpGenericRequest: VerifyOtpGenericRequest): Response<ValidateDeviceResponse>

    // Verify otp request for change mobile number request
    @PUT(MessagesRepository.URL_VERIFY_OTP_GENERIC_WITH_PHONE)
    suspend fun verifyOtpGenericWithPhone(
        @Path("phone") phone: String, @Body verifyOtpGenericRequest: VerifyOtpGenericRequest
    ): Response<ValidateDeviceResponse>

    //forgot passcode create otp
    @POST(MessagesRepository.URL_FORGOT_PASSCODE)
    suspend fun createForgotPasscodeOTP(@Body createForgotPasscodeOtpRequest: CreateForgotPasscodeOtpRequest): Response<CreateForgotPasscodeOtpResponse>

    @PUT(MessagesRepository.URL_VERIFY_FORGOT_PASSCODE_OTP)
    suspend fun verifyForgotPasscodeOtp(@Body verifyForgotPasscodeOtpRequest: VerifyForgotPasscodeOtpRequest): Response<OtpValidationResponse>

    @GET(MessagesRepository.URL_HELP_DESK_PHONE)
    suspend fun getHelpDeskContact(): Response<HelpDeskResponse>

    @GET(MessagesRepository.URL_FAQS)
    suspend fun getFaqs(): Response<FaqsResponse>
}