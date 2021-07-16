package co.yap.modules.dashboard.cards.paymentcarddetail.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
@Deprecated("this is not usefull currently")
data class CardInfoModel(@get:Bindable var cardName: String, @get:Bindable var cardNumber: String) :BaseObservable(){
}