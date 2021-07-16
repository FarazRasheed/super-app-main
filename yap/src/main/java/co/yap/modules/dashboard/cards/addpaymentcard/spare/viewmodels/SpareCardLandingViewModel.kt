package co.yap.modules.dashboard.cards.addpaymentcard.spare.viewmodels

import android.app.Application
import android.content.Context
import co.yap.modules.dashboard.cards.addpaymentcard.interfaces.ISpareCards
import co.yap.modules.dashboard.cards.addpaymentcard.models.BenefitsModel
import co.yap.modules.dashboard.cards.addpaymentcard.spare.states.SpareCardLandingState
import co.yap.modules.dashboard.cards.addpaymentcard.viewmodels.AddPaymentChildViewModel
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.responsedtos.transaction.RemittanceFeeResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets


class SpareCardLandingViewModel(application: Application) :
    AddPaymentChildViewModel<ISpareCards.State>(application), ISpareCards.ViewModel {

    private val transactionRepository: TransactionsRepository = TransactionsRepository
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val errorEvent: SingleClickEvent = SingleClickEvent()
    override val state: SpareCardLandingState =
        SpareCardLandingState()

    override fun handlePressOnAddVirtualCard(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnAddPhysicalCard(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        state.virtualCardFee =
            parentViewModel?.virtualCardFee ?: "0.0".toFormattedCurrency()
        state.physicalCardFee =
            parentViewModel?.physicalCardFee ?: "0.0".toFormattedCurrency()
    }

    override fun getVirtualCardFee() {
        launch {
            state.loading = true
            when (val response = transactionRepository.getCardFee("virtual")) {
                is RetroApiResponse.Success -> {
                    if (response.data.data != null) {
                        feeType = response.data.data?.feeType ?: ""
                        response.data.data?.tierRateDTOList?.let {
                            feeTiers =
                                response.data.data?.tierRateDTOList as ArrayList<RemittanceFeeResponse.RemittanceFee.TierRateDTO>
                            isFeeReceived.value = true
                        }
                    }
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    errorEvent.call()
                }
            }
            state.loading = false
        }
    }

    override fun getPhysicalCardFee() {
        launch {
            when (val response = transactionRepository.getCardFee("physical")) {
                is RetroApiResponse.Success -> {
                    if (response.data.data != null) {
                        if (response.data.data?.feeType == Constants.FEE_TYPE_FLAT) {
                            val feeAmount = response.data.data?.tierRateDTOList?.get(0)?.feeAmount
                            val VATAmount = response.data.data?.tierRateDTOList?.get(0)?.vatAmount
                            state.virtualCardFee =
                                feeAmount?.plus(VATAmount ?: 0.0).toString()
                                    .toFormattedCurrency()
                        }
                    } else {
                        state.physicalCardFee = "0.0".toFormattedCurrency()
                    }

                    parentViewModel?.physicalCardFee = state.physicalCardFee
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_spare_card_landing_display_text_title))
    }

    override fun loadJSONDummyList(): ArrayList<BenefitsModel> {
        val benefitsModelList: ArrayList<BenefitsModel> = ArrayList<BenefitsModel>()

        val mainObj = JSONObject(loadTransactionFromJsonAssets(context))
        val mainDataList = mainObj.getJSONArray("dataList")
        if (mainDataList != null) {

            for (i in 0 until mainDataList.length()) {
                //
                val parentArrayList = mainDataList.getJSONObject(i)
                val benfitTitle: String = parentArrayList.getString("benfitTitle")
                val benfitDetail: String = parentArrayList.getString("benfitDetail")


                val benefitsModel: BenefitsModel = BenefitsModel(
                    benfitTitle,
                    benfitDetail
                )
                benefitsModelList.add(benefitsModel)
            }
        }


        return benefitsModelList
    }

    private fun loadTransactionFromJsonAssets(context: Context): String? {
        val json: String?
        try {
            val `is` = context.assets.open("benefits.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}