package co.yap.yapcore.helpers

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitsInputFilter(allowedDecimals: Int) : InputFilter {
    private var moPattern: Pattern =
        if (allowedDecimals == 0) Pattern.compile("[0-9]*||(\\.)?") else
            Pattern.compile("[0-9]*+((\\.[0-9]{0,${allowedDecimals}})?)||(\\.)?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        var lsStart = ""
        var lsInsert = ""
        var lsEnd = ""
        var lsText = ""
        lsText = dest.toString()
        if (lsText.isNotEmpty()) {

            lsStart = lsText.substring(0, dstart)
            if (source !== "") {
                lsInsert = source.toString()
            }
            lsEnd = lsText.substring(dend)
            lsText = lsStart + lsInsert + lsEnd
        }

        val loMatcher = moPattern.matcher(lsText)
        return if (!loMatcher.matches()) {
            ""
        } else null

    }

}
