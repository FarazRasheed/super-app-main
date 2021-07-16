package co.yap.networking.customers

import co.yap.networking.BaseRepository
import co.yap.networking.CookiesManager
import co.yap.networking.RetroNetwork
import co.yap.networking.customers.requestdtos.*
import co.yap.networking.customers.responsedtos.*
import co.yap.networking.customers.responsedtos.beneficiary.BankParamsResponse
import co.yap.networking.customers.responsedtos.currency.CurrenciesByCodeResponse
import co.yap.networking.customers.responsedtos.currency.CurrenciesResponse
import co.yap.networking.customers.responsedtos.documents.GetMoreDocumentsResponse
import co.yap.networking.customers.responsedtos.sendmoney.*
import co.yap.networking.customers.responsedtos.tax.TaxInfoResponse
import co.yap.networking.messages.responsedtos.OtpValidationResponse
import co.yap.networking.models.ApiResponse
import co.yap.networking.models.RetroApiResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CustomersRepository : BaseRepository(), CustomersApi {

    const val URL_SIGN_UP = "/customers/api/profile"
    const val URL_SEND_VERIFICATION_EMAIL = "/customers/api/sign-up/email"
    const val URL_ACCOUNT_INFO = "/customers/api/accounts"
    const val URL_POST_DEMOGRAPHIC_DATA_SIGN_IN = "/customers/api/demographics/device-login"
    const val URL_POST_DEMOGRAPHIC_DATA = "/customers/api/demographics/"
    const val URL_VALIDATE_DEMOGRAPHIC_DATA =
        "customers/api/demographics/validate/user-device/{device_id}"
    const val URL_GET_DOCUMENTS = "customers/api/customer-documents"
    const val URL_UPLOAD_DOCUMENTS = "customers/api/v2/documents"
    const val URL_GET_MORE_DOCUMENTS = "customers/api/document-information"
    const val URL_UPLOAD_PROFILE_PICTURE = "customers/api/customers/profile-picture"
    const val URL_DELETE_PROFILE_PICTURE = "customers/api/customers/profile-picture"
    const val URL_VALIDATE_PHONE_NUMBER = "customers/api/validate-mobile-number"
    const val URL_VALIDATE_EMAIL = "customers/api/validate-email"
    const val URL_CHANGE_MOBILE_NUMBER =
        "customers/api/change-mobile-number/{country-code}/{mobile-number}"
    const val URL_CHANGE_VERIFIED_EMAIL = "customers/api/change-email/{email}"
    const val URL_CHANGE_UNVERIFIED_EMAIL = "customers/api/change-unverified-email"
    const val URL_Y2Y_BENEFICIARIES = "customers/api/y2y-beneficiaries"
    const val URL_Y2Y_RECENT_BENEFICIARIES = "customers/api/recent-beneficiaries/y2y"
    const val URL_DELETE_BENEFICIARIE = "customers/api/mastercard/beneficiaries/{cardId}"
    const val URL_TOPUP_BENEFICIARIES = "customers/api/mastercard/beneficiaries"
    const val URL_CREATE_BENEFICIARIY = "customers/api/mastercard/beneficiaries"
    const val URL_CARDS_LIMITS = "customers/api/mastercard/beneficiaries/limits"
    const val URL_GET_COUNTRY_DATA_WITH_ISO_DIGIT =
        "customers/api/countries/CountryCode2Digit/{country-code}"
    const val URL_GET_COUNTRY_TRANSACTION_LIMITS =
        "/customers/api/bank-transfer/transaction/limit"

    const val URL_DETECT = "digi-ocr/detect/"
    const val URL_SAVE_REFERAL_INVITATION = "/customers/api/save-referral-invitation"

    const val URL_GET_ALL_BENEFICIARIES = "/customers/api/beneficiaries/bank-transfer"
    const val URL_GET_RECENT_BENEFICIARIES = "/customers/api/beneficiaries/bank-transfer/recent"
    const val URL_GET_BENEFICIARY_BY_ID = "/customers/api/beneficiaries/{beneficiary-id}"
    const val URL_DELETE_BENEFICIARY_BY_ID =
        "/customers/api/beneficiaries/bank-transfer/{beneficiary-id}"
    const val URL_EDIT_BENEFICIARY_BY_ID = "/customers/api/beneficiaries/bank-transfer"
    const val URL_GET_COUNTRIES = "/customers/api/bank-transfer/countries"
    const val URL_ADD_BENEFICIARY = "/customers/api/beneficiaries/bank-transfer"
    const val URL_SEARCH_BANK_PARAMS = "/customers/api/other_bank/params"
    const val URL_SEARCH_BANKS = "/customers/api/other_bank/query"
    const val URL_VALIDATE_BENEFICIARY = "customers/api/validate/bank-transfer/beneficiary-details"

    val URL_GET_TRANSFER_REASONS = "/transactions/api/product-codes/{product-code}/purpose-reasons"
    val URL_INTERNAL_TRANSFER = "/transactions/api/internal-transfer"
    val URL_SEND_MONEY_UAEFT = "/transactions/api/uaefts"
    val URL_GET_FEE = "/transactions/api/product-codes/{product-code}/fees"
    val URL_BENEFICIARY_CHECK_OTP_STATUS = "customers/api/beneficiaries/bank-transfer/otp-req"


    val URL_RMT = "transactions/api/rmt"
    val URL_SWIFT = "transactions/api/swift "
    val URL_CASH_PICKUP = "/transactions/api/cashpayout "
    val URL_CASH_PICKUP_PARTNER =
        "customers/api/pickup-partner/currency-code/{currency-code}/country-code/{country-code} "

    const val URL_CURRENCIES_BY_COUNTRY_CODE =
        "customers/api/country/{country}/currencies"

    const val URL_VERIFY_HOUSEHOLD_MOBILE = "customers/api/on-board/verify/household-mobile"
    const val URL_VERIFY_PARENT_HOUSEHOLD_MOBILE = "customers/api/verify/parent-mobile-no/household"
    const val URL_HOUSEHOLD_USER_ONBOARD = "customers/api/on-board/household"
    const val URL_ADD_HOUSEHOLD_EMAIL = "customers/api/on-board/household-email"
    const val URL_CREATE_HOUSEHOLD_PASSCODE = "customers/api/on-board/household-passcode"
    const val URL_SANCTIONED_COUNTRIES = "customers/api/countries/sanctioned"
    const val URL_BIRTH_INFO = "customers/api/customer-birth-info"
    const val URL_TAX_INFO = "customers/api/tax-information"
    const val URL_CITIES = "customers/api/cities"
    const val URL_TAX_REASONS = "customers/api/tin-reasons"

    /*
   * Url's that comes from admin repo
   * */
    const val URL_VERIFY_USERNAME = "/customers/api/verify-user"
    const val URL_FORGOT_PASSCODE = "/customers/api/forgot-password"
    const val URL_VALIDATE_CURRENT_PASSCODE = "/customers/api/user/verify-passcode"
    const val URL_CHANGE_PASSCODE = "/customers/api/user/change-password"
    const val URL_APP_VERSION = "/customers/api/mobile-app-versions"
    const val URL_RESEND_EMAIL = "/customers/api/sign-up/resend/email"

    const val URL_GET_ALL_CURRENCIES = "/customers/api/currencies"
    const val URL_GET_BY_CURRENCY_CODE = "/customers/aapi/currencies/code/{currencyCode}"

    const val URL_GET_COOLING_PERIOD = "customers/api/cooling-period-duration"
    private val api: CustomersRetroService =
        RetroNetwork.createService(CustomersRetroService::class.java)

    override suspend fun signUp(signUpRequest: SignUpRequest): RetroApiResponse<SignUpResponse> {
        val response = executeSafely(call = { api.signUp(signUpRequest) })
        when (response) {
            is RetroApiResponse.Success -> {
                CookiesManager.jwtToken = response.data.data
                CookiesManager.isLoggedIn = true
            }
        }
        return response
    }

    override suspend fun sendVerificationEmail(verificationEmailRequest: SendVerificationEmailRequest): RetroApiResponse<OtpValidationResponse> =
        executeSafely(call = { api.sendVerificationEmail(verificationEmailRequest) })


    override suspend fun getAccountInfo(): RetroApiResponse<AccountInfoResponse> =
        executeSafely(call = { api.getAccountInfo() })

    override suspend fun generateOTPForDeviceVerification(demographicDataRequest: DemographicDataRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.generateOTPForDeviceVerification(demographicDataRequest) })

    override suspend fun verifyOTPForDeviceVerification(demographicDataRequest: DemographicDataRequest): RetroApiResponse<OtpValidationResponse> =
        executeSafely(call = { api.verifyOTPForDeviceVerification(demographicDataRequest) })

    override suspend fun postDemographicData(demographicDataRequest: DemographicDataRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.postDemographicData(demographicDataRequest) })

    override suspend fun validateDemographicData(deviceId: String): RetroApiResponse<ValidateDeviceResponse> =
        executeSafely(call = { api.validateDemographicData(deviceId) })

    override suspend fun getDocuments(): RetroApiResponse<GetDocumentsResponse> =
        executeSafely(call = { api.getDocuments() })

    override suspend fun uploadDocuments(document: UploadDocumentsRequest): RetroApiResponse<ApiResponse> =
        document.run {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val files = ArrayList<MultipartBody.Part>()
            filePaths.forEach {
                val file = File(it)
                val reqFile: RequestBody =
                    RequestBody.create(MediaType.parse("image/" + file.extension), file)
                val body = MultipartBody.Part.createFormData("files", file.name, reqFile)
                files.add(body)
            }

            executeSafely(call = {
                api.uploadDocuments(
                    files,
                    RequestBody.create(MediaType.parse("multipart/form-dataList"), documentType),
                    RequestBody.create(MediaType.parse("multipart/form-dataList"), firstName),
                    middleName = RequestBody.create(
                        MediaType.parse("multipart/form-dataList"),
                        middleName ?: ""
                    ),
                    lastName = RequestBody.create(
                        MediaType.parse("multipart/form-dataList"), lastName ?: ""
                    ),
                    nationality = RequestBody.create(
                        MediaType.parse("multipart/form-dataList"),
                        nationality
                    ),
                    dateExpiry = RequestBody.create(
                        MediaType.parse("multipart/form-dataList"),
                        dateFormatter.format(dateExpiry)
                    ),
                    dob = RequestBody.create(
                        MediaType.parse("multipart/form-dataList"),
                        dateFormatter.format(dob)
                    ),
                    fullName = RequestBody.create(
                        MediaType.parse("multipart/form-dataList"),
                        fullName
                    ),
                    gender = RequestBody.create(MediaType.parse("multipart/form-dataList"), gender),
                    identityNo = RequestBody.create(
                        MediaType.parse("multipart/form-dataList"),
                        identityNo
                    )
                )
            })
        }

    override suspend fun getMoreDocumentsByType(documentType: String): RetroApiResponse<GetMoreDocumentsResponse> =
        executeSafely(call = { api.getMoreDocumentsByType(documentType) })

    override suspend fun uploadProfilePicture(profilePicture: MultipartBody.Part): RetroApiResponse<UploadProfilePictureResponse> =
        executeSafely(call = { api.uploadProfilePicture(profilePicture) })

    override suspend fun validatePhoneNumber(
        countryCode: String,
        mobileNumber: String
    ): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.validatePhoneNumber(countryCode, mobileNumber) })


    override suspend fun changeMobileNumber(
        countryCode: String,
        mobileNumber: String
    ): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.changeMobileNumber(countryCode, mobileNumber) })


    override suspend fun validateEmail(email: String): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.validateEmail(email) })

    override suspend fun changeVerifiedEmail(email: String): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.changeVerifiedEmail(email) })

    override suspend fun changeUnverifiedEmail(newEmail: String): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.changeUnverifiedEmail(newEmail) })

    override suspend fun detectCardData(file: MultipartBody.Part) =
        executeSafely(call = { api.uploadIdCard(file) })

    override suspend fun getY2YBeneficiaries(contacts: List<Contact>) =
        executeSafely(call = { api.getY2YBeneficiaries(contacts) })

    override suspend fun getRecentY2YBeneficiaries() =
        executeSafely(call = { api.getRecentY2YBeneficiaries() })

    /*  send money */

    override suspend fun getRecentBeneficiaries() =
        executeSafely(call = { api.getRecentBeneficiaries() })

    override suspend fun getAllBeneficiaries() = executeSafely(call = { api.getAllBeneficiaries() })

    override suspend fun getAllCountries() = executeSafely(call = { api.getAllCountries() })

    override suspend fun addBeneficiary(beneficiary: Beneficiary): RetroApiResponse<AddBeneficiaryResponseDTO> =
        executeSafely(call = { api.addBeneficiary(beneficiary) })

    override suspend fun validateBeneficiary(beneficiary: Beneficiary): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.validateBeneficiary(beneficiary) })

    override suspend fun editBeneficiary(beneficiary: Beneficiary?): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.editBeneficiary(beneficiary) })

    override suspend fun deleteBeneficiaryFromList(beneficiaryId: String): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.deleteBeneficiaryById(beneficiaryId) })

    override suspend fun getTopUpBeneficiaries() =
        executeSafely(call = { api.getTopUpBeneficiaries() })

    override suspend fun deleteBeneficiary(cardId: String): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.deleteBeneficiary(cardId) })

    override suspend fun createBeneficiary(createBeneficiaryRequest: CreateBeneficiaryRequest): RetroApiResponse<CreateBeneficiaryResponse> =
        executeSafely(call = { api.createBeneficiary(createBeneficiaryRequest) })

    override suspend fun getCardsLimit(): RetroApiResponse<CardsLimitResponse> =
        executeSafely(call = { api.getCardsLimit() })

    override suspend fun getCurrenciesByCountryCode(country: String) =
        executeSafely(call = { api.getCurrenciesByCountryCode(country) })

    override suspend fun findOtherBank(otherBankQuery: OtherBankQuery): RetroApiResponse<RAKBankModel> =
        executeSafely(call = { api.findOtherBank(otherBankQuery) })

    override suspend fun getOtherBankParams(countryName: String): RetroApiResponse<BankParamsResponse> =
        executeSafely(call = { api.getOtherBankParams(countryName) })

    /* Household */
    override suspend fun verifyHouseholdMobile(verifyHouseholdMobileRequest: VerifyHouseholdMobileRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.verifyHouseholdMobile(verifyHouseholdMobileRequest) })

    override suspend fun verifyHouseholdParentMobile(
        mobileNumber: String?, verifyHouseholdMobileRequest: VerifyHouseholdMobileRequest
    ): RetroApiResponse<ApiResponse> =
        executeSafely(call = {
            api.verifyHouseholdParentMobile(
                mobileNumber,
                verifyHouseholdMobileRequest
            )
        })

    override suspend fun onboardHousehold(householdOnboardRequest: HouseholdOnboardRequest): RetroApiResponse<HouseholdOnBoardingResponse> =
        executeSafely(call = { api.onboardHouseholdUser(householdOnboardRequest) })

    override suspend fun addHouseholdEmail(addHouseholdEmailRequest: AddHouseholdEmailRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.addHouseholdEmail(addHouseholdEmailRequest) })

    override suspend fun createHouseholdPasscode(createPassCodeRequest: CreatePassCodeRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.createHouseholdPasscode(createPassCodeRequest) })

    override suspend fun getCountryDataWithISODigit(countryCodeWith2Digit: String): RetroApiResponse<CountryDataWithISODigit> =
        executeSafely(call = { api.getCountryDataWithISODigit(countryCodeWith2Digit) })

    override suspend fun getCountryTransactionLimits(
        countryCode: String,
        currencyCode: String
    ): RetroApiResponse<CountryLimitsResponseDTO> =
        executeSafely(call = { api.getCountryTransactionLimits(countryCode, currencyCode) })

    override suspend fun getSectionedCountries(): RetroApiResponse<SectionedCountriesResponseDTO> =
        executeSafely(call = { api.getSectionedCountries() })

    override suspend fun verifyUsername(username: String): RetroApiResponse<VerifyUsernameResponse> =
        executeSafely(call = { api.verifyUsername(username) })

    override suspend fun forgotPasscode(forgotPasscodeRequest: ForgotPasscodeRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.forgotPasscode(forgotPasscodeRequest) })

    override suspend fun validateCurrentPasscode(verifyPasscodeRequest: VerifyPasscodeRequest): RetroApiResponse<OtpValidationResponse> =
        executeSafely(call = { api.validateCurrentPasscode(verifyPasscodeRequest) })

    override suspend fun changePasscode(changePasscodeRequest: ChangePasscodeRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.changePasscode(changePasscodeRequest) })

    override suspend fun appUpdate(): RetroApiResponse<AppUpdateResponse> =
        executeSafely(call = { api.appUpdate() })

    override suspend fun saveReferalInvitation(saveReferalRequest: SaveReferalRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.saveReferalInvitation(saveReferalRequest) })

    override suspend fun getCities(): RetroApiResponse<CitiesModel> =
        executeSafely(call = { api.getCities() })

    override suspend fun getTaxReasons(): RetroApiResponse<TaxReasonResponse> =
        executeSafely(call = { api.getTaxReasons() })

    override suspend fun saveBirthInfo(birthInfoRequest: BirthInfoRequest): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.saveBirthInfo(birthInfoRequest) })

    override suspend fun saveTaxInfo(taxInfoRequest: TaxInfoRequest): RetroApiResponse<TaxInfoResponse> =
        executeSafely(call = { api.saveTaxInfo(taxInfoRequest) })

    override suspend fun getAllCurrenciesConfigs(): RetroApiResponse<CurrenciesResponse> =
        executeSafely(call = { api.getAllCurrencies() })

    override suspend fun getCurrencyByCode(currencyCode: String?): RetroApiResponse<CurrenciesByCodeResponse> =
        executeSafely(call = { api.getCurrencyByCode(currencyCode ?: "") })

    override suspend fun resendVerificationEmail(): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.resendVerificationEmail() })

    override suspend fun removeProfilePicture(): RetroApiResponse<ApiResponse> =
        executeSafely(call = { api.removeProfilePicture() })

    override suspend fun getCoolingPeriod(smCoolingPeriodRequest: SMCoolingPeriodRequest): RetroApiResponse<SMCoolingPeriodResponseDTO> =
        executeSafely(call = {
            api.getCoolingPeriod(
                smCoolingPeriodRequest.beneficiaryId,
                smCoolingPeriodRequest.productCode
            )
        })
}