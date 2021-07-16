package co.yap.networking.cards

import co.yap.networking.cards.requestdtos.*
import co.yap.networking.cards.responsedtos.*
import co.yap.networking.customers.responsedtos.HouseHoldCardsDesignResponse
import co.yap.networking.models.ApiResponse
import co.yap.networking.models.RetroApiResponse

interface CardsApi {
    suspend fun createCardPin(
        createCardPinRequest: CreateCardPinRequest,
        cardSerialNumber: String
    ): RetroApiResponse<ApiResponse>

    suspend fun getDebitCards(cardType: String): RetroApiResponse<GetCardsResponse>

    suspend fun orderCard(
        address: Address
    ): RetroApiResponse<ApiResponse>

    suspend fun getAccountBalanceRequest(): RetroApiResponse<CardBalanceResponseDTO>
    suspend fun configAllowAtm(cardLimitConfigRequest: CardLimitConfigRequest): RetroApiResponse<ApiResponse>
    suspend fun configAbroadPayment(cardLimitConfigRequest: CardLimitConfigRequest): RetroApiResponse<ApiResponse>
    suspend fun configRetailPayment(cardLimitConfigRequest: CardLimitConfigRequest): RetroApiResponse<ApiResponse>
    suspend fun configOnlineBanking(cardLimitConfigRequest: CardLimitConfigRequest): RetroApiResponse<ApiResponse>
    suspend fun addSpareVirtualCard(
        addVirtualSpareCardRequest: AddVirtualSpareCardRequest
    ): RetroApiResponse<AddSpareVirualCardResponse>

    suspend fun addSparePhysicalCard(
        address: Address
    ): RetroApiResponse<ApiResponse>

    suspend fun getUserAddressRequest(): RetroApiResponse<ApiResponse>
    suspend fun getCardBalance(cardSerialNumber: String): RetroApiResponse<CardBalanceResponseDTO>
    suspend fun freezeUnfreezeCard(cardLimitConfigRequest: CardLimitConfigRequest): RetroApiResponse<ApiResponse>
    suspend fun getCardDetails(cardSerialNumber: String): RetroApiResponse<CardDetailResponseDTO>
    suspend fun removeCard(cardLimitConfigRequest: CardLimitConfigRequest): RetroApiResponse<ApiResponse>
    suspend fun updateCardName(
        cardName: String,
        cardSerialNumber: String
    ): RetroApiResponse<CardDetailResponseDTO>

    suspend fun reportAndBlockCard(cardsHotlistReequest: CardsHotlistRequest): RetroApiResponse<ApiResponse>
    suspend fun changeCardPinRequest(changeCardCardPinRequest: ChangeCardPinRequest): RetroApiResponse<ApiResponse>
    suspend fun editAddressRequest(address: Address): RetroApiResponse<ApiResponse>
    suspend fun forgotCardPin(
        cardSerialNumber: String,
        forgotCardPin: ForgotCardPin
    ): RetroApiResponse<ApiResponse>

    suspend fun reorderDebitCard(address: Address): RetroApiResponse<ApiResponse>
    suspend fun reorderSupplementryCard(address: Address): RetroApiResponse<ApiResponse>
    suspend fun getCardsAtmCdm(): RetroApiResponse<AtmCdmResponse>

    suspend fun getHouseHoldCardsDesign(
        accountType: String
    ): RetroApiResponse<HouseHoldCardsDesignResponse>
}