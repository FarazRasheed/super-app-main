package co.yap.widgets.swipe_lib

import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary


interface SwipeCallBack {
    fun onSwipeEdit(beneficiary: Beneficiary)
    fun onSwipeDelete(beneficiary: Beneficiary, position: Int)

}