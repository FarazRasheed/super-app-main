package co.yap.networking.transactions

import co.yap.networking.models.ApiResponse
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.requestdtos.*
import co.yap.networking.transactions.responsedtos.*
import co.yap.networking.transactions.responsedtos.purposepayment.PaymentPurposeResponseDTO
import co.yap.networking.transactions.responsedtos.topuptransactionsession.Check3DEnrollmentSessionResponse
import co.yap.networking.transactions.responsedtos.topuptransactionsession.CreateTransactionSessionResponseDTO
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionsResponse
import co.yap.networking.transactions.responsedtos.transaction.RemittanceFeeResponse

interface TransactionsApi {
    suspend fun addFunds(addFundsRequest: AddFundsRequest): RetroApiResponse<AddRemoveFundsResponse>
    suspend fun removeFunds(removeFundsResponse: RemoveFundsRequest): RetroApiResponse<AddRemoveFundsResponse>
    suspend fun getFundTransferLimits(productCode: String?): RetroApiResponse<FundTransferLimitsResponse>
    suspend fun getFundTransferDenominations(productCode: String): RetroApiResponse<FundTransferDenominationsResponse>
    suspend fun getCardFee(cardType: String): RetroApiResponse<RemittanceFeeResponse>
    suspend fun getDebitCardFee(): RetroApiResponse<RemittanceFeeResponse>
    suspend fun getTransactionFeeWithProductCode(
        productCode: String?,
        mRemittanceFeeRequest: RemittanceFeeRequest?
    ): RetroApiResponse<ApiResponse>

    suspend fun getTransactionInternationalReasonList(productCode: String?): RetroApiResponse<InternationalFundsTransferReasonList>
    suspend fun getCardStatements(cardSerialNumber: String?): RetroApiResponse<CardStatementsResponse>
    suspend fun getAccountStatements(): RetroApiResponse<CardStatementsResponse>
    suspend fun getTransactionInternationalRXList(
        productCode: String?,
        mRxListRequest: RxListRequest
    ): RetroApiResponse<ApiResponse>

    suspend fun y2yFundsTransferRequest(y2YFundsTransferRequest: Y2YFundsTransferRequest): RetroApiResponse<ApiResponse>
    suspend fun addEditNote(addEditNoteRequest: AddEditNoteRequest): RetroApiResponse<AddEditNoteResponse>
    suspend fun getSearchFilterAmount(): RetroApiResponse<SearchFilterAmountResponse>
    suspend fun getTransactionDetails(transactionId: String?): RetroApiResponse<TransactionDetailsResponse>
    suspend fun getAccountTransactions(homeTransactionsRequest: HomeTransactionsRequest): RetroApiResponse<HomeTransactionsResponse>
    suspend fun getCardTransactions(cardTransactionRequest: CardTransactionRequest): RetroApiResponse<HomeTransactionsResponse>
    suspend fun getTransactionFee(productCode: String): RetroApiResponse<TransactionFeeResponseDTO>
    suspend fun createTransactionSession(createSessionRequest: CreateSessionRequest): RetroApiResponse<CreateTransactionSessionResponseDTO>
    suspend fun check3DEnrollmentSession(check3DEnrollmentSessionRequest: Check3DEnrollmentSessionRequest): RetroApiResponse<Check3DEnrollmentSessionResponse>
    suspend fun secureIdPooling(secureId: String = ""): RetroApiResponse<StringDataResponseDTO>
    suspend fun cardTopUpTransactionRequest(
        orderId: String,
        topUpTransactionRequest: TopUpTransactionRequest
    ): RetroApiResponse<ApiResponse>

    suspend fun getAnalyticsByMerchantName(
        cardSerialNo: String? = "",
        date: String? = ""
    ): RetroApiResponse<AnalyticsResponseDTO>

    suspend fun getAnalyticsByCategoryName(
        cardSerialNo: String? = "",
        date: String? = ""
    ): RetroApiResponse<AnalyticsResponseDTO>

    suspend fun cashPayoutTransferRequest(sendMoneyTransferRequest: SendMoneyTransferRequest): RetroApiResponse<SendMoneyTransactionResponseDTO>
    suspend fun domesticTransferRequest(sendMoneyTransferRequest: SendMoneyTransferRequest): RetroApiResponse<SendMoneyTransactionResponseDTO>
    suspend fun uaeftsTransferRequest(sendMoneyTransferRequest: SendMoneyTransferRequest): RetroApiResponse<SendMoneyTransactionResponseDTO>
    suspend fun rmtTransferRequest(sendMoneyTransferRequest: SendMoneyTransferRequest): RetroApiResponse<SendMoneyTransactionResponseDTO>
    suspend fun swiftTransferRequest(sendMoneyTransferRequest: SendMoneyTransferRequest): RetroApiResponse<SendMoneyTransactionResponseDTO>
    suspend fun getHousholdFeePackage(packageType: String): RetroApiResponse<RemittanceFeeResponse>
    suspend fun getTransactionThresholds(): RetroApiResponse<TransactionThresholdResponseDTO>
    suspend fun getCutOffTimeConfiguration(
        productCode: String?,
        currency: String?,
        amount: String?,
        isCbwsi: Boolean? = null
    ): RetroApiResponse<CutOffTime>

    suspend fun getAchievements(): RetroApiResponse<ApiResponse>
    suspend fun getPurposeOfPayment(productCode: String): RetroApiResponse<PaymentPurposeResponseDTO>
    suspend fun checkCoolingPeriodRequest(
        beneficiaryId: String?,
        beneficiaryCreationDate: String?,
        beneficiaryName: String?,
        amount: String?
    ): RetroApiResponse<ApiResponse>
    suspend fun getTransactionsOfMerchant(
        merchantType: String,
        cardSerialNo: String?,
        date: String?, merchantName: ArrayList<String>?
    ): RetroApiResponse<AnalyticsDetailResponseDTO>
}