package co.yap.networking.transactions

import co.yap.networking.models.ApiResponse
import co.yap.networking.transactions.requestdtos.*
import co.yap.networking.transactions.responsedtos.*
import co.yap.networking.transactions.responsedtos.achievement.AchievementsResponseDTO
import co.yap.networking.transactions.responsedtos.purposepayment.PaymentPurposeResponseDTO
import co.yap.networking.transactions.responsedtos.topuptransactionsession.Check3DEnrollmentSessionResponse
import co.yap.networking.transactions.responsedtos.topuptransactionsession.CreateTransactionSessionResponseDTO
import co.yap.networking.transactions.responsedtos.transaction.FxRateResponse
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionsResponse
import co.yap.networking.transactions.responsedtos.transaction.RemittanceFeeResponse
import retrofit2.Response
import retrofit2.http.*

interface TransactionsRetroService {

    // Add funds
    @POST(TransactionsRepository.URL_ADD_FUNDS)
    suspend fun addFunds(@Body addFundsRequest: AddFundsRequest): Response<AddRemoveFundsResponse>

    // Remove funds
    @POST(TransactionsRepository.URL_REMOVE_FUNDS)
    suspend fun removeFunds(@Body removeFundsResponse: RemoveFundsRequest): Response<AddRemoveFundsResponse>

    // Get fund transfer limits
    @GET(TransactionsRepository.URL_FUND_TRANSFER_LIMITS)
    suspend fun getFundTransferLimits(@Path("product-code") productCode: String?): Response<FundTransferLimitsResponse>

    // Get fund transfer denominations
    @GET(TransactionsRepository.URL_FUND_TRANSFER_DENOMINATIONS)
    suspend fun getFundTransferDenominations(@Path("product-code") productCode: String): Response<FundTransferDenominationsResponse>

    // Get Supplementry Card Fee
    @GET(TransactionsRepository.URL_GET_CARD_FEE)
    suspend fun getCardFee(@Path("card-type") cardType: String?): Response<RemittanceFeeResponse>

    // Get Debit Card Fee
    @GET(TransactionsRepository.URL_GET_DEBIT_CARD_FEE)
    suspend fun getDebitCardFee(): Response<RemittanceFeeResponse>

    // Get Card Statements
    @GET(TransactionsRepository.URL_GET_CARD_STATEMENTS)
    suspend fun getCardStatements(@Query("cardSerialNumber") cardSerialNumber: String?): Response<CardStatementsResponse>

    @GET(TransactionsRepository.URL_GET_ACCOUNT_STATEMENTS)
    suspend fun getAccountStatements(): Response<CardStatementsResponse>

    // Get Card Statements
    @POST(TransactionsRepository.URL_Y2Y_FUNDS_TRANSFER)
    suspend fun y2yFundsTransferRequest(@Body y2YFundsTransferRequest: Y2YFundsTransferRequest?): Response<ApiResponse>

    // AddEdit Note
    @POST(TransactionsRepository.URL_ADD_EDIT_NOTE)
    suspend fun addEditNote(@Body addEditNoteRequest: AddEditNoteRequest?): Response<AddEditNoteResponse>

    // Dashboard filter Amount
    @GET(TransactionsRepository.URL_SEARCH_FILTER_AMOUNT)
    suspend fun getSearchFilterAmount(): Response<SearchFilterAmountResponse>

    // Transaction details
    @GET(TransactionsRepository.URL_GET_TRANSACTION_DETAILS)
    suspend fun getTransactionDetails(@Path("transactionId") transactionId: String?): Response<TransactionDetailsResponse>

    // Get Account Transactions
    @GET(TransactionsRepository.URL_GET_ACCOUNT_TRANSACTIONS)
    suspend fun getAccountTransactions(
        @Path("number") number: Int?,
        @Path("size") size: Int?,
        @Query("amountStartRange") minAmount: Double?,
        @Query("amountEndRange") maxAmount: Double?,
        @Query("txnType") txnType: String?,
        @Query("title") title: String?
    ): Response<HomeTransactionsResponse>

    // Get Card Transactions
    @GET(TransactionsRepository.URL_GET_CARD_TRANSACTIONS)
    suspend fun getCardTransactions(
        @Path("number") number: Int?,
        @Path("size") size: Int?,
        @Query("cardSerialNumber") cardSerialNumber: String?,
        @Query("amountStartRange") minAmount: Double?,
        @Query("amountEndRange") maxAmount: Double?,
        @Query("txnType") txnType: String?,
        @Query("title") title: String?

    ): Response<HomeTransactionsResponse>

    // Get transaction fee
    @GET(TransactionsRepository.URL_GET_FEE)
    suspend fun getTransactionFee(@Query("productCode") type: String): Response<TransactionFeeResponseDTO>

    // Create transaction session
    @POST(TransactionsRepository.URL_CREATE_TRANSACTION_SESSION)
    suspend fun createTransactionSession(@Body createSessionRequest: CreateSessionRequest): Response<CreateTransactionSessionResponseDTO>

    // Check 3ds enrollment session
    @PUT(TransactionsRepository.URL_CHECK_3Ds_ENROLLMENT_SESSION)
    suspend fun check3DEnrollmentSession(@Body check3DEnrollmentSessionRequest: Check3DEnrollmentSessionRequest): Response<Check3DEnrollmentSessionResponse>

