package co.yap.modules.dashboard.home.status

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import co.yap.R
import co.yap.databinding.FragmentYapHomeBinding
import co.yap.modules.dashboard.home.interfaces.IYapHome
import co.yap.modules.dashboard.yapit.topup.landing.TopUpLandingActivity
import co.yap.modules.others.fragmentpresenter.activities.FragmentPresenterActivity
import co.yap.modules.setcardpin.activities.SetCardPinWelcomeActivity
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.enums.CardDeliveryStatus
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.enums.PartnerBankStatus
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.helpers.DateUtils.DEFAULT_DATE_FORMAT
import co.yap.yapcore.helpers.DateUtils.SERVER_DATE_FORMAT
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager

class DashboardNotificationStatusHelper(
    val context: Context, val binding: FragmentYapHomeBinding,
    val viewModel: IYapHome.ViewModel, val fragment: FragmentActivity? = null

) {
    private fun getStringHelper(resourceKey: String): String =
        Translator.getString(context, resourceKey)

    init {
        setUpAdapter()
    }

    private fun setUpAdapter() {
        val dashboardNotificationStatusAdapter =
            DashboardNotificationStatusAdapter(context, getStatusList())
        dashboardNotificationStatusAdapter.allowFullItemClickListener = false

        dashboardNotificationStatusAdapter.setItemListener(object : OnItemClickListener {
            override fun onItemClick(view: View, data: Any, pos: Int) {
                val statusDataModel: StatusDataModel = data as StatusDataModel
                when {
                    PaymentCardOnboardingStage.SHIPPING == statusDataModel.stage && statusDataModel.progressStatus.name != StageProgress.INACTIVE.name -> {
                        openCardDeliveryStatusScreen()
                    }
                    PaymentCardOnboardingStage.SET_PIN == statusDataModel.stage && statusDataModel.progressStatus.name != StageProgress.INACTIVE.name -> {
                        openSetCardPinScreen()
                    }
                    PaymentCardOnboardingStage.TOP_UP == statusDataModel.stage && statusDataModel.progressStatus.name != StageProgress.INACTIVE.name -> {
                        openTopUpScreen()
                    }
                }
            }
        })

        binding.lyInclude.rvNotificationStatus.adapter = dashboardNotificationStatusAdapter
    }

    private fun getStatusList(): MutableList<StatusDataModel> {
        val list = ArrayList<StatusDataModel>()
        list.add(
            StatusDataModel(
                stage = PaymentCardOnboardingStage.SHIPPING,
                statusTitle = getStringHelper(Strings.screen_time_line_display_text_status_card_on_the_way_title),
                statusDescription = getSubheading(
                    PaymentCardOnboardingStage.SHIPPING,
                    getNotificationStatus(PaymentCardOnboardingStage.SHIPPING)
                ),
                statusAction = getStringHelper(Strings.screen_time_line_display_text_status_card_on_the_way_action),
                statusDrawable = if (getNotificationStatus(PaymentCardOnboardingStage.SHIPPING) == StageProgress.COMPLETED) context.resources.getDrawable(
                    R.drawable.ic_dashboard_finish
                ) else context.resources.getDrawable(R.drawable.ic_dashboard_delivery),
                progressStatus = getNotificationStatus(PaymentCardOnboardingStage.SHIPPING)
            )
        )
        list.add(
            StatusDataModel(
                stage = PaymentCardOnboardingStage.DELIVERY,
                statusTitle = getStringHelper(Strings.screen_time_line_display_text_status_card_delivered_title),
                statusDescription = getSubheading(
                    PaymentCardOnboardingStage.DELIVERY,
                    getNotificationStatus(PaymentCardOnboardingStage.DELIVERY)
                ),
                statusAction = null,
                statusDrawable = if (getNotificationStatus(PaymentCardOnboardingStage.DELIVERY) == StageProgress.COMPLETED) context.resources.getDrawable(
                    R.drawable.ic_dashboard_finish
                ) else context.resources.getDrawable(R.drawable.ic_card_small),
                progressStatus = getNotificationStatus(PaymentCardOnboardingStage.DELIVERY)
            )
        )

        list.add(
            StatusDataModel(
                stage = PaymentCardOnboardingStage.SET_PIN,
                statusTitle = getStringHelper(Strings.screen_time_line_display_text_status_set_card_pin_title),
                statusDescription = getSubheading(
                    PaymentCardOnboardingStage.SET_PIN,
                    getNotificationStatus(PaymentCardOnboardingStage.SET_PIN)
                ),
                statusAction = getStringHelper(Strings.screen_time_line_display_text_status_set_card_pin_action),
                statusDrawable = if (getNotificationStatus(PaymentCardOnboardingStage.SET_PIN) == StageProgress.COMPLETED) context.resources.getDrawable(
                    R.drawable.ic_dashboard_finish
                ) else context.resources.getDrawable(R.drawable.ic_dashboard_set_pin),
                progressStatus = getNotificationStatus(PaymentCardOnboardingStage.SET_PIN)
            )
        )
        list.add(
            StatusDataModel(
                stage = PaymentCardOnboardingStage.TOP_UP,
                statusTitle = getStringHelper(Strings.screen_time_line_display_text_status_card_top_up_title),
                statusDescription = getSubheading(
                    PaymentCardOnboardingStage.TOP_UP,
                    getNotificationStatus(PaymentCardOnboardingStage.TOP_UP)
                ),
                statusAction = getStringHelper(Strings.screen_time_line_display_text_status_card_top_up_action),
                statusDrawable = if (getNotificationStatus(PaymentCardOnboardingStage.TOP_UP) == StageProgress.COMPLETED) context.resources.getDrawable(
                    R.drawable.ic_dashboard_finish
                ) else context.resources.getDrawable(R.drawable.ic_dashboard_topup),
                progressStatus = getNotificationStatus(PaymentCardOnboardingStage.TOP_UP),
                hideLine = true
            )
        )
        return list
    }

    private fun getNotificationStatus(stage: PaymentCardOnboardingStage): StageProgress {
        return SessionManager.card.value?.let { card ->
            when (stage) {
                PaymentCardOnboardingStage.SHIPPING -> {
                    return (when {
                        SessionManager.user?.partnerBankStatus == PartnerBankStatus.SIGN_UP_PENDING.status -> {
                            StageProgress.INACTIVE
                        }
                        card.deliveryStatus == CardDeliveryStatus.ORDERED.name || card.deliveryStatus == CardDeliveryStatus.BOOKED.name || card.deliveryStatus == CardDeliveryStatus.SHIPPING.name -> {
                            StageProgress.ACTIVE
                        }
                        card.deliveryStatus == CardDeliveryStatus.SHIPPED.name -> {
                            StageProgress.COMPLETED
                        }
                        else -> StageProgress.INACTIVE
                    })
                }
                PaymentCardOnboardingStage.DELIVERY -> {
                    return (when {
                        card.deliveryStatus == CardDeliveryStatus.SHIPPED.name
                                && SessionManager.user?.partnerBankStatus != PartnerBankStatus.ACTIVATED.status -> {
                            StageProgress.ACTIVE
                        }
                        card.deliveryStatus == CardDeliveryStatus.SHIPPED.name
                                && SessionManager.user?.partnerBankStatus == PartnerBankStatus.INITIAL_VERIFICATION_SUCCESSFUL.status
                                || SessionManager.user?.partnerBankStatus == PartnerBankStatus.ACTIVATED.status -> {
                            StageProgress.COMPLETED
                        }
                        else -> StageProgress.INACTIVE
                    })
                }
                PaymentCardOnboardingStage.SET_PIN -> {
                    return (when {
                        card.deliveryStatus == CardDeliveryStatus.SHIPPED.name && !card.pinCreated && SessionManager.user?.partnerBankStatus == PartnerBankStatus.ACTIVATED.status -> {
                            StageProgress.ACTIVE
                        }
                        card.deliveryStatus == CardDeliveryStatus.SHIPPED.name && card.pinCreated && SessionManager.user?.partnerBankStatus == PartnerBankStatus.ACTIVATED.status -> {
                            StageProgress.COMPLETED
                        }
                        else -> StageProgress.INACTIVE
                    })
                }

                PaymentCardOnboardingStage.TOP_UP -> {
                    return (when {
                        card.deliveryStatus == CardDeliveryStatus.SHIPPED.name && card.pinCreated && SessionManager.user?.partnerBankStatus == PartnerBankStatus.ACTIVATED.status -> {
                            StageProgress.ACTIVE
                        }
                        else -> StageProgress.INACTIVE
                    })
                }
            }
        } ?: return StageProgress.INACTIVE
    }

    private fun getSubheading(stage: PaymentCardOnboardingStage, progress: StageProgress): String {
        return (when (stage) {
            PaymentCardOnboardingStage.SHIPPING -> return (when (progress) {
                StageProgress.ACTIVE, StageProgress.INACTIVE -> getStringHelper(Strings.screen_time_line_display_text_status_card_on_the_way_description)
                StageProgress.COMPLETED -> "Your card was delivered on ${
                DateUtils.reformatStringDate(
                    SessionManager.card.value?.shipmentDate ?: "",
                    SERVER_DATE_FORMAT,
                    DEFAULT_DATE_FORMAT
                )
                }"
                else -> getStringHelper(Strings.screen_time_line_display_text_status_card_on_the_way_description)
            })
            PaymentCardOnboardingStage.DELIVERY -> return (when (progress) {
                StageProgress.INACTIVE -> "EID scan will be carried out by the agent"
                StageProgress.ACTIVE -> getStringHelper(Strings.screen_time_line_display_text_status_card_delivered_description)
                StageProgress.COMPLETED -> "Your EID scan was approved on 03/09/2020"
                else -> "EID scan will be carried out by the agent"
            })
            PaymentCardOnboardingStage.SET_PIN -> return (when (progress) {
                StageProgress.ACTIVE, StageProgress.INACTIVE -> getStringHelper(Strings.screen_time_line_display_text_status_set_card_pin_description)
                StageProgress.COMPLETED -> "Your PIN was successfully set on ${
                DateUtils.reformatStringDate(
                    SessionManager.card.value?.activationDate ?: "",
                    SERVER_DATE_FORMAT,
                    DEFAULT_DATE_FORMAT
                )
                }"
                else -> getStringHelper(Strings.screen_time_line_display_text_status_set_card_pin_description)
            })
            PaymentCardOnboardingStage.TOP_UP -> getStringHelper(Strings.screen_time_line_display_text_status_card_top_up_description)

        })
    }

    private fun openTopUpScreen() {
        context.launchActivity<TopUpLandingActivity>(type = FeatureSet.TOP_UP)
    }

    private fun openCardDeliveryStatusScreen() {
        fragment?.startActivityForResult(
            FragmentPresenterActivity.getIntent(
                context,
                Constants.MODE_STATUS_SCREEN,
                SessionManager.card.value
            ), Constants.EVENT_CREATE_CARD_PIN
        )
    }

    private fun openSetCardPinScreen() {
        fragment?.startActivityForResult(
            SessionManager.getPrimaryCard()?.let {
                SetCardPinWelcomeActivity.newIntent(
                    context,
                    it
                )
            }, RequestCodes.REQUEST_FOR_SET_PIN
        )
    }
}
