package co.yap.yapcore.binders


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.databinding.*
import androidx.recyclerview.widget.RecyclerView
import co.yap.modules.placesautocomplete.adapter.PlacesAutoCompleteAdapter
import co.yap.modules.placesautocomplete.model.Place
import co.yap.networking.cards.responsedtos.Card
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.translation.Translator
import co.yap.widgets.CoreButton
import co.yap.widgets.CoreDialerPad
import co.yap.widgets.CorePaymentCard
import co.yap.widgets.MaskTextWatcher
import co.yap.widgets.otptextview.OTPListener
import co.yap.widgets.otptextview.OtpTextView
import co.yap.yapcore.R
import co.yap.yapcore.enums.*
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.helpers.StringUtils
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.loadImage
import co.yap.yapcore.interfaces.IBindable
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat

object UIBinder {
    @BindingAdapter(requireAll = false, value = ["adaptor", "selectedListener"])
    @JvmStatic
    fun setSpinnerAdaptor(
        spinner: Spinner,
        options: ArrayList<String>, listener: OnItemClickListener?
    ) {
        val myListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                view?.let { listener?.onItemClick(view, options[position], position) }
            }
        }
        spinner.onItemSelectedListener = myListener
        val dataAdapter = ArrayAdapter<String>(
            spinner.context,
            android.R.layout.simple_spinner_item,
            options
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter
    }

    @BindingAdapter("tvColor")
    @JvmStatic
    fun updateTextColor(view: TextView, position: Int) {
        if (position == -1) return
        try {
            val colors = view.context.resources.getIntArray(R.array.analyticsColors)
            view.setTextColor(colors[position % colors.size])
        } catch (ex: Exception) {

        }
    }

    @BindingAdapter("cardStatus")
    @JvmStatic
    fun setCardStatus(view: ImageView, card: TopUpCard?) {
        card?.expiry?.let {
            if (DateUtils.isDatePassed(it, SimpleDateFormat("MMyy"))) {
                view.setImageResource(R.drawable.ic_status_expired)
                view.visibility = VISIBLE
            } else {
                //view.setImageResource(R.drawable.ic_card_status)
                view.visibility = VISIBLE
            }
        }
    }

    @BindingAdapter("bitmap")
    @JvmStatic
    fun setImageBitmap(view: ImageView, bitmap: Bitmap?) {
        if (bitmap != null)
            view.setImageBitmap(bitmap)
    }

    @BindingAdapter("stringToBitmap")
    @JvmStatic
    fun getPhoto(view: ImageView, photoUri: String?) {
        if (photoUri == null) {
            view.visibility = GONE
            return
        }

        if (photoUri.contains("http")) {
            view.loadImage(photoUri)
        } else {
            val cursor = view.context.contentResolver.query(
                Uri.parse(photoUri),
                arrayOf(ContactsContract.Contacts.Photo.PHOTO), null, null, null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val data = cursor.getBlob(0)
                    if (data != null) {
                        cursor.close()
                        val bitmap = byteArrayToBitmap(data)
                        view.visibility = VISIBLE
                        view.setImageBitmap(bitmap)
                    }
                }
                cursor.close()
            }
            cursor?.close()
        }
    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @BindingAdapter("cardSeeDetailVisibility")
    @JvmStatic
    fun setCardDetailLayoutVisibility(linearLayout: LinearLayout, card: Card) {
        when (card.status) {
            CardStatus.ACTIVE.name, CardStatus.PIN_BLOCKED.name -> {
                if (card.cardType == CardType.DEBIT.type) {
                    if (PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus && !card.pinCreated)
                        linearLayout.visibility = GONE
                } else {
                    linearLayout.visibility = VISIBLE
                }
            }
            else -> linearLayout.visibility = GONE
        }
    }

    // Delivery status and core action Button
    @BindingAdapter("cardStatus")
    @JvmStatic
    fun setCardStatus(linearLayout: LinearLayout, card: Card) {
        if (CardStatus.valueOf(card.status).name.isNotEmpty()) {
            when (CardStatus.valueOf(card.status)) {
                CardStatus.ACTIVE,CardStatus.PIN_BLOCKED -> {
                    if (card.cardType == CardType.DEBIT.type) {
                        if (PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus && !card.pinCreated)
                            linearLayout.visibility = VISIBLE
                        else
                            linearLayout.visibility = GONE
                    } else
                        linearLayout.visibility = GONE
                }
                CardStatus.BLOCKED, CardStatus.INACTIVE, CardStatus.HOTLISTED -> {
                    linearLayout.visibility = VISIBLE
                }
            }
        }
    }

    // Top right card status image
    @BindingAdapter("cardStatus")
    @JvmStatic
    fun setCardStatus(imageView: ImageView, card: Card) {
        if (CardStatus.valueOf(card.status).name.isNotEmpty())
            when (CardStatus.valueOf(card.status)) {
                CardStatus.ACTIVE,CardStatus.PIN_BLOCKED -> {
                    if (card.cardType == CardType.DEBIT.type) {
                        if (PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus && !card.pinCreated) {
                            imageView.visibility = VISIBLE
                            imageView.setImageResource(R.drawable.ic_status_ontheway)
                        } else
                            imageView.visibility = GONE
                    } else
                        imageView.visibility = GONE
                }
                CardStatus.BLOCKED -> {
                    imageView.visibility = VISIBLE
                    imageView.setImageResource(R.drawable.ic_status_frozen)
                }
                CardStatus.HOTLISTED -> {
                    imageView.visibility = VISIBLE
                    imageView.setImageResource(R.drawable.ic_status_frozen)
                }
                CardStatus.INACTIVE -> {
                    imageView.visibility = VISIBLE
                    imageView.setImageResource(R.drawable.ic_status_ontheway)
                }
                CardStatus.EXPIRED -> {
                    imageView.visibility = VISIBLE
                    imageView.setImageResource(R.drawable.ic_status_expired)
                }

            }
    }

    // Card status message text
    @BindingAdapter("cardStatus")
    @JvmStatic
    fun setCardStatus(text: TextView, card: Card) {
        if (CardStatus.valueOf(card.status).name.isNotEmpty())
            when (CardStatus.valueOf(card.status)) {
                CardStatus.ACTIVE,CardStatus.PIN_BLOCKED -> {
                    if (card.cardType == CardType.DEBIT.type) {
                        if (PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus && !card.pinCreated)
                            setTextForInactiveCard(text = text, card = card)
                    } else
                        text.visibility = GONE
                }
                CardStatus.BLOCKED -> {
                    text.visibility = VISIBLE
                    text.text = Translator.getString(
                        text.context,
                        R.string.screen_cards_display_text_freeze_card
                    )
                }
                CardStatus.HOTLISTED -> {
                    text.visibility = VISIBLE
                    text.text = Translator.getString(
                        text.context,
                        R.string.screen_cards_display_text_hotlisted
                    )
                }
                CardStatus.INACTIVE -> {
                    setTextForInactiveCard(text = text, card = card)
                }
                CardStatus.EXPIRED -> {
                    text.visibility = VISIBLE
                    text.text = Translator.getString(
                        text.context,
                        R.string.screen_cards_display_text_expired_card
                    )
                }
            }
    }

    private fun setTextForInactiveCard(text: TextView, card: Card) {
        when (card.cardType) {
            CardType.DEBIT.type -> {
                if (card.deliveryStatus == CardDeliveryStatus.SHIPPED.name && PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus) {
                    text.visibility = VISIBLE
                    text.text = Translator.getString(
                        text.context,
                        R.string.screen_cards_display_text_set_message
                    )
                } else {
                    text.visibility = VISIBLE
                    text.text = Translator.getString(
                        text.context,
                        R.string.screen_cards_display_text_pending_delivery
                    )
                }
            }
            else -> {
                // use for other card type like Gold , platinium etc
            }
        }
    }

    //Core action Button text
    @BindingAdapter("cardButtonStatus")
    @JvmStatic
    fun setcardButtonStatus(coreButton: TextView, card: Card) {
        if (CardStatus.valueOf(card.status).name.isNotEmpty())
            when (CardStatus.valueOf(card.status)) {
                CardStatus.ACTIVE,CardStatus.PIN_BLOCKED -> {
                    if (card.cardType == CardType.DEBIT.type) {
                        if (PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus && !card.pinCreated)
                            setCardButtonTextForInactive(coreButton, card)
                        else
                            coreButton.visibility = GONE
                    } else
                        coreButton.visibility = GONE
                }
                CardStatus.BLOCKED -> {
                    coreButton.visibility = VISIBLE
                    coreButton.text = Translator.getString(
                        coreButton.context,
                        R.string.screen_cards_button_unfreeze_card
                    )
                }
                CardStatus.HOTLISTED -> {
                    coreButton.visibility = VISIBLE
                    coreButton.text = Translator.getString(
                        coreButton.context,
                        R.string.screen_cards_button_reorder_card
                    )
                }
                CardStatus.INACTIVE -> {
                    if (card.deliveryStatus == CardDeliveryStatus.SHIPPED.name && PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus)
                        setCardButtonTextForInactive(coreButton, card)
                    else
                        coreButton.visibility = GONE
                }

                CardStatus.EXPIRED -> {
                    //visible this when we needed in future
                    coreButton.visibility = GONE
                    coreButton.text = Translator.getString(
                        coreButton.context,
                        R.string.screen_cards_button_update_card
                    )
                }
            }
    }

    private fun setCardButtonTextForInactive(coreButton: TextView, card: Card) {
        when (card.cardType) {
            CardType.DEBIT.type -> {
                coreButton.visibility = VISIBLE
                coreButton.text = Translator.getString(
                    coreButton.context,
                    R.string.screen_cards_display_text_set_pin
                )
            }
            else -> {
                // use for other card type like Gold , platinium etc
            }
        }
    }


    // Card status message text
    @BindingAdapter("underline_text")
    @JvmStatic
    fun setCardStatus(text: TextView, value: String) {
        val content = SpannableString(value)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        text.text = content
    }

    @BindingAdapter("src")
    @JvmStatic
    fun setImageResId(view: ImageView, resId: Int) {
        if (resId != -1)
            view.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("CoreDialerError")
    fun setDialerErrorMessage(view: CoreDialerPad, error: String) {
        if (!error.isEmpty()) view.showError(error) else view.removeError()
    }

    @BindingAdapter("src")
    @JvmStatic
    fun setImageResId(view: ImageView, drawable: Drawable?) {
        if (drawable != null)
            view.setImageDrawable(drawable)
    }

    @BindingAdapter("text")
    @JvmStatic
    fun setText(view: TextView, text: String) {
        view.text = Translator.getString(view.context, text)
    }

    @BindingAdapter("text")
    @JvmStatic
    fun setText(view: TextView, textId: Int) {
        view.text = Translator.getString(view.context, textId)
    }

    @BindingAdapter("text", "concat")
    @JvmStatic
    fun setText(view: TextView, textKey: String, concat: Array<String>) {
        view.text = Translator.getString(view.context, textKey, *concat)
    }

    @BindingAdapter("textVal", "concatVal")
    @JvmStatic
    fun setconcatVal(tv: TextView, textKey: String, concat: String) {
        Translator.getString(tv.context, textKey)
        tv.text = String.format(Translator.getString(tv.context, textKey), '\n' + concat)
    }

    @BindingAdapter("textVal", "concatenatedVal")
    @JvmStatic
    fun setconcatedVal(tv: TextView, textKey: String, concat: String?) {
        if (!concat.isNullOrEmpty() && !concat.equals("null")) {
            tv.visibility = VISIBLE
            Translator.getString(tv.context, textKey)
            tv.text = String.format(Translator.getString(tv.context, textKey), concat)
        } else {
            tv.visibility = GONE
        }
    }

    @BindingAdapter("textVal", "noLineconcatVal")
    @JvmStatic
    fun setconcatValWithOutNewLine(tv: TextView, textKey: String, concat: String) {
        Translator.getString(tv.context, textKey)
        tv.text = String.format(Translator.getString(tv.context, textKey), concat)

    }

    @BindingAdapter("text", "concat")
    @JvmStatic
    fun setText(view: TextView, textId: Int, concat: Array<String>) {
        view.text = Translator.getString(view.context, textId, *concat)
    }

    @BindingAdapter("text", "concat")
    @JvmStatic
    fun setText(view: TextView, textKey: String, concat: String) {
        view.text =
            Translator.getString(view.context, textKey, *StringUtils.toStringArray(concat))
    }

    @BindingAdapter("text", "concat")
    @JvmStatic
    fun setText(view: TextView, textId: Int, concat: String) {
        view.text =
            Translator.getString(view.context, textId, *StringUtils.toStringArray(concat))
    }

    @BindingAdapter("text", "start", "end")
    @JvmStatic
    fun setText(view: TextView, text: String, start: Int, end: Int) {
        val text1 = SpannableString(text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text1.setSpan(
                ForegroundColorSpan(
                    view.context.resources.getColor(
                        R.color.colorPrimaryDark,
                        null
                    )
                ), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        } else {
            text1.setSpan(
                ForegroundColorSpan(view.context.resources.getColor(R.color.colorPrimaryDark)),
                start,
                end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        view.text = text1
    }

    @BindingAdapter("hint")
    @JvmStatic
    fun setHint(view: TextView, textId: String) {
        view.hint = Translator.getString(view.context, textId)
    }

    @BindingAdapter("hint")
    @JvmStatic
    fun setHint(view: EditText, textId: String) {
        view.hint = Translator.getString(view.context, textId)
    }

    @BindingAdapter("selected")
    @JvmStatic
    fun setSelected(view: Button, selected: Boolean) {
        view.isSelected = selected
        view.isPressed = selected
    }

    @BindingAdapter("selected")
    @JvmStatic
    fun setSelected(view: TextView, selected: Boolean) {
        view.isSelected = selected
    }

    @BindingAdapter("enabled")
    @JvmStatic
    fun setEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
    }

    @BindingAdapter("entries", "layout")
    @JvmStatic
    fun <T : IBindable> setEntries(
        viewGroup: ViewGroup,
        entries: List<T>?, layoutId: Int
    ) {
        viewGroup.removeAllViews()
        if (entries != null) {
            val inflater =
                viewGroup.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            for (i in entries.indices) {
                val entry = entries[i]
                val binding =
                    DataBindingUtil.inflate<ViewDataBinding>(
                        inflater,
                        layoutId,
                        viewGroup,
                        true
                    )
                binding.setVariable(entry.bindingVariable, entry)
                binding.executePendingBindings()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("enableCoreButton")
    fun setEnable(view: CoreButton, enable: Boolean) {
        if (view != null) view.enableButton(enable)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["componentDialerError", "isScreenLocked", "isAccountLocked"],
        requireAll = true
    )
    fun setDialerInLockedState(
        view: CoreDialerPad,
        error: String?,
        isScreenLocked: Boolean = false,
        isAccountLocked: Boolean = false
    ) {
        if (null != error && error.isNotEmpty()) {
            if (!isScreenLocked && !isAccountLocked) {
                view.showError(error)
            } else {
                view.showError(error)
                view.settingUIForError(isScreenLocked)
                view.setPasscodeVisiblity(isAccountLocked)
            }

        } else {
            view.removeError()
            view.settingUIForNormal(isScreenLocked)
            view.setPasscodeVisiblity(isAccountLocked)
        }
    }

    @JvmStatic
    @BindingAdapter("componentDialerError")
    fun setDialerError(view: CoreDialerPad, error: String) {
        if (error.isNotEmpty()) {
            view.showError(error)
        } else {
            view.removeError()
        }
    }

    @JvmStatic
    @BindingAdapter("passcodeTextWatcher")
    fun te132mp(view: CoreDialerPad, watcher: TextWatcher) {
        view.etPassCodeText?.addTextChangedListener(watcher)
    }

    @JvmStatic
    @BindingAdapter(value = ["otpAttrChanged"])
    fun setOtpListener(view: OtpTextView, listener: InverseBindingListener?) {

        if (listener != null) {
            view.otpListener = object : OTPListener {
                override fun onInteractionListener() {
                    // fired when user types something in the Otpbox
                    listener.onChange()
                }

                override fun onOTPComplete(otp: String) {
                    // fired when user has entered the OTP fully.

                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("otp")
    fun setOtp(view: OtpTextView, value: String) {
        if (view.otp != value) {
            view.setOTP(value)
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "otp")
    fun getOtp(view: OtpTextView): String = view.otp!!

    @JvmStatic
    @BindingAdapter(value = ["requestKeyboard", "forceKeyboard"], requireAll = false)
    fun setRequestKeyboard(view: View, request: Boolean, forced: Boolean) {
        Utils.requestKeyboard(view, request, forced)
    }

    //mobile Component
    @BindingAdapter("isCCpActivated")
    @JvmStatic
    fun isActivated(view: LinearLayout, isActive: Boolean) {
        view.isActivated = isActive
    }

    @BindingAdapter("setBackGroundRes")
    @JvmStatic
    fun setBgRes(view: LinearLayout, drawable: Drawable?) {
        view.background = drawable
        view.setPadding(
            view.context.resources.getDimensionPixelSize(R.dimen.margin_medium),
            0,
            view.context.resources.getDimensionPixelSize(R.dimen.margin_small),
            0
        )
    }

    //    @BindingAdapter(value = ["src", "addCallback"], requireAll = false)
    @BindingAdapter("src")
    @JvmStatic
    fun setImageResId(view: ImageView, path: String) {
        Glide.with(view.context)
            .load(path).centerCrop().placeholder(R.color.greyLight)
            .into(view)
    }

    @BindingAdapter(value = ["imageSrc", "imageUri"], requireAll = true)
    @JvmStatic
    fun setImageResId(view: ImageView, path: String, imageUri: Uri) {
        if (imageUri == Uri.EMPTY) {
            Glide.with(view.context)
                .load(path).centerCrop()
                .into(view)
        } else {
            Glide.with(view.context).load(imageUri.path).centerCrop().into(view)
        }
    }

    @BindingAdapter("src", "circular")
    @JvmStatic
    fun setImageResId(view: ImageView, resId: Bitmap?, circular: Boolean) {
        resId?.let {
            if (circular) {
                Glide.with(view.context)
                    .asBitmap().load(resId).placeholder(R.color.greyLight)
                    .transforms(CenterCrop(), RoundedCorners(15))
                    .into(view)

            } else {

                Glide.with(view.context)
                    .asBitmap().load(resId).placeholder(R.color.greyLight)
                    .transforms(CenterCrop(), RoundedCorners(15))
                    .into(view)
                //set placeholder here
            }
        }
    }

    @BindingAdapter("toggleVisibility")
    @JvmStatic
    fun setImageResId(view: CardView, visibility: Boolean) {
        if (visibility) {
            view.visibility = VISIBLE
            YoYo.with(Techniques.SlideInUp)
                .duration(400)
                .playOn(view)


        } else {
            YoYo.with(Techniques.SlideOutDown)
                .duration(300)
                .playOn(view)

        }

    }

    @BindingAdapter("toggleButtonVisibility")
    @JvmStatic
    fun setImageResId(view: ImageView, visibility: Boolean) {
        if (visibility) {
            view.visibility = VISIBLE
        } else {
            view.visibility = GONE

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("textSelection")
    fun textSelection(view: EditText, selection: String?) {
        selection?.let {
            if (selection.isNotBlank()) {
                val selectedString = selection.substring(0, selection.length.coerceAtMost(100))
                view.setSelection(selectedString.length)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("drawableClick")
    fun setDrawableRightListener(view: EditText, onDrawableClick: Boolean) {
        if (onDrawableClick) {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_field, 0)

            view.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View, m: MotionEvent): Boolean {
                    var hasConsumed = false
                    if (v is EditText) {
                        if (m.x >= v.width - v.totalPaddingRight) {
                            if (m.action == MotionEvent.ACTION_UP) {
                                view.text.clear()

                                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            }
                            hasConsumed = true
                        }
                    }
                    return hasConsumed
                }
            })
        } else {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        }
    }

    @JvmStatic
    @BindingAdapter("isLayoutActivated")
    fun setisLayoutActivated(view: LinearLayout, value: Boolean) {
//        if (!value) {
        view.isActivated = value

//        }
    }

    @BindingAdapter("src", "isRound")
    @JvmStatic
    fun setProfilePicture(view: ImageView, imageSrc: String, circular: Boolean) {
        Glide.with(view.context)
            .load(Uri.parse(imageSrc))
            .transforms(CenterCrop(), RoundedCorners(115))
            .into(view)

    }

    @BindingAdapter("textColor")
    @JvmStatic
    fun setSelectedTextColor(view: TextView, isActive: Boolean) {
        if (isActive) {
            view.setTextColor(view.context.resources.getColor(R.color.colorPrimaryDark))
        } else {
            view.setTextColor(view.context.resources.getColor(R.color.greyDark))
        }
    }


    @BindingAdapter("setBeneficiaryImageSrc")
    @JvmStatic
    fun setImageSrc(imageView: ImageView, transferType: String) {

        if (transferType == SendMoneyBeneficiaryType.CASHPAYOUT.type) {
            imageView.setImageResource(R.drawable.ic_cash)
        } else {
            imageView.setImageResource(R.drawable.ic_bank)
        }
    }

    @JvmStatic
    @BindingAdapter("cardNickname")
    fun setCardNickname(view: CorePaymentCard, cardNickname: String?) {
        if (cardNickname != null) {
            view.setCardNickname(cardNickname)
        }
    }

    @JvmStatic
    @BindingAdapter("cardNumber")
    fun setCardNumber(view: CorePaymentCard, cardNumber: String?) {
        if (cardNumber != null)
            view.setCardNumber(cardNumber)
    }

    @JvmStatic
    @BindingAdapter("cardExpiry")
    fun setCardExpiry(view: CorePaymentCard, cardExpiry: String?) {
        if (!cardExpiry.isNullOrEmpty())
            view.setCardExpiry(cardExpiry)
    }

    @JvmStatic
    @BindingAdapter("cardBackgroundColor")
    fun setCardBackground(view: CorePaymentCard, cardBackgroundColor: String?) {
        if (!cardBackgroundColor.isNullOrBlank())
            view.setCardBackground(cardBackgroundColor)
    }

    @JvmStatic
    @BindingAdapter("cardType")
    fun setCardLogoByType(view: CorePaymentCard, cardType: String?) {
        if (cardType != null)
            view.setCardLogoByType(cardType)
    }

    @JvmStatic
    @BindingAdapter("editable")
    fun setEditTextEditable(editText: EditText, editable: Boolean = true) {
        editText.isFocusable = editable
        editText.isFocusableInTouchMode = editable
        editText.isClickable = editable
        editText.isCursorVisible = editable
    }

    @JvmStatic
    @BindingAdapter("app:mAdapter")
    fun setAdapter(
        view: RecyclerView,
        adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>?
    ) {
        if (null == adapter)
            return
        view.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("recycleViewAdapter")
    fun setRecycleViewAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>?
    ) {
        if (null == adapter)
            return
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("ibanMask")
    fun maskIbanNo(view: AppCompatEditText, ibanMask: String?) {
        ibanMask?.let { view.addTextChangedListener(MaskTextWatcher(view, it)) }
    }

    @JvmStatic
    @BindingAdapter(requireAll = false, value = ["textColorChangePin", "isAllEmpty"])
    fun textColorChangePin(view: TextInputLayout, pin: String?, isEmpty: Boolean) {
        when {
            isEmpty -> {
                view.defaultHintTextColor = view.context.getColorStateList(R.color.colorPrimaryDark)

            }
            pin?.isNotEmpty() ?: false -> {
                view.defaultHintTextColor =
                    view.context.getColorStateList(R.color.colorPlaceHolderGrey)
            }
            else -> {
                view.defaultHintTextColor = view.context.getColorStateList(R.color.colorPrimaryDark)
            }
        }

    }

    @JvmStatic
    @BindingAdapter("spanColor")
    fun spanColor(view: AppCompatTextView, currency: String) {
        val splitStringArray: List<String> = currency.split(" ")
        val spannable: Spannable =
            SpannableStringBuilder(splitStringArray[0] + "  " + splitStringArray[1])

        spannable.setSpan(
            ForegroundColorSpan(
                view.context.getColor(R.color.greyDark)
            ),
            0,
            splitStringArray[0].length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        view.text = spannable
    }

    @BindingAdapter(requireAll = false, value = ["placesAdaptor", "selectedListener"])
    @JvmStatic
    fun setPlacesAdapter(
        autoCompleteTextView: AutoCompleteTextView,
        placesAdapter: PlacesAutoCompleteAdapter,
        listener: OnItemClickListener?
    ) {
        autoCompleteTextView.setAdapter(placesAdapter)
        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val place: Place = parent.getItemAtPosition(position) as Place
                view?.let {
                    listener?.onItemClick(view, place, position)
                }
                autoCompleteTextView.setText(place.mainText)
                autoCompleteTextView.setSelection(place.mainText.length)
            }
        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.isFocusable = true
            autoCompleteTextView.isFocusableInTouchMode = true
            autoCompleteTextView.isFocusable = true
            autoCompleteTextView.isFocusableInTouchMode = true
        }
    }

    @BindingAdapter("android:layout_marginTop")
    @JvmStatic
    fun setLayoutMarginTop(view: View, margin: Float) {
        val lp = view.layoutParams as MarginLayoutParams
        lp.setMargins(lp.leftMargin, margin.toInt(), lp.rightMargin, lp.bottomMargin)
        view.layoutParams = lp
    }
}
