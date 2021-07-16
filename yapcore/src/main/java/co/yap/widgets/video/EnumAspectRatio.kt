package co.yap.widgets.video

enum class EnumAspectRatio(var valueStr: String, val value: Int) {
    UNDEFINE("UNDEFINE", 0),
    ASPECT_1_1("ASPECT_1_1", 1),
    ASPECT_16_9("ASPECT_16_9", 2),
    ASPECT_4_3(
        "ASPECT_4_3",
        3
    ),
    ASPECT_MATCH("ASPECT_MATCH", 4), ASPECT_MP3("ASPECT_MP3", 5);

    companion object {
        fun get(value: String?): EnumAspectRatio {
            if (value == null) {
                return UNDEFINE
            }
            val arrgs =
                values()
            for (v in arrgs) {
                if (v.valueStr.equals(value.trim { it <= ' ' }, ignoreCase = true)) {
                    return v
                }
            }
            return UNDEFINE
        }

        fun get(value: Int?): EnumAspectRatio {
            if (value == null) {
                return UNDEFINE
            }
            val arrgs =
                values()
            for (v in arrgs) {
                if (v.value == value) {
                    return v
                }
            }
            return UNDEFINE
        }
    }

}