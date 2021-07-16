package co.yap.widgets

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import co.yap.yapcore.R
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.helpers.ThemeColorUtils
import co.yap.yapcore.helpers.Utils
import kotlinx.android.synthetic.main.core_payment_card.view.*
import kotlin.math.roundToInt


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CorePaymentCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var cardSizeType: Int = 2
    private var cardSizeTypeSmall: Int = 0
    private var cardSizeTypeMedium: Int = 1
    private var cardSizeTypeLarge: Int = 2

    var view: View = LayoutInflater.from(context)
        .inflate(R.layout.core_payment_card, this, true)

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CorePaymentCard, 0, 0)
            cardSizeType = typedArray.getInt(
                R.styleable.CorePaymentCard_card_size_type,
                cardSizeTypeLarge
            )
            ivCardType.setImageDrawable(typedArray.getDrawable(R.styleable.CorePaymentCard_card_type))
            setCardSizeTypeDimensions()
            typedArray.recycle()
        }
    }

    private fun setCardSizeTypeDimensions() {
        when (cardSizeType) {
            cardSizeTypeSmall -> {

                tvBankName.textSize = 5f
                tvCardNumber.textSize = 7f
                tvCardExpiry.textSize = 5f

                val ivChipHeight = context.applicationContext.resources.getDimension(R.dimen._8sdp)
                val ivChipWidth = context.applicationContext.resources.getDimension(R.dimen._8sdp)

                setUpImageDimensions(
                    ivCardType,
                    ivChipHeight.roundToInt(),
                    ivChipWidth.roundToInt()
                )

                setUpImageDimensions(
                    ivChip,
                    ivChipHeight.roundToInt(),
                    ivChipWidth.roundToInt()
                )
                setUpImageDimensions(
                    ivInfo,
                    ivChipHeight.roundToInt(),
                    ivChipWidth.roundToInt()
                )
                setViewMargin(
                    tvCardExpiry,
                    bottom = R.dimen._4sdp
                )
                setViewPadding(
                    clMainContainer,
                    left = R.dimen._6sdp,
                    right = R.dimen._5sdp
                )
                setViewPadding(
                    tvBankName,
                    left = R.dimen._2sdp,
                    right = R.dimen.margin_normal
                )

                clRoot.radius =
                    context.applicationContext.resources.getDimension(R.dimen._3sdp)
                clRoot.elevation =
                    context.applicationContext.resources.getDimension(R.dimen.margin_two_dp)
            }
            else -> {
                setViewMargin(
                    tvCardExpiry,
                    bottom = R.dimen._8sdp
                )
                tvBankName.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.applicationContext.resources.getDimension(R.dimen._10sdp)
                )
                tvCardNumber.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.applicationContext.resources.getDimension(R.dimen._13sdp)
                )
                tvCardExpiry.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.applicationContext.resources.getDimension(R.dimen._10sdp)
                )

                val ivChipHeight = context.applicationContext.resources.getDimension(R.dimen._22sdp)
                val ivChipWidth =
                    context.applicationContext.resources.getDimension(R.dimen._22sdp)

                val ivInfoHeight = context.applicationContext.resources.getDimension(R.dimen._18sdp)
                val ivInfoWidth =
                    context.applicationContext.resources.getDimension(R.dimen._18sdp)

                setUpImageDimensions(ivChip, ivChipHeight.roundToInt(), ivChipWidth.roundToInt())
                setUpImageDimensions(ivInfo, ivInfoHeight.roundToInt(), ivInfoWidth.roundToInt())
                setUpImageDimensions(
                    ivCardType,
                    ivChipHeight.roundToInt(),
                    ivChipWidth.roundToInt()
                )

                setViewPadding(
                    clMainContainer,
                    bottom = R.dimen.margin_small
                )

                setViewPadding(
                    clMainContainer,
                    left = R.dimen._12sdp,
                    right = R.dimen._10sdp
                )

                setViewPadding(
                    tvBankName,
                    left = R.dimen.small,
                    right = R.dimen.margin_normal
                )

                clRoot.radius = context.applicationContext.resources.getDimension(R.dimen._6sdp)
                clRoot.elevation = context.applicationContext.resources.getDimension(R.dimen._4sdp)
            }
        }
    }

    private fun setViewPadding(
        view: View,
        left: Int = -1,
        top: Int = -1,
        right: Int = -1,
        bottom: Int = -1
    ) {
        view.setPadding(
            if (left == -1) 0 else context.applicationContext.resources.getDimension(left).toInt(),
            if (top == -1) 0 else context.applicationContext.resources.getDimension(top).toInt(),
            if (right == -1) 0 else context.applicationContext.resources.getDimension(right)
                .toInt(),
            if (bottom == -1) 0 else context.applicationContext.resources.getDimension(bottom)
                .toInt()
        )
    }

    private fun setViewMargin(
        tvCardExpiry: View,
        left: Int = -1,
        top: Int = -1,
        right: Int = -1,
        bottom: Int = -1
    ) {
        val p = tvCardExpiry.layoutParams as ConstraintLayout.LayoutParams
        p.setMargins(
            if (left == -1) 0 else context.applicationContext.resources.getDimension(left).toInt(),
            if (top == -1) 0 else context.applicationContext.resources.getDimension(top).toInt(),
            if (right == -1) 0 else context.applicationContext.resources.getDimension(right)
                .toInt(),
            if (bottom == -1) 0 else context.applicationContext.resources.getDimension(bottom)
                .toInt()
        )
        tvCardExpiry.layoutParams = p
    }

    private fun setUpImageDimensions(imageView: ImageView, newHeight: Int, newWidth: Int) {
        imageView.requestLayout()
        imageView.layoutParams.height = newHeight
        imageView.layoutParams.width = newWidth
    }

    fun setCardNickname(cardName: String) {
        tvBankName.text = cardName
    }

    fun setCardNumber(cardNumber: String) {
        tvCardNumber.text = Utils.getFormattedCardNumber(cardNumber)
    }

    fun setCardExpiry(cardExpiry: String) {
        tvCardExpiry.text = DateUtils.convertTopUpDate(cardExpiry)
    }

    fun setCardBackground(bgCardColor: String) {
        try {
            val bg = clMainContainer.background
            bg.setTint(Color.parseColor("#$bgCardColor"))
        } catch (ex: IllegalArgumentException) {
            clMainContainer.setBackgroundColor(
                ThemeColorUtils.colorPrimaryDarkAttribute(context)
            )
        }
    }

    fun setCardLogoByType(cardType: String) {
        when (cardType) {
            Constants.VISA -> ivCardType.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_visa_card
                )
            )
            Constants.MASTER -> ivCardType.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_master_card_logo
                )
            )
            Constants.AMEX -> ivCardType.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_amex_bank
                )
            )
            Constants.JCB -> ivCardType.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_jcb_bank
                )
            )
            Constants.DINNERS -> ivCardType.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_dinersclub_bank
                )
            )
            Constants.DISCOVER -> ivCardType.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_discover_bank
                )
            )
            else -> {
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_visa_card
                )
            }

        }
    }

}