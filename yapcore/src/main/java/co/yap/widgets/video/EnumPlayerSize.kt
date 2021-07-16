package co.yap.widgets.video

enum class EnumPlayerSize(var valueStr: String, val value: Int) {
    UNDEFINE("UNDEFINE", -1), EXACTLY("EXACTLY", 0), AT_MOST(
        "AT_MOST",
        1
    ),
    UNSPECIFIED("UNSPECIFIED", 2);

    companion object {
        operator fun get(value: String?): EnumPlayerSize {
            if (value == null) {
                return UNDEFINE
            }
            val arrgs = values()
            for (v in arrgs) {
                if (v.valueStr.equals(value.trim { it <= ' ' }, ignoreCase = true)) {
                    return v
                }
            }
            return UNDEFINE
        }

        operator fun get(value: Int?): EnumPlayerSize {
            if (value == null) {
                return UNDEFINE
            }
            val arrgs = values()
            for (v in arrgs) {
                if (v.value == value) {
                    return v
                }
            }
            return UNDEFINE
        }
    }

}