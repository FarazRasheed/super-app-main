package co.yap.modules.dashboard.cards.paymentcarddetail.activities

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import co.yap.BR
import co.yap.R
import co.yap.databinding.ActivityPaymentCardDetailBinding
import co.yap.modules.dashboard.cards.paymentcarddetail.activities.carddetaildialog.CardDetailsDialogPagerAdapter
import co.yap.modules.dashboard.cards.paymentcarddetail.activities.carddetaildialog.CardDetailsModel
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.activities.AddFundsActivity
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.activities.RemoveFundsActivity
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.activities.ForgotCardPinActivity
import co.yap.modules.dashboard.cards.paymentcarddetail.fragments.CardClickListener
import co.yap.modules.dashboard.cards.paymentcarddetail.fragments.PrimaryCardBottomSheet
import co.yap.modules.dashboard.cards.paymentcarddetail.fragments.SpareCardBottomSheet
import co.yap.modules.dashboard.cards.paymentcarddetail.interfaces.IPaymentCardDetail
import co.yap.modules.dashboard.cards.paymentcarddetail.limits.activities.CardLimitsActivity
import co.yap.modules.dashboard.cards.paymentcarddetail.statments.activities.CardStatementsActivity
import co.yap.modules.dashboard.cards.paymentcarddetail.viewmodels.PaymentCardDetailViewModel
import co.yap.modules.dashboard.cards.reordercard.activities.ReorderCardActivity
import co.yap.modules.dashboard.cards.reportcard.activities.ReportLostOrStolenCardActivity
import co.yap.modules.dashboard.home.adaptor.TransactionsHeaderAdapter
import co.yap.modules.dashboard.home.filters.activities.TransactionFiltersActivity
import co.yap.modules.dashboard.home.filters.models.TransactionFilters
import co.yap.modules.dashboard.transaction.activities.TransactionDetailsActivity
import co.yap.modules.dummy.ActivityNavigator
import co.yap.modules.dummy.NavigatorProvider
import co.yap.modules.others.helper.Constants
import co.yap.networking.cards.responsedtos.Card
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionListData
import co.yap.networking.transactions.responsedtos.transaction.Transaction
import co.yap.translation.Strings
import co.yap.yapcore.AdjustEvents.Companion.trackAdjustPlatformEvent
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.adjust.AdjustEvents
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.CardStatus
import co.yap.yapcore.helpers.*
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.helpers.ExtraKeys
import co.yap.yapcore.helpers.cancelAllSnackBar
import co.yap.yapcore.helpers.confirm
import co.yap.yapcore.helpers.extentions.*
import co.yap.yapcore.helpers.showSnackBar
import co.yap.yapcore.helpers.spannables.underline
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.FeatureProvisioning
import co.yap.yapcore.managers.SessionManager
import com.google.android.material.snackbar.Snackbar
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.activity_payment_card_detail.*
import kotlinx.android.synthetic.main.layout_card_info.*

