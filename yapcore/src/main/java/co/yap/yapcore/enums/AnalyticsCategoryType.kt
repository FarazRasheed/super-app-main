package co.yap.yapcore.enums

import androidx.annotation.Keep

@Keep
enum class AnalyticsCategoryType(val title: String) {
    TRAVEL("Travel"),
    UTILITIES("Utilities"),
    SHOPPING("Shopping"),
    GROCERIES("Groceries"),
    MEDIA_AND_ENTERTAINMENT("Media and entertainment"),
    FOOD_AND_DRINK("Food and drink"),
    HEALTH_AND_BEAUTY("Health And beauty"),
    SERVICES("Services"),
    TRANSAPORT("Transport"),
    INSURANCE("Insurance"),
    EDUCATION("Education"),
    AIRPORT_LOUNGE("Airport lounge"),
    OTHERS("Others")
}