    // Secure id pooling
    @GET(TransactionsRepository.URL_SECURE_ID_POOLING)
    suspend fun secureIdPooling(@Path("secureId") secureId: String): Response<StringDataResponseDTO>

    // Card top up transaction request
    @PUT(TransactionsRepository.URL_TOP_UP_TRANSACTION)
    suspend fun cardTopUpTransactionRequest(
        @Path("order-id") orderId: String,
        @Body topUpTransactionRequest: TopUpTransactionRequest
    ): Response<ApiResponse>

    //Get analytics by merchant name
    @GET(TransactionsRepository.URL_GET_ANALYTICS_BY_MERCHANT_NAME)
    suspend fun getAnalyticsByMerchantName(
        @Query("cardSerialNo") cardSerialNo: String?,
        @Query("date") date: String?
    ): Response<AnalyticsResponseDTO>

    //Get analytics by category name
    @GET(TransactionsRepository.URL_GET_ANALYTICS_BY_CATEGORY_NAME)
    suspend fun getAnalyticsByCategoryName(
        @Query("cardSerialNo") cardSerialNo: String?,
        @Query("date") date: String?
    ): Response<AnalyticsResponseDTO>

    //Cash payout transfer request
    @POST(TransactionsRepository.URL_CASH_PAYOUT_TRANSFER)
    suspend fun cashPayoutTransferRequest(@Body sendMoneyTransferRequest: SendMoneyTransferRequest): Response<SendMoneyTransactionResponseDTO>

    //Get transaction fee
    @POST(TransactionsRepository.URL_GET_TRANSACTION_FEE_WITH_PRODUCT_CODE)
    suspend fun getTransactionFeeWithProductCode(
        @Path("product-code") productCode: String?,
        @Body mRemittanceFeeRequest: RemittanceFeeRequest?
    ): Response<RemittanceFeeResponse>

    //Get transaction international purpose reasons.
    @GET(TransactionsRepository.URL_GET_INTERNATIONAL_TRANSACTION_REASON_LIST)
    suspend fun getInternationalTransactionReasonList(@Path("product-code") productCode: String?): Response<InternationalFundsTransferReasonList>

    //Get transaction international purpose reasons.
    @POST(TransactionsRepository.URL_GET_INTERNATIONAL_RX_RATE_LIST)
    suspend fun getInternationalRXRateList(
        @Path("product-code") productCode: String?,
        @Body mRxListRequest: RxListRequest
    ): Response<FxRateResponse>

    //Domestic transfer request
    @POST(TransactionsRepository.URL_DOMESTIC_TRANSFER)
    suspend fun domesticTransferRequest(@Body sendMoneyTransferRequest: SendMoneyTransferRequest): Response<SendMoneyTransactionResponseDTO>

    //Uaefts transfer request
    @POST(TransactionsRepository.URL_UAEFTS_TRANSFER)
    suspend fun uaeftsTransferRequest(@Body sendMoneyTransferRequest: SendMoneyTransferRequest): Response<SendMoneyTransactionResponseDTO>

    //RMT transfer request
    @POST(TransactionsRepository.URL_RMT_TRANSFER)
    suspend fun rmtTransferRequest(@Body sendMoneyTransferRequest: SendMoneyTransferRequest): Response<SendMoneyTransactionResponseDTO>

    //Swift transfer request
    @POST(TransactionsRepository.URL_SWIFT_TRANSFER)
    suspend fun swiftTransferRequest(@Body sendMoneyTransferRequest: SendMoneyTransferRequest): Response<SendMoneyTransactionResponseDTO>

    @GET(TransactionsRepository.URL_HOUSEHOLD_CARD_FEE_PACKAGE)
    suspend fun getHousholdFeePackage(@Path("pkg-type") packageType: String): Response<RemittanceFeeResponse>

    @GET(TransactionsRepository.URL_GET_TRANSACTION_THRESHOLDS)
    suspend fun getTransactionThresholds(): Response<TransactionThresholdResponseDTO>

    @GET(TransactionsRepository.URL_GET_CUTT_OFF_TIME_CONFIGURATION)
    suspend fun getCutOffTimeConfiguration(
        @Query("productCode") productCode: String?,
        @Query("currency") currency: String?,
        @Query("amount") amount: String?,
        @Query("isCbwsi") isCbwsi: Boolean?
    ): Response<CutOffTime>

    @GET(TransactionsRepository.URL_GET_ACHIEVEMENTS)
    suspend fun getAchievements(): Response<AchievementsResponseDTO>


    @GET(TransactionsRepository.URL_GET_PURPOSE_OF_PAYMENT)
    suspend fun getPurposeOfPayment(@Path("product-code") productCode: String): Response<PaymentPurposeResponseDTO>

    @GET(TransactionsRepository.URL_CHECK_COOLING_PERIOD)
    suspend fun checkCoolingPeriodRequest(
        @Query("beneficiaryId") beneficiaryId: String?,
        @Query("beneficiaryCreationDate") beneficiaryCreationDate: String?,
        @Query("beneficiaryName") beneficiaryName: String?,
        @Query("amount") amount: String?
    ): Response<ApiResponse>

    @POST(TransactionsRepository.URL_GET_MERCHANT_TRANSACTIONS)
    suspend fun getTransactionsOfMerchant(
        @Path("merchant-type") merchantType: String,
        @Query("cardSerialNo") cardSerialNo: String?,
        @Query("date") date: String?,
        @Body merchantName : ArrayList<String>?
    ): Response<AnalyticsDetailResponseDTO>
}