class PaymentCardDetailActivity : BaseBindingActivity<IPaymentCardDetail.ViewModel>(),
    IPaymentCardDetail.View, CardClickListener {

    private lateinit var primaryCardBottomSheet: PrimaryCardBottomSheet
    private lateinit var spareCardBottomSheet: SpareCardBottomSheet

    private var cardFreezeUnfreeze: Boolean = false
    private var cardRemoved: Boolean = false
    private var limitsUpdated: Boolean = false
    private var nameUpdated: Boolean = false
    private lateinit var mNavigator: ActivityNavigator

    companion object {
        private const val CARD = "card"
        fun newIntent(context: Context, card: Card): Intent {
            val intent = Intent(context, PaymentCardDetailActivity::class.java)
            intent.putExtra(CARD, card)
            return intent
        }
    }

    override val viewModel: IPaymentCardDetail.ViewModel
        get() = ViewModelProviders.of(this).get(PaymentCardDetailViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_payment_card_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerTransactionBroadcast()
        mNavigator = (this.applicationContext as NavigatorProvider).provideNavigator()
        setUpTransactionsListRecyclerView()
        setObservers()
        setupView()
    }

    private fun getBindings(): ActivityPaymentCardDetailBinding {
        return viewDataBinding as ActivityPaymentCardDetailBinding
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickObserver)
        viewModel.card.observe(this, Observer {
            viewModel.cardTransactionRequest.serialNumber = it.cardSerialNumber
            viewModel.requestAccountTransactions()
        })

        viewModel.transactionsLiveData.observe(this, Observer {
            if (viewModel.isLoadMore.value!!) {
                if (getRecycleViewAdaptor().itemCount == 0) getBindings().appbar.setExpanded(true)

                if (getRecycleViewAdaptor().itemCount > 0)
                    getRecycleViewAdaptor().removeItemAt(getRecycleViewAdaptor().itemCount - 1)

                val listToAppend: MutableList<HomeTransactionListData> = mutableListOf()
                val oldData = getRecycleViewAdaptor().getDataList()
                for (parentItem in it) {

                    var shouldAppend = false
                    for (i in 0 until oldData.size) {
                        if (parentItem.date == oldData[i].date) {
                            if (parentItem.transaction.size != oldData[i].transaction.size) {
                                shouldAppend = true
                                break
                            }
                            shouldAppend = true
                            break
                        }
                    }
                    if (!shouldAppend)
                        listToAppend.add(parentItem)
                }
                getRecycleViewAdaptor().addList(listToAppend)
            } else {
                if (it.isEmpty()) {
                    collapsingToolbar.disableScroll()
                    viewModel.state.isTxnsEmpty.set(true)
                } else {
                    collapsingToolbar.enableScroll()
                    viewModel.state.isTxnsEmpty.set(false)
                    getRecycleViewAdaptor().setList(it)
                }
            }
        })

        getBindings().rvTransaction.addOnScrollListener(
            object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager =
                        getBindings().rvTransaction.layoutManager as LinearLayoutManager
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    if (lastVisiblePosition == layoutManager.itemCount - 1) {
                        if (!viewModel.isLoadMore.value!! && !viewModel.isLast.value!!) {
                            viewModel.isLoadMore.value = true
                        }
                    }
                }
            })

        viewModel.isLoadMore.observe(this, Observer {
            if (it) {
                viewModel.cardTransactionRequest.number =
                    viewModel.cardTransactionRequest.number + 1
                val item =
                    getRecycleViewAdaptor().getDataForPosition(getRecycleViewAdaptor().itemCount - 1)
                        .copy()
                item.totalAmount = "loader"
                getRecycleViewAdaptor().run { addListItem(item) }
                viewModel.loadMore()
            }
        })

    }

    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.llAddFunds -> {
                trackAdjustPlatformEvent(AdjustEvents.TOP_UP_START.type)
                viewModel.card.value?.let { card ->
                    launchActivity<AddFundsActivity>(
                        requestCode = Constants.REQUEST_ADD_REMOVE_FUNDS,
                        type = FeatureSet.ADD_FUNDS
                    ) {
                        putExtra(AddFundsActivity.CARD, card)
                        cancelAllSnackBar()
                    }
                }
            }
            R.id.llFreezeSpareCard -> {
                if (FeatureProvisioning.getFeatureProvisioning(FeatureSet.UNFREEZE_CARD) && viewModel.card.value?.blocked == true) {
                    showBlockedFeatureAlert(this, FeatureSet.UNFREEZE_CARD)
                } else {
                    viewModel.freezeUnfreezeCard()
                }
            }
            R.id.llFreezePrimaryCard -> {
                if (FeatureProvisioning.getFeatureProvisioning(FeatureSet.UNFREEZE_CARD) && viewModel.card.value?.blocked == true) {
                    showBlockedFeatureAlert(this, FeatureSet.UNFREEZE_CARD)
                } else {
                    viewModel.freezeUnfreezeCard()
                }
            }
            R.id.llRemoveFunds -> {
                if (viewModel.card.value?.blocked == false) {
                    viewModel.card.value?.let { card ->
                        launchActivity<RemoveFundsActivity>(
                            requestCode = Constants.REQUEST_ADD_REMOVE_FUNDS,
                            type = FeatureSet.REMOVE_FUNDS
                        ) {
                            putExtra(AddFundsActivity.CARD, card)
                        }
                    }
                    cancelAllSnackBar()
                } else {
                    showToast("${getString(Strings.screen_remove_funds_display_text_unfreeze_feature)}^${AlertType.DIALOG.name}")
                }
            }
            R.id.llCardLimits -> {
                startActivityForResult(
                    CardLimitsActivity.getIntent(this, viewModel.card.value!!),
                    Constants.REQUEST_SET_LIMITS
                )
                cancelAllSnackBar()
            }
            R.id.rlFilter -> {
                if (viewModel.state.isTxnsEmpty.get() == false) {
                    openTransactionFilters()
                    cancelAllSnackBar()
                } else {
                    if (viewModel.state.filterCount.get() ?: 0 > 0) {
                        openTransactionFilters()
                        cancelAllSnackBar()
                    } else {
                        return@Observer
                    }
                }

            }

            viewModel.EVENT_FREEZE_UNFREEZE_CARD -> {
                cardFreezeUnfreeze = true
                viewModel.card.value?.blocked = viewModel.card.value?.blocked != true
                checkFreezeUnfreezStatus()
            }

            viewModel.EVENT_CARD_DETAILS -> {
                showCardDetailsPopup()
            }

            viewModel.EVENT_REMOVE_CARD -> {
                SessionManager.updateCardBalance {}
                cardRemoved = true
                showToast("Card successfully removed!")
                setupActionsIntent()
                finish()
            }
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                setupActionsIntent()
                finish()
            }
            R.id.ivRightIcon -> {
                if (Constants.CARD_TYPE_DEBIT == viewModel.state.cardType) {
                    primaryCardBottomSheet =
                        PrimaryCardBottomSheet(viewModel.card.value?.status ?: "", this)
                    primaryCardBottomSheet.show(supportFragmentManager, "")
                } else {
                    spareCardBottomSheet =
                        SpareCardBottomSheet(viewModel.card.value?.physical ?: false, this)
                    spareCardBottomSheet.show(supportFragmentManager, "")
                }
            }
        }
    }

    private fun openTransactionFilters() {
        startActivityForResult(
            TransactionFiltersActivity.newIntent(
                this,
                viewModel.transactionFilters
            ),
            RequestCodes.REQUEST_TXN_FILTER
        )
    }

    private fun getRecycleViewAdaptor(): TransactionsHeaderAdapter {
        return (rvTransaction.adapter as TransactionsHeaderAdapter)
    }

    private fun setupView() {
        viewModel.card.value = intent.getParcelableExtra(CARD)
        viewModel.state.cardStatus.set(viewModel.card.value?.status)
        viewModel.state.cardType = viewModel.card.value?.cardType ?: ""
        viewModel.state.cardPanNumber = viewModel.card.value?.maskedCardNo ?: ""
        viewModel.card.value?.cardName?.let { cardName ->
            viewModel.card.value?.nameUpdated?.let {
                if (it) {
                    viewModel.state.cardName = cardName
                } else {
                    viewModel.state.cardName = cardName.toCamelCase()
                }
            }
        }

        viewModel.card.value?.status?.let {
            when (it) {
                CardStatus.ACTIVE.name -> {
                }
                CardStatus.BLOCKED.name -> {
                }
                CardStatus.HOTLISTED.name -> {
                    showLostStolenSnackbar()
                }
                CardStatus.INACTIVE.name -> {
                }
            }
        }

        if (Constants.CARD_TYPE_DEBIT == viewModel.state.cardType) {
            viewModel.state.cardTypeText = Constants.TEXT_PRIMARY_CARD
            rlPrimaryCardActions.visibility = View.VISIBLE
            rlCardBalance.visibility = View.GONE
        } else {
            if (viewModel.card.value?.physical!!) {
                viewModel.state.cardTypeText = Constants.TEXT_SPARE_CARD_PHYSICAL
            } else {
                viewModel.state.cardTypeText = Constants.TEXT_SPARE_CARD_VIRTUAL
            }
            viewModel.getCardBalance { balance ->
                llAddFunds.alpha = 1f
                llAddFunds.isEnabled = true
                if (balance.parseToDouble() > 0) {
                    llRemoveFunds.isEnabled = true
                    llRemoveFunds.alpha = 1f
                } else {
                    llRemoveFunds.isEnabled = false
                    llRemoveFunds.alpha = 0.5f
                }
            }
            rlSpareCardActions.visibility = View.VISIBLE
        }
        btnCardDetails.setOnClickListener {
            viewModel.getCardDetails()
        }
    }

    override fun onResume() {
        super.onResume()
        checkFreezeUnfreezStatus()
    }

    private fun checkFreezeUnfreezStatus() {
        viewModel.card.value?.blocked?.let {
            val handler = Handler()
            handler.postDelayed({
                if (it) {
                    clSnackbar?.showSnackBar(
                        msg = getString(Strings.screen_cards_display_text_freeze_card),
                        viewBgColor = R.color.colorPrimary,
                        colorOfMessage = R.color.white,
                        gravity = Gravity.TOP,
                        duration = Snackbar.LENGTH_INDEFINITE,
                        actionText = underline(getString(Strings.screen_cards_display_text_freeze_card_action)),
                        clickListener = View.OnClickListener {
                            if (FeatureProvisioning.getFeatureProvisioning(FeatureSet.UNFREEZE_CARD)) {
                                showBlockedFeatureAlert(this, FeatureSet.UNFREEZE_CARD)
                            } else {
                                viewModel.freezeUnfreezeCard()
                            }
                        }
                    )
                    if (Constants.CARD_TYPE_DEBIT == viewModel.state.cardType) {
                        tvPrimaryCardStatus.text = "Unfreeze card"
                    } else {
                        tvSpareCardStatus.text = "Unfreeze card"
                    }
                } else {
                    cancelAllSnackBar()
                    if (Constants.CARD_TYPE_DEBIT == viewModel.state.cardType) {
                        tvPrimaryCardStatus.text = "Freeze card"
                    } else {
                        tvSpareCardStatus.text = "Freeze card"
                    }
                }
            }, 1000)

        }
    }

    private fun showLostStolenSnackbar() {
        clSnackbar?.showSnackBar(
            msg = getString(Strings.screen_cards_display_text_lost_stolen_card),
            viewBgColor = R.color.colorPrimary,
            colorOfMessage = R.color.white,
            gravity = Gravity.TOP,
            duration = Snackbar.LENGTH_INDEFINITE,
            actionText = underline(getString(Strings.screen_cards_display_text_lost_stolen_card_action)),
            clickListener = View.OnClickListener { startReorderCardFlow() }
        )
    }


    override fun onClick(eventType: Int) {

        if (Constants.CARD_TYPE_DEBIT == viewModel.state.cardType) {
            primaryCardBottomSheet.dismiss()
        } else {
            spareCardBottomSheet.dismiss()
        }

        when (eventType) {
            Constants.EVENT_ADD_CARD_NAME -> {
                startActivityForResult(
                    UpdateCardNameActivity.newIntent(this, viewModel.card.value!!),
                    Constants.REQUEST_CARD_NAME_UPDATED
                )
                cancelAllSnackBar()
            }
            Constants.EVENT_CHANGE_PIN -> {
                launchActivity<ChangeCardPinActivity>(type = FeatureSet.CHANGE_PIN) {
                    putExtra(
                        ChangeCardPinActivity.CARD_SERIAL_NUMBER,
                        viewModel.card.value?.cardSerialNumber ?: ""
                    )
                    cancelAllSnackBar()
                }
            }

            Constants.EVENT_FORGOT_CARD_PIN -> {
                viewModel.card.value?.cardSerialNumber?.let {
                    launchActivity<ForgotCardPinActivity>(type = FeatureSet.FORGOT_PIN) {
                        putExtra(
                            co.yap.yapcore.constants.Constants.CARD_SERIAL_NUMBER,
                            it
                        )
                        cancelAllSnackBar()
                    }
                }
            }

            Constants.EVENT_VIEW_STATEMENTS -> {
                viewModel.card.value?.let {
                    launchActivity<CardStatementsActivity> {
                        putExtra("card", it)
                        putExtra("isFromDrawer", false)
                        cancelAllSnackBar()
                    }
                }
            }
            Constants.EVENT_REPORT_CARD -> {
                viewModel.card.value?.let {
                    startActivityForResult(
                        ReportLostOrStolenCardActivity.newIntent(
                            this,
                            viewModel.card.value!!
                        ), Constants.REQUEST_REPORT_LOST_OR_STOLEN
                    )
                    cancelAllSnackBar()
                }
            }
            Constants.EVENT_REMOVE_CARD -> {
                confirm(
                    message = "Once removed, the balance from this card will be transferred to your main card.",
                    title = "Remove card from YAP account",
                    positiveButton = "CONFIRM",
                    negativeButton = "CANCEL"
                ) {
                    viewModel.removeCard()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Constants.REQUEST_CARD_NAME_UPDATED -> {
                if (resultCode == Activity.RESULT_OK) {
                    nameUpdated = true
                    viewModel.state.cardName = data?.getStringExtra("name").toString()
                    viewModel.card.value?.cardName = viewModel.state.cardName
                    viewModel.card.value?.nameUpdated = true
                }
            }

            Constants.REQUEST_ADD_REMOVE_FUNDS -> {
                if (resultCode == Activity.RESULT_OK) {
                    // Send Broadcast for updating transactions list in `Home Fragment`
                    val intent =
                        Intent(co.yap.yapcore.constants.Constants.BROADCAST_UPDATE_TRANSACTION)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

                    viewModel.card.value?.availableBalance =
                        data?.getStringExtra("newBalance").toString()
                    viewModel.state.cardBalance = data?.getStringExtra("newBalance").toString()
                        .toFormattedCurrency(true, SessionManager.getDefaultCurrency())
                    if (viewModel.card.value?.availableBalance.parseToDouble() > 0) {
                        llRemoveFunds.isEnabled = true
                        llRemoveFunds.alpha = 1f
                    } else {
                        llRemoveFunds.isEnabled = false
                        llRemoveFunds.alpha = 0.5f
                    }
                }
            }

            Constants.REQUEST_REPORT_LOST_OR_STOLEN -> {
                if (resultCode == Activity.RESULT_OK) {
                    setupCardBlockActionsIntent()
                    finish()
                }
            }
            Constants.REQUEST_SET_LIMITS -> {
                if (resultCode == Activity.RESULT_OK) {
                    limitsUpdated = true
                    viewModel.card.value = data?.getParcelableExtra<Card>("card")!!
                }
            }
            RequestCodes.REQUEST_REORDER_CARD -> {
                if (resultCode == Activity.RESULT_OK) {
                    setupCardReorderActionsIntent()
                    finish()
                }
            }
            RequestCodes.REQUEST_TXN_FILTER -> {
                if (resultCode == Activity.RESULT_OK) {
                    val filters: TransactionFilters? =
                        data?.getParcelableExtra<TransactionFilters?>("txnRequest")
                    if (viewModel.transactionFilters != filters) {
                        setTransactionRequest(filters)
                        viewModel.requestAccountTransactions()
                    }
                }
            }
            RequestCodes.REQUEST_FOR_TRANSACTION_NOTE_ADD_EDIT -> {

                val groupPosition = data.let {
                    it?.getIntExtra(
                        ExtraKeys.TRANSACTION_OBJECT_GROUP_POSITION.name,
                        -1
                    )
                }
                val childPosition = data.let { intent ->
                    intent?.getIntExtra(
                        ExtraKeys.TRANSACTION_OBJECT_CHILD_POSITION.name,
                        -1
                    )
                }
                if (groupPosition != -1 && childPosition != -1) {
                    getRecycleViewAdaptor().getDataForPosition(
                        groupPosition ?: 0
                    ).transaction[childPosition ?: 0].transactionNote =
                        (data?.getParcelableExtra(ExtraKeys.TRANSACTION_OBJECT_STRING.name) as Transaction).transactionNote
                    getRecycleViewAdaptor().getDataForPosition(
                        groupPosition ?: 0
                    ).transaction[childPosition ?: 0].transactionNoteDate =
                        (data.getParcelableExtra(ExtraKeys.TRANSACTION_OBJECT_STRING.name) as Transaction).transactionNoteDate
                    getRecycleViewAdaptor().notifyItemChanged(
                        groupPosition ?: 0,
                        getRecycleViewAdaptor().getDataForPosition(
                            groupPosition ?: 0
                        ).transaction[childPosition ?: 0]
                    )

                }
            }
        }
    }

    private fun setTransactionRequest(filters: TransactionFilters?) {
        filters?.let {
            viewModel.transactionFilters = it
            viewModel.cardTransactionRequest.number = 0
            viewModel.cardTransactionRequest.size = 20
            viewModel.cardTransactionRequest.txnType = getTxnType()
            viewModel.cardTransactionRequest.amountStartRange = it.amountStartRange
            viewModel.cardTransactionRequest.amountEndRange = it.amountEndRange
            viewModel.cardTransactionRequest.title = null
            viewModel.cardTransactionRequest.totalAppliedFilter = it.totalAppliedFilter
            viewModel.state.filterCount.set(it.totalAppliedFilter)
        }
    }

    private fun getTxnType(): String? {
        return if (viewModel.transactionFilters.incomingTxn == false && viewModel.transactionFilters.outgoingTxn == false || viewModel.transactionFilters.incomingTxn == true && viewModel.transactionFilters.outgoingTxn == true) {
            null
        } else if (viewModel.transactionFilters.incomingTxn == true)
            co.yap.yapcore.constants.Constants.MANUAL_CREDIT
        else
            co.yap.yapcore.constants.Constants.MANUAL_DEBIT
    }

    private fun startReorderCardFlow() {
        viewModel.card.value?.let {
            launchActivity<ReorderCardActivity>(
                requestCode = RequestCodes.REQUEST_REORDER_CARD,
                type = FeatureSet.REORDER_DEBIT_CARD
            ) {
                putExtra(ReorderCardActivity.CARD, it)
            }
        }
    }

    private fun showCardDetailsPopup() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_card_details)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnClose = dialog.findViewById(R.id.ivCross) as ImageView
        var cardType = ""
        var cardNumber: String? = ""
        if (null != viewModel.cardDetail.cardNumber) {
            if (viewModel.cardDetail.cardNumber?.trim()?.contains(" ")!!) {
                cardNumber = viewModel.cardDetail.cardNumber
            } else {
                if (viewModel.cardDetail.cardNumber?.length == 16) {
                    val formattedCardNumber: StringBuilder =
                        StringBuilder(viewModel.cardDetail.cardNumber ?: "")
                    formattedCardNumber.insert(4, " ")
                    formattedCardNumber.insert(9, " ")
                    formattedCardNumber.insert(14, " ")
                    cardNumber = formattedCardNumber.toString()
                }
            }
        }

        cardType = if (Constants.CARD_TYPE_DEBIT == viewModel.state.cardType) {
            "Primary card"
        } else {
            if (viewModel.card.value?.physical == true) {
                Constants.TEXT_SPARE_CARD_PHYSICAL
            } else {
                Constants.TEXT_SPARE_CARD_VIRTUAL
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        val indicator = dialog.findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)
        val viewPager = dialog.findViewById<ViewPager2>(R.id.cardsPager)
        val pagerList = mutableListOf<CardDetailsModel>()
        pagerList.add(
            CardDetailsModel(
                cardExpiry = viewModel.cardDetail.expiryDate,
                cardType = cardType,
                cardNumber = cardNumber,
                cardCvv = viewModel.cardDetail.cvv,
                displayName = viewModel.card.value?.cardName
            )
        )
        pagerList.add(
            CardDetailsModel(
                cardExpiry = viewModel.cardDetail.expiryDate,
                cardType = cardType,
                cardNumber = cardNumber,
                cardCvv = viewModel.cardDetail.cvv,
                displayName = viewModel.card.value?.cardName
            )
        )
        val cardDetailsPagerAdapter = CardDetailsDialogPagerAdapter(pagerList)
        viewPager?.adapter = cardDetailsPagerAdapter
        indicator?.setViewPager2(viewPager)
        dialog.show()
    }


    private fun setUpTransactionsListRecyclerView() {
        rvTransaction.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rvTransaction.layoutManager = layoutManager
        rvTransaction.adapter = TransactionsHeaderAdapter(mutableListOf(), adaptorlistener)
    }

    private val adaptorlistener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            val groupPosition = data as Int
            val transaction: Transaction? =
                getRecycleViewAdaptor()?.getDataForPosition(groupPosition)?.transaction?.get(pos)
            launchActivity<TransactionDetailsActivity>(requestCode = RequestCodes.REQUEST_FOR_TRANSACTION_NOTE_ADD_EDIT) {
                putExtra(
                    ExtraKeys.TRANSACTION_OBJECT_STRING.name,
                    transaction
                )
                putExtra(
                    ExtraKeys.TRANSACTION_OBJECT_GROUP_POSITION.name,
                    groupPosition
                )
                putExtra(
                    ExtraKeys.TRANSACTION_OBJECT_CHILD_POSITION.name,
                    pos
                )

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterTransactionBroadcast()
        viewModel.clickEvent.removeObservers(this)
    }

    override fun onBackPressed() {
        setupActionsIntent()
        super.onBackPressed()
    }

    private fun setupActionsIntent() {
        if (cardFreezeUnfreeze || cardRemoved || limitsUpdated || nameUpdated) {
            val updateCard = viewModel.card.value!!
            updateCard.cardBalance = viewModel.state.cardBalance
            updateCard.cardName = viewModel.state.cardName

            if (cardFreezeUnfreeze) {
                if (viewModel.card.value?.blocked == true)
                    updateCard.status = "BLOCKED"
                else
                    updateCard.status = "ACTIVE"
            }

            val returnIntent = Intent()
            returnIntent.putExtra("card", updateCard)
            returnIntent.putExtra("cardRemoved", cardRemoved)
            setResult(Activity.RESULT_OK, returnIntent)
        }
    }

    private fun setupCardBlockActionsIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra("cardBlocked", true)
        setResult(Activity.RESULT_OK, returnIntent)
    }

    private fun setupCardReorderActionsIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra("cardReorder", true)
        setResult(Activity.RESULT_OK, returnIntent)
    }

    private fun registerTransactionBroadcast() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadCastReceiver,
                IntentFilter(co.yap.yapcore.constants.Constants.BROADCAST_UPDATE_TRANSACTION)
            )
    }

    private fun unregisterTransactionBroadcast() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastReceiver)
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                co.yap.yapcore.constants.Constants.BROADCAST_UPDATE_TRANSACTION -> {
                    viewModel.requestAccountTransactions()
                }
            }
        }
    }

}
