package co.yap.yapcore.helpers

import org.json.JSONArray
import java.util.regex.Pattern


object StringUtils {

    fun isValidAddress(s: String): Boolean {
        var i = 0
        while (i < s.length) {
            val c = s.codePointAt(i)
            if (c in 0x0600..0x06E0) return false
            i += Character.charCount(c)
        }
        return true
    }

    fun validateName(name: String): Boolean {

        var inputStr: CharSequence = ""
        var isValid = false
        val expression =
            "^[a-zA-Z]{1}[a-zA-Z ]{1,100}\$"
        inputStr = name
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(inputStr)

        if (matcher.matches() && !name.isNullOrEmpty()) {
            if (name.length >= 2) {
                isValid = true
            }
        }
        return isValid
    }

    fun validateLastName(name: String): Boolean {

        var inputStr: CharSequence = ""
        var isValid = false
        val expression =
            "^[a-zA-Z]{1,100}\$"
        inputStr = name
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(inputStr)

        if (matcher.matches() && !name.isNullOrEmpty()) {
            if (name.length >= 1) {
                isValid = true
            }
        }
        return isValid
    }

    fun validateRegix(value: String, expression: String, lengthCheck: Int = 2): Boolean {

        var isValid = false
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(value)

        if (matcher.matches() && !value.isEmpty()) {
            if (value.length >= lengthCheck) {
                isValid = true
            }
        }
        return isValid
    }


    /**
     * Function takes a string value like "["hello", "world", "1"]"
     * and evaluates it to return an Array<String?>
     */

    fun eval(text: String?): Array<String?> {
        val jsonArray = JSONArray(text)
        val strArr = arrayOfNulls<String>(jsonArray.length())

        for (i in 0 until jsonArray.length()) {
            strArr[i] = jsonArray.getString(i)
        }
        return strArr
    }

    /**
     * Function takes a string value like "["hello", "world", "1"]"
     * and evaluates it to return an Array<String?>
     */

    fun toStringArray(text: String?): Array<String> = eval(text).requireNoNulls()

    /**
     * Checks if a string contains numbers in increasing or decreasing sequence
     */
    fun isSequenced(text: String): Boolean {
        val sequenced = text.run {
            val first = if (length > 0) get(0).toString().toIntOrNull() else null
            first?.let {
                val low = first - (length - 1)
                val high = first + (length - 1)

                val lowSeq = (low..first).asReversedString()
                val highSeq = (first..high).asString()

                lowSeq == this || highSeq == this
            }
        }
        return sequenced ?: false
    }


    private fun IntRange.asString(): String = run {
        val s = StringBuilder()
        forEach { s.append(it.toString()) }
        s.toString()
    }

    private fun IntRange.asReversedString(): String = run {
        asString().reversed()
    }

    /**
     * Checks if a string contains all same chars like "0000"
     */
    fun hasAllSameChars(text: String): Boolean = text.run {
        val first = if (length > 0) get(0).toString() else ""
        replace(first, "").isEmpty()
    }

    fun getInitials(fullName: String): String {
        return if (fullName.isNotBlank()) {
            fullName.split(' ')
                .mapNotNull { it.firstOrNull()?.toString() }
                .reduce { acc, s -> acc + s }.toUpperCase()
        } else ""

    }

    fun getFirstname(fullName: String): String {
        return if (fullName.isNotBlank()) {
            fullName.split(' ')[0]
        } else ""

    }

    fun isValidIBAN(iban: String, code: String?): Boolean {
        var inputStr: CharSequence = ""
        var isValid = false
        val expression = "^($code)[0-9]{2}[0-9A-Z]{1,35}$"
        inputStr = iban
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(inputStr)

        if (matcher.matches() && iban.isNotEmpty()) {
            isValid = true
        }
        return isValid
    }

    fun isValidAccountNumber(accountNo: String): Boolean {
        var inputStr: CharSequence = ""
        var isValid = false
        val expression =
            "^[0-9]{4,34}$"
        inputStr = accountNo
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(inputStr)

        if (matcher.matches() && accountNo.isNotEmpty()) {
            isValid = true
        }
        return isValid
    }

    fun isValidSwift(swift: String): Boolean {
        var inputStr: CharSequence = ""
        var isValid = false
        val expression =
            "^[0-9A-Z]{4,11}$"
        inputStr = swift
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(inputStr)

        if (matcher.matches() && swift.isNotEmpty()) {
            isValid = true
        }
        return isValid
    }
}

