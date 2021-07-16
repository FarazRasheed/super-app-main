package co.yap.sendmoney.base


import co.yap.yapcore.enums.FeeType
import org.junit.Assert.assertEquals
import org.junit.Test


class SMFeeViewModelTest {
    @Test
    fun getFeeIfEnterAmountIsEmpty() {
        assertEquals("0.00", updateFees("",""))
    }
    @Test
    fun getFeeIfEnterAmountIsZero() {
        assertEquals("0.00", updateFees("",FeeType.FLAT.name))
    }
    @Test
    fun getFeeIfEnterAmountIsNonZero() {
//        assertEquals("0.00", updateFees(""))
    }

    private fun updateFees(enterAmount: String,feeTye:String): String {
        return when(feeTye){
            FeeType.FLAT.name-> ""
            FeeType.TIER.name-> ""
            else -> "0.00"
        }
    }
}