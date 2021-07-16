package co.yap.modules.dashboard.yapit.topup.cardslisting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import co.yap.BR
import co.yap.R
import co.yap.app.YAPApplication
import co.yap.databinding.ActivityTopupCardsBinding
import co.yap.modules.dashboard.yapit.topup.addtopupcard.activities.AddTopUpCardActivityV2
import co.yap.modules.dashboard.yapit.topup.carddetail.TopupCardDetailActivity
import co.yap.modules.dashboard.yapit.topup.topupamount.activities.TopUpCardActivity
import co.yap.modules.others.helper.Constants
import co.yap.modules.others.helper.Constants.EVENT_ADD_TOPUP_CARD
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.translation.Strings
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants.TYPE_ADD_CARD
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.interfaces.OnItemClickListener
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import timber.log.Timber
import kotlin.math.abs

class TopUpBeneficiariesActivity : BaseBindingActivity<ITopUpBeneficiaries.ViewModel>(),
    ITopUpBeneficiaries.View {
    companion object {
        fun newIntent(context: Context, successButtonLabel: String): Intent {
            val intent = Intent(context, TopUpBeneficiariesActivity::class.java)
            intent.putExtra("successButtonLabel", successButtonLabel)
            return intent
        }
    }

    override var successButtonLabel: String = ""
    private lateinit var mAdapter: TopUpBeneficiariesAdapter
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_topup_cards
    override val viewModel: ITopUpBeneficiaries.ViewModel
        get() = ViewModelProviders.of(this).get(
            TopUpBeneficiariesViewModel::class.java
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addObservers()
        setupCards()
        successButtonLabel =
            (intent?.getValue("successButtonLabel", ExtraType.STRING.name) as? String) ?: ""

    }

    private fun setupCards() {
        mAdapter = TopUpBeneficiariesAdapter(this, mutableListOf())
        getBinding().rvTopUpCards.setSlideOnFling(false)
        getBinding().rvTopUpCards.setOverScrollEnabled(true)
        getBinding().rvTopUpCards.adapter = mAdapter
        getBinding().rvTopUpCards.addOnItemChangedListener { viewHolder, adapterPosition ->
            updateSelection(viewHolder, adapterPosition)
            if (viewHolder is TopUpBeneficiariesAdapter.TopUpCardViewHolder)
                viewHolder.binding.parent?.alpha = 1f
            else {
                if (viewHolder is TopUpBeneficiariesAdapter.TopUpEmptyItemViewHolder) { // in case of no result
                    val prams = viewHolder.itemTopUpCardEmptyBinding.lycard.layoutParams
                    if (prams is RecyclerView.LayoutParams) {
                        prams.setMargins(30, 30, 30, 30)
                        viewHolder.itemTopUpCardEmptyBinding.lycard?.layoutParams = prams
                    }
                }
            }
        }
        mAdapter.setItemListener(listener)
        getBinding().rvTopUpCards.smoothScrollToPosition(0)
        getBinding().rvTopUpCards.setItemTransitionTimeMillis(150)
        getBinding().rvTopUpCards.addScrollStateChangeListener(object :
            DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder> {
            override fun onScroll(
                scrollPosition: Float,
                currentPosition: Int,
                newPosition: Int,
                currentHolder: RecyclerView.ViewHolder?,
                newCurrent: RecyclerView.ViewHolder?
            ) {
                if (newCurrent is TopUpBeneficiariesAdapter.TopUpCardViewHolder) {

                    val crItem = getBinding().rvTopUpCards.currentItem
                    var totalItems = 0
                    getBinding().rvTopUpCards.layoutManager?.itemCount?.let {
                        totalItems = it
                    }

                    var p: RecyclerView.ViewHolder? = null
                    var n: RecyclerView.ViewHolder? = null

                    if (crItem >= 1) {
                        p =
                            getBinding().rvTopUpCards.getViewHolder(crItem - 1)
                        n =
                            getBinding().rvTopUpCards.getViewHolder(crItem + 1)
                    } else {
                        n =
                            getBinding().rvTopUpCards.getViewHolder(crItem + 1)
                    }

                    val alphaFactor = if (abs(scrollPosition) < .5f) .5f else abs(scrollPosition)

                    if (p is TopUpBeneficiariesAdapter.TopUpCardViewHolder)
                        p.binding.parent.alpha = alphaFactor

                    if (n is TopUpBeneficiariesAdapter.TopUpCardViewHolder)
                        n.binding.parent.alpha = alphaFactor

                    if (currentHolder is TopUpBeneficiariesAdapter.TopUpCardViewHolder) {

                        val currentAlpha =
                            if (1 - abs(scrollPosition) < .5f) .5f else 1 - abs(scrollPosition)
                        Timber.d("topupcards: sc $scrollPosition alphaRatio $alphaFactor currentAlpha $currentAlpha")
                        currentHolder.binding.parent.alpha = currentAlpha
                    }
                }
            }

            override fun onScrollEnd(
                currentItemHolder: RecyclerView.ViewHolder,
                position: Int
            ) {
                if (currentItemHolder is TopUpBeneficiariesAdapter.TopUpCardViewHolder) {

                    val crItem = getBinding().rvTopUpCards.currentItem
                    var totalItems = 0
                    getBinding().rvTopUpCards.layoutManager?.itemCount?.let {
                        totalItems = it
                    }

                    var pp: RecyclerView.ViewHolder? = null
                    var nn: RecyclerView.ViewHolder? = null

                    if (crItem >= 1) {
                        pp =
                            getBinding().rvTopUpCards.getViewHolder(crItem - 2)
                        nn =
                            getBinding().rvTopUpCards.getViewHolder(crItem + 3)
                    } else {
                        nn =
                            getBinding().rvTopUpCards.getViewHolder(crItem + 2)
                    }

                    if (pp is TopUpBeneficiariesAdapter.TopUpCardViewHolder)
                        pp.binding.parent.alpha = 0.5f

                    if (nn is TopUpBeneficiariesAdapter.TopUpCardViewHolder)
                        nn.binding.parent.alpha = 0.5f
                }
            }

            override fun onScrollStart(
                currentItemHolder: RecyclerView.ViewHolder,
                position: Int
            ) {

            }
        })
        getBinding().rvTopUpCards.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                //.setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()
        )
    }

    val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            viewModel.clickEvent.setPayload(
                SingleClickEvent.AdaptorPayLoadHolder(
                    view,
                    data,
                    pos
                )
            )
            viewModel.clickEvent.setValue(view.id)
        }
    }

    private fun updateSelection(viewHolder: RecyclerView.ViewHolder?, adapterPosition: Int) {
        val item = mAdapter.getDataForPosition(adapterPosition)
        if (item.alias == "addCard") {
            viewModel.state.valid.set(false)
            viewModel.state.alias.set("")
        } else {
            viewModel.state.valid.set(true)
            viewModel.state.alias.set(item.alias)
        }
    }

    private fun addObservers() {
        viewModel.clickEvent.observe(this, clickEventObserver)
        viewModel.topUpCards.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                mAdapter.setList(it.toMutableList())
            } else {
                viewModel.state.valid.set(false)
                viewModel.state.alias.set("")
            }
            viewModel.updateCardCount()
        })
    }

    private val clickEventObserver = Observer<Int> {
        if (viewModel.clickEvent.getPayload() != null) {
            val pos = viewModel.clickEvent.getPayload()?.position
            val view = viewModel.clickEvent.getPayload()?.view
            viewModel.clickEvent.setPayload2(null)

            if (pos != getBinding().rvTopUpCards.currentItem) {
                pos?.let { it -> getBinding().rvTopUpCards.smoothScrollToPosition(it) }
                return@Observer
            }
        }
        when (it) {
            R.id.btnSelect -> {
                if (mAdapter.getDataList().isNotEmpty()) {
                    val item: TopUpCard? =
                        mAdapter.getDataForPosition(getBinding().rvTopUpCards.currentItem)
                    if (item?.alias != "addCard")
                        item?.let { card ->
                            startTopUpActivity(card)
                        }
                }
            }
            R.id.paymentCard -> {
                if (mAdapter.getDataList().isNotEmpty()) {
                    val item = mAdapter.getDataForPosition(getBinding().rvTopUpCards.currentItem)
                    startTopUpActivity(item)
                }
            }

            R.id.imgStatus -> {
                if (mAdapter.getDataList().isNotEmpty()) {
                    val item = mAdapter.getDataForPosition(getBinding().rvTopUpCards.currentItem)
                    openCardDetail(item)
                }
            }

            R.id.lycard -> {
                if (viewModel.cardLimits?.remaining ?: 0 > 0) {
                    addCardProcess()
                } else {
                    showToast(
                        "${
                            getString(Strings.screen_add_topup_card_limit_text_title).format(
                                viewModel.cardLimits?.maxLimit
                            )
                        }^${AlertType.DIALOG.name}"
                    )
                }
            }
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                onBackPressed()
            }
            R.id.ivRightIcon -> {
                if (viewModel.cardLimits?.remaining ?: 0 >= 0) {
                    addCardProcess()
                } else {
                    showToast(
                        "${
                            getString(Strings.screen_add_topup_card_limit_text_title).format(
                                viewModel.cardLimits?.maxLimit
                            )
                        }^${AlertType.DIALOG.name}"
                    )
                }
            }
        }
    }

    private fun startTopUpActivity(item: TopUpCard) {
        launchActivity<TopUpCardActivity>(
            requestCode = RequestCodes.REQUEST_TOP_UP_BENEFICIARY,
            type = FeatureSet.TOP_UP_BY_EXTERNAL_CARD
        ) {
            putExtra(co.yap.yapcore.constants.Constants.CARD, item)
            putExtra("successButtonLabel", successButtonLabel)
        }
    }

    private fun openCardDetail(card: TopUpCard) {
        startActivityForResult(
            TopupCardDetailActivity.getIntent(
                this@TopUpBeneficiariesActivity,
                TopUpCard(
                    card.id,
                    card.logo,
                    card.expiry,
                    card.number,
                    card.alias,
                    card.color
                )
            ),
            Constants.EVENT_DELETE_TOPUP_CARD
        )
    }

    private fun addCardProcess() {
        getUrl()?.let {
            launchActivity<AddTopUpCardActivityV2>(requestCode = EVENT_ADD_TOPUP_CARD) {
                putExtra(co.yap.yapcore.constants.Constants.KEY, it)
                putExtra(co.yap.yapcore.constants.Constants.TYPE, TYPE_ADD_CARD)
            }
        }

    }

    private fun getUrl(): String? {
        return when (YAPApplication.configManager?.flavor ?: "") {
            "live" -> {
                "https://ae-prod-hci.yap.com/admin-web/HostedSessionIntegration.html"
            }
            "Preprod" -> {
                "https://ae-preprod-hci.yap.com/admin-web/HostedSessionIntegration.html"
            }
            "dev" -> {
                "https://dev-hci.yap.co/admin-web/HostedSessionIntegration.html"
            }
            "qa" -> {
                "https://qa-hci.yap.co/admin-web/HostedSessionIntegration.html"
            }
            "stg","yapinternal" -> {
                "https://stg-hci.yap.co/admin-web/HostedSessionIntegration.html"
            }
            else -> null
        }
    }

    fun getBinding(): ActivityTopupCardsBinding {
        return viewDataBinding as ActivityTopupCardsBinding
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.EVENT_ADD_TOPUP_CARD && resultCode == Activity.RESULT_OK) {
            if (true == data?.getBooleanExtra("isCardAdded", false)) {
                viewModel.getPaymentCards()
                val card: TopUpCard? = data.getParcelableExtra<TopUpCard>("card")
                card?.let {
                    startTopUpActivity(it)
                }
            }
        } else if (requestCode == Constants.EVENT_DELETE_TOPUP_CARD && resultCode == Activity.RESULT_OK) {
            if (true == data?.getBooleanExtra("isCardDeleted", false)) {
                showToast("Card Removed Successfully")
                viewModel.getPaymentCards()
            }
        } else if (requestCode == RequestCodes.REQUEST_TOP_UP_BENEFICIARY && resultCode == Activity.RESULT_OK) {
            if (true == data?.getBooleanExtra(
                    co.yap.yapcore.constants.Constants.TOP_UP_VIA_EXTERNAL_CARD,
                    false
                )
            ) {
                val intent = Intent()
                intent.putExtra(
                    RequestCodes.REQUEST_SHOW_BENEFICIARY.toString(),
                    RequestCodes.REQUEST_SHOW_BENEFICIARY
                )
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}