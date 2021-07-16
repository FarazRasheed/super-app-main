package co.yap.networking.cards

import co.yap.networking.cards.requestdtos.*
import co.yap.networking.cards.responsedtos.*
import co.yap.networking.customers.responsedtos.HouseHoldCardsDesignResponse
import co.yap.networking.models.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface CardsRetroService {

    // Create Card Pin
    @POST(CardsRepository.URL_CREATE_PIN)
    suspend fun createCardPin(@Path("card-serial-number") cardSerialNumber: String, @Body createCardPinRequest: CreateCardPinRequest): Response<ApiResponse>

    // Get Cards
    @GET(CardsRepository.URL_GET_CARDS)
    suspend fun getDebitCards(@Query("cardType") cardType: String): Response<GetCardsResponse>

    // Order Card
    @POST(CardsRepository.URL_ORDER_CARD)
    suspend fun orderCard(@Body address: Address): Response<ApiResponse>

    // get card balance
    @GET(CardsRepository.URL_GET_DEBIT_CARD_BALANCE)
    suspend fun getAccountBalanceRequest(): Response<CardBalanceResponseDTO>

    @PUT(CardsRepository.URL_ALLOW_ATM)
    suspend fun configAllowAtm(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<ApiResponse>

    @PUT(CardsRepository.URL_ABROAD_PAYMENT)
    suspend fun configAbroadPayment(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<ApiResponse>

    @PUT(CardsRepository.URL_ONLINE_BANKING)
    suspend fun configOnlineBanking(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<ApiResponse>

    @PUT(CardsRepository.URL_RETAIL_PAYMENT)
    suspend fun configRetailPayment(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<ApiResponse>

    // add spare virtual card
    @POST(CardsRepository.URL_ADD_SPARE_VIRTUAL_CARD)
    suspend fun addSpareVirtualCardRequest(@Body addVirtualSpareCardRequest: AddVirtualSpareCardRequest): Response<AddSpareVirualCardResponse>

    // add spare physical card
    @POST(CardsRepository.URL_ADD_SPARE_PHYSICAL_CARD)
    suspend fun addSparePhysicalCardRequest(@Body address: Address): Response<ApiResponse>

    // add spare physical card
    @GET(CardsRepository.URL_GET_PHYSICAL_CARD_ADDRESS)
    suspend fun getPhysicalCardAddress(): Response<GetPhysicalAddress>

    // Get Card Balance
    @GET(CardsRepository.URL_GET_CARD_BALANCE)
    suspend fun getCardBalance(@Query("cardSerialNumber") cardSerialNumber: String): Response<CardBalanceResponseDTO>

    // Freeze / Unfreeze Card
    @PUT(CardsRepository.URL_CARD_FREEZE_UNFREEZE)
    suspend fun freezeUnfreezeCard(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<ApiResponse>

    // Get Card Details
    @GET(CardsRepository.URL_GET_CARD_DETAILS)
    suspend fun getCardDetails(@Query("cardSerialNumber") cardSerialNumber: String): Response<CardDetailResponseDTO>

    // Remove Card
    @PUT(CardsRepository.URL_REMOVE_CARD)
    suspend fun removeCard(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<ApiResponse>

    // Update Card name
    @PUT(CardsRepository.URL_UPDATE_CARD_NAME)
    suspend fun updateCardName(@Query("cardName") cardName: String, @Query("cardSerialNumber") cardSerialNumber: String): Response<CardDetailResponseDTO>

    // report & block Card
    @POST(CardsRepository.URL_REPORT_LOST_OR_STOLEN_CARD)
    suspend fun reportAndBlockCard(@Body cardsHotlistReequest: CardsHotlistRequest): Response<ApiResponse>

    @POST(CardsRepository.URL_CHANGE_CARD_PIN)
    suspend fun changeCardPinRequest(@Body changeCardPinRequest: ChangeCardPinRequest): Response<ApiResponse>

    // edit address
    @PUT(CardsRepository.URL_GET_PHYSICAL_CARD_ADDRESS)
    suspend fun editAddressRequest(@Body address: Address): Response<ApiResponse>

    // forgot card pin
    @POST(CardsRepository.URL_FORGOT_CARD_PIN)
    suspend fun forgotCardPin(@Path("card-serial-number") cardSerialNumber: String, @Body forgotCardPin: ForgotCardPin): Response<ApiResponse>

    // House hold cards design
    @GET(CardsRepository.URL_GET_HOUSE_HOLD_CARDS_DESIGN)
    suspend fun getHouseHoldCardsDesign(@Query("account_type") accountType: String): Response<HouseHoldCardsDesignResponse>

    // reorder debit card
    @POST(CardsRepository.URL_REORDER_DEBIT_CARD)
    suspend fun reorderDebitCard(@Body address: Address): Response<ApiResponse>

    // reorder supplementary card
    @POST(CardsRepository.URL_REORDER_SUPPLEMENTARY_CARD)
    suspend fun reorderSupplementaryCard(@Body address: Address): Response<ApiResponse>

    // reorder supplementary card
    @GET(CardsRepository.URL_ATM_CDM)
    suspend fun getCardsAtmCdm(): Response<AtmCdmResponse>

}