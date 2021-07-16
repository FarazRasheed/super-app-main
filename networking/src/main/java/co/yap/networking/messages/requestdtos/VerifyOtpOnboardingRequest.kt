package co.yap.networking.messages.requestdtos

data class VerifyOtpOnboardingRequest(val countryCode: String, val mobileNo: String, val otp: String)
