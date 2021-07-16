package co.yap.widgets.video

enum class EnumResizeMode(var valueStr: String, val value: Int) {
    UNDEFINE("UNDEFINE", -1), FIT("Fit", 1), FILL("Fill", 2), ZOOM("Zoom", 3);

    companion object {
        operator fun get(value: String?): EnumResizeMode {
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

         fun get(value: Int?): EnumResizeMode {
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