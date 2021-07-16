package co.yap.sendmoney

import org.junit.Assert
import org.junit.Test

class FundTransferTest {

    @Test
    fun getConfigurableLength() {
        val aa = 4
        var empty = ""
        for (i in 1..aa) {
            empty = "0${empty}"
        }
        Assert.assertEquals(
            true,
            empty == "0000"
        )
    }

    @Test
    fun getDecimalFromValue() {
        Assert.assertEquals(
            2,
            getDecimalFromValue("100.0")
        )
        Assert.assertEquals(
            3,
            getDecimalFromValue("100.000")
        )
        Assert.assertEquals(
            5,
            getDecimalFromValue("100.66655")
        )
        Assert.assertEquals(
            2,
            getDecimalFromValue("100,000.00")
        )
        Assert.assertEquals(
            2,
            getDecimalFromValue(".0")
        )
        Assert.assertEquals(
            3,
            getDecimalFromValue(".000")
        )
        Assert.assertEquals(
            2,
            getDecimalFromValue("0000")
        )
        Assert.assertEquals(
            2,
            getDecimalFromValue("1432,25")
        )
    }

    fun getDecimalFromValue(amount: String): Int {
        val splitAmount = amount.split(".")
        return if (splitAmount.size > 1) {
            if (splitAmount[1].length < 2) {
                2
            } else {
                splitAmount[1].length
            }
        } else {
            2
        }
    }

    @Test
    fun getDecimalDigits() {
        Assert.assertEquals(
            "000000",
            getDecimalDigits(6)
        )
    }
    fun getDecimalDigits(digits: Int): String {
        val sb = StringBuilder()
        for (i in 1..digits) {
            sb.append("0")
        }
        return sb.toString()
    }
//
//    @Test
//    fun isAmountDecimalConfigured() {
//
//        val configuredDecimal = 2
//        val amount = "1000.1232"
//        Assert.assertEquals(
//            true,
//            amount.toFormattedCurrency2(configuredDecimal) == "1,000.1232"
//        )
//    }
//
//
//    @Test
//    fun isValidCPErrorMessage() {
//        val cp = SMCoolingPeriod()
//        cp.maxAllowedCoolingPeriodAmount = "10000.00"
//        cp.consumedAmount = 10000.00
//
//        Assert.assertEquals(
//            true,
//            cp.consumedAmount ?: 0.0 >= cp.maxAllowedCoolingPeriodAmount.parseToDouble()
//        )
//    }

//    @Test
//    fun isCoolingPeriodAmountConsumed() {
////        val cp = SMCoolingPeriod()
//        cp.maxAllowedCoolingPeriodAmount = "10000.00"
//        cp.consumedAmount = 0.00
//        val inputAmount = 10001.00
//        val remainingLimit = cp.maxAllowedCoolingPeriodAmount.parseToDouble()
//            .minus(cp.consumedAmount ?: 0.0)
//
//        Assert.assertEquals(
//            true,
//            inputAmount > remainingLimit
//        )
//    }
}