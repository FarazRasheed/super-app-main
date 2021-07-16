package co.yap.yapcore.config

import co.yap.yapcore.adjust.AdjustEvent
import co.yap.yapcore.enums.ProductFlavour

data class BuildConfigManager(
    var md5: String?, var sha1: String?, var sha256: String?,
    var leanPlumSecretKey: String?,
    var leanPlumKey: String?,
    var adjustToken: String?,
    var baseUrl: String?,
    var buildType: String?,
    var flavor: String?,
    var versionName: String?,
    var versionCode: String?,
    var applicationId: String?,
    var hasValidSignature: Boolean = true,
    var sslPin1: String?,
    var sslPin2: String?,
    var sslPin3: String?,
    var sslHost: String?
) {

    fun isReleaseBuild(): Boolean = buildType == "release"
    fun getAdjustEvent(event: AdjustEvent): String {
        return when (event) {
            AdjustEvent.KYC_END -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "8r4o52"
                    ProductFlavour.PREPROD.flavour -> "pzpv1p"
                    else -> "9um5u9"
                }
            }

            AdjustEvent.KYC_START -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "kelb07"
                    ProductFlavour.PREPROD.flavour -> "wbhghc"
                    else -> "mdcyli"
                }
            }
            AdjustEvent.SET_PIN_END -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "7vzpfo"
                    ProductFlavour.PREPROD.flavour -> "9mispm"
                    else -> "cs2msk"
                }
            }
            AdjustEvent.SET_PIN_START -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "i3m1cv"
                    ProductFlavour.PREPROD.flavour -> "pv9nj3"
                    else -> "smn577"
                }
            }
            AdjustEvent.SIGN_UP_MOBILE_NUMBER_VERIFIED -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "kx5hl6"
                    ProductFlavour.PREPROD.flavour -> "sgxm0z"
                    else -> "6zou42"
                }
            }
            AdjustEvent.SIGN_UP_END -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "skzf2k"
                    ProductFlavour.PREPROD.flavour -> "r5ix0l"
                    else -> "4c9qmq"
                }
            }
            AdjustEvent.SIGN_UP_START -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "w6rmpa"
                    ProductFlavour.PREPROD.flavour -> "p8ytou"
                    else -> "73mcc8"

                }
            }
            AdjustEvent.TOP_UP_END -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "6yum4e"
                    ProductFlavour.PREPROD.flavour -> "grwcy8"
                    else -> "jw0tz5"
                }
            }
            AdjustEvent.TOP_UP_START -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "vquxsb"
                    ProductFlavour.PREPROD.flavour -> "erscd5"
                    else -> "cadxmk"
                }
            }
            AdjustEvent.INVITER -> {
                when (flavor) {
                    ProductFlavour.PROD.flavour -> "efnby4"
                    ProductFlavour.PREPROD.flavour -> "oo9yjy"
                    else -> "sgy2ni"
                }
            }
        }
    }

    override fun toString(): String {
        return "BuildConfigManager(md5=$md5, sha1=$sha1, sha256=$sha256, leanPlumSecretKey=$leanPlumSecretKey, leanPlumKey=$leanPlumKey, adjustToken=$adjustToken, baseUrl=$baseUrl, buildType=$buildType, flavor=$flavor, versionName=$versionName, versionCode=$versionCode, applicationId=$applicationId, hasValidSignature=$hasValidSignature, sslPin1=$sslPin1, sslPin2=$sslPin2, sslPin3=$sslPin3, sslHost=$sslHost)"
    }

}