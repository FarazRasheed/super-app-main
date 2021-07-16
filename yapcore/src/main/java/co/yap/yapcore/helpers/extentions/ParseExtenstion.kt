package co.yap.yapcore.helpers.extentions

fun String?.parseToDouble() = try {
    this?.getValueWithoutComa()?.toDouble()?:0.0
} catch (e: NumberFormatException) {
    0.0
}

fun String.parseToInt() = try {
    toInt()
} catch (e: NumberFormatException) {
    0
}
fun String.parseToLong() = try {
    toLong()
} catch (e: NumberFormatException) {
    0L
}

fun Double.parseToInt() = try {
    toInt()
} catch (e: NumberFormatException) {
    0
}

fun String.parseToFloat() = try {
    toFloat()
} catch (e: NumberFormatException) {
    0.0F
}
