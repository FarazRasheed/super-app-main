package co.yap.networking.customers

import co.yap.networking.customers.requestdtos.*
import co.yap.networking.customers.responsedtos.*
import co.yap.networking.customers.responsedtos.beneficiary.RecentBeneficiariesResponse
import co.yap.networking.customers.responsedtos.beneficiary.TopUpBeneficiariesResponse
import co.yap.networking.customers.responsedtos.currency.CurrenciesByCodeResponse
import co.yap.networking.customers.responsedtos.currency.CurrenciesResponse
import co.yap.networking.customers.responsedtos.sendmoney.*
import co.yap.networking.customers.responsedtos.tax.TaxInfoResponse
import co.yap.networking.messages.responsedtos.OtpValidationResponse
import co.yap.networking.models.ApiResponse
import co.yap.networking.models.RetroApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body

interface CustomersApi {
    suspend fun signUp(signUpRequest: SignUpRequest): RetroApiResponse<SignUpResponse>
    suspend fun sendVerificationEmail(verificationEmailRequest: SendVerificationEmailRequest): RetroApiResponse<OtpValidationResponse>
    suspend fun getAccountInfo(): RetroApiResponse<AccountInfoResponse>
    suspend fun postDemographicData(demographicDataRequest: DemographicDataRequest): RetroApiResponse<ApiResponse>
    suspend fun generateOTPForDeviceVerification(demographicDataRequest: DemographicDataRequest): RetroApiResponse<ApiResponse>
    suspend fun verifyOTPForDeviceVerification(demographicDataRequest: DemographicDataRequest): RetroApiResponse<OtpValidationResponse>
    suspend fun validateDemographicData(deviceId: String): RetroApiResponse<ValidateDeviceResponse>
    suspend fun uploadDocuments(document: UploadDocumentsRequest): RetroApiResponse<ApiResponse>
    suspend fun getDocuments(): RetroApiResponse<ApiResponse>
    suspend fun validateEmail(email: String): RetroApiResponse<ApiResponse>
    suspend fun getMoreDocumentsByType(documentType: String): RetroApiResponse<ApiResponse>
    suspend fun uploadProfilePicture(profilePicture: MultipartBody.Part): RetroApiResponse<UploadProfilePictureResponse>
    suspend fun validatePhoneNumber(
        countryCode: String,
        mobileNumber: String
    ): RetroApiResponse<ApiResponse>

    suspend fun changeMobileNumber(
        countryCode: String,
        mobileNumber: String
    ): RetroApiResponse<ApiResponse>

    suspend fun changeVerifiedEmail(email: String): RetroApiResponse<ApiResponse>
    suspend fun changeUnverifiedEmail(newEmail: String): RetroApiResponse<ApiResponse>
    suspend fun detectCardData(file: MultipartBody.Part): RetroApiResponse<ApiResponse>
    suspend fun getY2YBeneficiaries(contacts: List<Contact>): RetroApiResponse<Y2YBeneficiariesResponse>
    suspend fun getRecentY2YBeneficiaries(): RetroApiResponse<RecentBeneficiariesResponse>
    suspend fun getTopUpBeneficiaries(): RetroApiResponse<TopUpBeneficiariesResponse>
    suspend fun deleteBeneficiary(cardId: String): RetroApiResponse<ApiResponse>
    suspend fun createBeneficiary(createBeneficiaryRequest: CreateBeneficiaryRequest): RetroApiResponse<CreateBeneficiaryResponse>
    suspend fun getCardsLimit(): RetroApiResponse<CardsLimitResponse>
    suspend fun removeProfilePicture(): RetroApiResponse<ApiResponse>


/*  send money */

    suspend fun getRecentBeneficiaries(): RetroApiResponse<GetAllBeneficiaryResponse>
    suspend fun getAllBeneficiaries(): RetroApiResponse<GetAllBeneficiaryResponse>
    suspend fun getAllCountries(): RetroApiResponse<CountryModel>
    suspend fun addBeneficiary(beneficiary: Beneficiary): RetroApiResponse<AddBeneficiaryResponseDTO>
    suspend fun validateBeneficiary(beneficiary: Beneficiary): RetroApiResponse<ApiResponse>
    suspend fun editBeneficiary(beneficiary: Beneficiary?): RetroApiResponse<ApiResponse>
    suspend fun deleteBeneficiaryFromList(beneficiaryId: String): RetroApiResponse<ApiResponse>

    suspend fun getCurrenciesByCountryCode(country: String): RetroApiResponse<ApiResponse>
    suspend fun findOtherBank(otherBankQuery: OtherBankQuery): RetroApiResponse<ApiResponse>
    suspend fun getOtherBankParams(countryName: String): RetroApiResponse<ApiResponse>

    suspend fun getSectionedCountries(): RetroApiResponse<SectionedCountriesResponseDTO>
    /* Household */

    suspend fun verifyHouseholdMobile(verifyHouseholdMobileRequest: VerifyHouseholdMobileRequest): RetroApiResponse<ApiResponse>
    suspend fun verifyHouseholdParentMobile(
        mobileNumber: String?, verifyHouseholdMobileRequest: VerifyHouseholdMobileRequest
    ): RetroApiResponse<ApiResponse>

    suspend fun onboardHousehold(householdOnboardRequest: HouseholdOnboardRequest): RetroApiResponse<HouseholdOnBoardingResponse>
    suspend fun addHouseholdEmail(addHouseholdEmailRequest: AddHouseholdEmailRequest): RetroApiResponse<ApiResponse>
    suspend fun createHouseholdPasscode(createPassCodeRequest: CreatePassCodeRequest): RetroApiResponse<ApiResponse>
    suspend fun getCountryDataWithISODigit(countryCodeWith2Digit: String): RetroApiResponse<CountryDataWithISODigit>
    suspend fun getCountryTransactionLimits(
        countryCode: String,
        currencyCode: String
    ): RetroApiResponse<CountryLimitsResponseDTO>

    suspend fun saveReferalInvitation(@Body saveReferalRequest: SaveReferalRequest): RetroApiResponse<ApiResponse>
    /*
    * fun that comes from admin repo to be replaced
    * */
    suspend fun verifyUsername(username: String): RetroApiResponse<VerifyUsernameResponse>

    suspend fun forgotPasscode(forgotPasscodeRequest: ForgotPasscodeRequest): RetroApiResponse<ApiResponse>
    suspend fun validateCurrentPasscode(verifyPasscodeRequest: VerifyPasscodeRequest): RetroApiResponse<OtpValidationResponse>
    suspend fun changePasscode(changePasscodeRequest: ChangePasscodeRequest): RetroApiResponse<ApiResponse>
    suspend fun appUpdate(): RetroApiResponse<AppUpdateResponse>
    suspend fun getCities(): RetroApiResponse<CitiesModel>
    suspend fun getTaxReasons(): RetroApiResponse<TaxReasonResponse>
    suspend fun saveBirthInfo(birthInfoRequest: BirthInfoRequest): RetroApiResponse<ApiResponse>
    suspend fun saveTaxInfo(taxInfoRequest: TaxInfoRequest): RetroApiResponse<TaxInfoResponse>
    suspend fun resendVerificationEmail(): RetroApiResponse<ApiResponse>
    suspend fun getAllCurrenciesConfigs(): RetroApiResponse<CurrenciesResponse>
    suspend fun getCurrencyByCode(currencyCode: String?): RetroApiResponse<CurrenciesByCodeResponse>
    suspend fun getCoolingPeriod(smCoolingPeriodRequest: SMCoolingPeriodRequest): RetroApiResponse<SMCoolingPeriodResponseDTO>


}
