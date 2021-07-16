package co.yap.sendmoney.fundtransfer.states

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import co.yap.networking.transactions.responsedtos.InternationalFundsTransferReasonList
import co.yap.sendmoney.fundtransfer.interfaces.ICashTransfer
import co.yap.sendmoney.BR
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.extentions.getValueWithoutComa

class CashTransferState(application: Application) : BaseState(), ICashTransfer.State {

    val context: Context = application.applicationContext

    @get:Bindable
    override var amountBackground: Drawable? =
        context.resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds, null)
        set(value) {
            field = value
            notifyPropertyChanged(BR.amountBackground)
        }


    @get:Bindable
    override var availableBalanceString: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.availableBalanceString)
        }

    @get:Bindable
    override var amount: String = ""
        set(value) {
            field = value.getValueWithoutComa()
            notifyPropertyChanged(BR.amount)
        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var minLimit: Double = 1.00
        set(value) {
            field = value
            notifyPropertyChanged(BR.minLimit)
        }


    @get:Bindable
    override var errorDescription: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.errorDescription)
        }

    @get:Bindable
    override var noteValue: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.noteValue)
        }

    @get:Bindable
    override var maxLimit: Double = 999999.00
        set(value) {
            field = value
            notifyPropertyChanged(BR.maxLimit)
        }

    @get:Bindable
    override var produceCode: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.produceCode)
        }

    @get:Bindable
    override var feeAmountSpannableString: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.feeAmountSpannableString)
        }

    @get:Bindable
    override var transactionData: ArrayList<InternationalFundsTransferReasonList.ReasonList> =
        ArrayList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.transactionData)
        }

    @get:Bindable
    override val populateSpinnerData: MutableLiveData<List<InternationalFundsTransferReasonList.ReasonList>> =
        MutableLiveData()

    override fun clearError() {
        if (amount != "") {
            if (amount != "." && amount.toDoubleOrNull() ?: 0.0 > 0.0) {
                valid = true
                amountBackground =
                    context.resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds, null)
            }
        } else if (amount == "") {
            valid = false
        }
    }
}