package co.yap.sendmoney.fundtransfer.states

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.sendmoney.fundtransfer.interfaces.IInternationalFundsTransfer
import co.yap.sendmoney.BR
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.cancelAllSnackBar
import co.yap.yapcore.helpers.extentions.getValueWithoutComa

class InternationalFundsTransferState(val application: Application) : BaseState(),
    IInternationalFundsTransfer.State {

    val context = application.applicationContext
    override var sourceCurrency: ObservableField<String> = ObservableField("")
    override var destinationCurrency: ObservableField<String> = ObservableField("")
    override var transactionNote: ObservableField<String> = ObservableField("")

    @get:Bindable
    override var errorDescription: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.errorDescription)
        }
    @get:Bindable
    override var availableBalanceString: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.availableBalanceString)
        }

    @get:Bindable
    override var transferFeeSpannable: CharSequence? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferFeeSpannable)
        }
    @get:Bindable
    override var etInputAmount: String? = ""
        set(value) {
            field = value.getValueWithoutComa()
            notifyPropertyChanged(BR.etInputAmount)
        }

    @get:Bindable
    override var etOutputAmount: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.etOutputAmount)
        }

    @get:Bindable
    override var fromFxRate: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.fromFxRate)
        }

    @get:Bindable
    override var toFxRate: String? = "0.0"
        set(value) {
            field = value
            notifyPropertyChanged(BR.toFxRate)
        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var maxLimit: Double? = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.maxLimit)
        }
    @get:Bindable
    override var minLimit: Double? = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.minLimit)
        }

    override fun clearError() {
        if (etInputAmount != "") {
            if (etInputAmount != "." && etInputAmount?.toDouble() ?: 0.0 > 0.0) {
                valid = true
            }
        } else if (etInputAmount == "") {
            valid = false
            cancelAllSnackBar()
        }
    }

}
