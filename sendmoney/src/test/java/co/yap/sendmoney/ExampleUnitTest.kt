package co.yap.sendmoney

import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.roundVal
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4.1, 2 + 2)
    }

    @Test
    fun isValidRoundUp() {
        val rate: Double = 0.033795
        val amount: Double = 3000.0
        val mul: Double = rate * amount
        assertEquals("101.39", mul.roundVal().toString())
    }

    @Test
    fun abc() {
        //'/^784-[0-9]{4}-[0-9]{7}-[0-9]{1}$/';
        assertEquals("123-4567-8901234-5", getFormattedCitizenNumber("123456789012345"))
        assertEquals("123-4567-8901234-5", getFormattedCitizenNumber("123456789012345"))
    }

    @Test
    fun isValidEID() {
        val citizenNo = "684-4567-8901234-5"
        val valid = Utils.isValidEID(citizenNo)
        assertEquals(true, valid)
    }

    private fun getFormattedCitizenNumber(citizenNo: String?): String {
        return citizenNo?.let {

            val valid = Utils.isValidEID(citizenNo)
            val builder = StringBuilder()
            if (hasValidPart(it, 0, 2)) {
                builder.append(it.subSequence(0..2))
                builder.append("-")
            }
            if (hasValidPart(it, 3, 6)) {
                builder.append(it.subSequence(3..6))
                builder.append("-")
            }
            if (hasValidPart(it, 7, 13)) {
                builder.append(it.subSequence(7..13))
                builder.append("-")
            }
            if (hasValidPart(it, 14, 14))
                builder.append(it.subSequence(14..14))
            return@let builder.toString()
        } ?: ""
    }

    private fun hasValidPart(value: String?, start: Int, end: Int): Boolean {
        return value?.let {
            return (end in start..it.length)
        } ?: false
    }

    @Test
    fun mathTest() {
        val mutlipiler: Double = 4.8
        val citizenNo: Double = 9999999.0
        val valid = citizenNo * mutlipiler
        val value = valid.toString()
        assertEquals(value, valid)
    }

}
