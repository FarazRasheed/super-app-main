package com.yap.yapandroid

import android.util.Base64
import co.yap.yapcore.config.BuildConfigManager
import co.yap.yapcore.enums.ProductFlavour

object ConfigurationsUAE {
    var configManager: BuildConfigManager? = null

    fun configure(
        flavour: String,
        buildType: String,
        versionName: String,
        versionCode: String,
        applicationId: String
    ) {
        val productFlavour = when (flavour) {
            ProductFlavour.DEV.flavour -> {
                ProductFlavour.DEV
            }
            ProductFlavour.QA.flavour -> {
                ProductFlavour.QA
            }
            ProductFlavour.STG.flavour -> {
                ProductFlavour.STG
            }
            ProductFlavour.PREPROD.flavour -> {
                ProductFlavour.PREPROD
            }
            ProductFlavour.PROD.flavour -> {
                ProductFlavour.PROD
            }
            else -> ProductFlavour.INTERNAL
        }


        configManager = BuildConfigManager(
            md5 = md5(productFlavour).decode(),
            sha1 = sha1(productFlavour).decode(),
            sha256 = sha256(productFlavour).decode(),
            leanPlumSecretKey = leanPlumKey(productFlavour, buildType).first,
            leanPlumKey = leanPlumKey(productFlavour, buildType).second,
            adjustToken = adjustToken(productFlavour),
            baseUrl = baseUrl(productFlavour),
            buildType = buildType,
            flavor = flavour,
            versionName = versionName,
            versionCode = versionCode,
            applicationId = applicationId,
            sslPin1 = sslPin1(productFlavour),
            sslPin2 = sslPin2(productFlavour),
            sslPin3 = sslPin3(productFlavour),
            sslHost = sslHost(productFlavour)
        )
    }

    private fun sha1(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "ODU6OUY6NjM6N0M6NjI6N0I6Qjc6N0E6MDg6RTQ6OEI6MDY6OUU6M0U6MkQ6RTU6MEQ6OEM6Mjg6MjU="
            ProductFlavour.PREPROD -> "ODU6OUY6NjM6N0M6NjI6N0I6Qjc6N0E6MDg6RTQ6OEI6MDY6OUU6M0U6MkQ6RTU6MEQ6OEM6Mjg6MjU="
            ProductFlavour.STG -> "REI6QTg6REE6OTg6RUY6ODA6QkY6ODQ6MDQ6RDE6NzM6Rjg6QzE6RjE6QzA6MTU6NTk6MjA6MTY6RDI="
            ProductFlavour.QA -> ""
            ProductFlavour.DEV -> ""
            ProductFlavour.INTERNAL -> ""
        }
    }

    private fun md5(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "MDg6NzM6ODQ6RTI6NEM6NTc6RTU6MUU6OEY6ODU6RTM6OTg6MUM6NDM6Qjg6NEE="
            ProductFlavour.PREPROD -> "MDg6NzM6ODQ6RTI6NEM6NTc6RTU6MUU6OEY6ODU6RTM6OTg6MUM6NDM6Qjg6NEE="
            ProductFlavour.STG -> "MjU6ODQ6MUY6RTE6RjE6QTg6QzI6NTg6N0I6QUU6RUE6QjM6NDE6NjU6NzY6RkU="
            ProductFlavour.QA -> ""
            ProductFlavour.DEV -> ""
            ProductFlavour.INTERNAL -> ""
        }
    }

    private fun sha256(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "ODY6QTE6MzQ6NEU6RkM6OTQ6M0I6NzA6Mjk6MjE6OUU6M0I6NzA6MzM6NDI6RUM6M0M6NjI6M0E6MkI6MEU6N0M6QkM6MDc6RTU6N0Q6M0M6Mjk6RTg6MkE6Q0Y6NTM="
            ProductFlavour.PREPROD -> "ODY6QTE6MzQ6NEU6RkM6OTQ6M0I6NzA6Mjk6MjE6OUU6M0I6NzA6MzM6NDI6RUM6M0M6NjI6M0E6MkI6MEU6N0M6QkM6MDc6RTU6N0Q6M0M6Mjk6RTg6MkE6Q0Y6NTM="
            ProductFlavour.STG -> "QTQ6QUM6MTQ6RjM6REQ6RDg6NTc6RTk6RkM6QUM6N0M6MDk6NkM6QTQ6MEQ6RUM6QjU6MEU6RTE6OTY6QTI6RjA6Qjc6Q0M6QjA6MEY6MDc6MDA6Qzc6N0M6RjM6Qjg="
            ProductFlavour.QA -> ""
            ProductFlavour.DEV -> ""
            ProductFlavour.INTERNAL -> ""
        }
    }

    private fun baseUrl(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "https://ae-prod.yap.com/"
            ProductFlavour.PREPROD -> "https://ae-preprod.yap.com/"
            ProductFlavour.STG -> "https://stg.yap.co/"
            ProductFlavour.QA -> "https://qa.yap.co/"
            ProductFlavour.DEV -> "https://dev-b.yap.co/"
            ProductFlavour.INTERNAL -> "https://stg.yap.co/"
        }
    }

    private fun adjustToken(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "xty7lf6skgsg"
            ProductFlavour.PREPROD -> "uv1oiis7wni8"
            ProductFlavour.STG -> "am0wjeshw5xc"
            ProductFlavour.QA -> "am0wjeshw5xc"
            ProductFlavour.DEV -> "am0wjeshw5xc"
            ProductFlavour.INTERNAL -> "am0wjeshw5xc"
        }
    }

    private fun sslPin1(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "sha256/SK10shgwb9jAeBvxJXrkBmjL2joCFoSq2Sp1tGyOcQk="
            ProductFlavour.PREPROD -> "sha256/SK10shgwb9jAeBvxJXrkBmjL2joCFoSq2Sp1tGyOcQk="
            ProductFlavour.STG -> "sha256/ZrRL6wSXl/4lm1KItkcZyh56BGOoxMWUDJr7YVqE4no="
            ProductFlavour.QA -> "sha256/e5L5CAoQjV0HFzAnunk1mPHVx1HvPxcfJYI0UtLyBwY="
            ProductFlavour.DEV -> "sha256/e5L5CAoQjV0HFzAnunk1mPHVx1HvPxcfJYI0UtLyBwY="
            ProductFlavour.INTERNAL -> "sha256/e5L5CAoQjV0HFzAnunk1mPHVx1HvPxcfJYI0UtLyBwY="
        }
    }

    private fun sslPin2(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8="
            ProductFlavour.PREPROD -> "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8="
            ProductFlavour.STG -> "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8="
            ProductFlavour.QA -> "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA="
            ProductFlavour.DEV -> "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA="
            ProductFlavour.INTERNAL -> "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA="
        }
    }

    private fun sslPin3(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA="
            ProductFlavour.PREPROD -> "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA="
            ProductFlavour.STG -> "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA="
            ProductFlavour.QA -> "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI="
            ProductFlavour.DEV -> "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI="
            ProductFlavour.INTERNAL -> "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI="
        }
    }

    private fun sslHost(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "*.yap.com"
            ProductFlavour.PREPROD -> "*.yap.com"
            ProductFlavour.STG -> "*.yap.co"
            ProductFlavour.QA -> "*.yap.co"
            ProductFlavour.DEV -> "*.yap.co"
            ProductFlavour.INTERNAL -> "*.yap.co"
        }
    }

    private fun leanPlumKey(
        productFlavour: ProductFlavour,
        buildType: String
    ): Pair<String, String> {

        return if (productFlavour == ProductFlavour.DEV && buildType == "debug") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ"
            )
        } else if (productFlavour == ProductFlavour.DEV && buildType == "release") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0"
            )
        } else if (productFlavour == ProductFlavour.QA && buildType == "debug") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ"
            )
        } else if (productFlavour == ProductFlavour.QA && buildType == "release") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0"
            )
        } else if (productFlavour == ProductFlavour.STG && buildType == "debug") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ"
            )
        } else if (productFlavour == ProductFlavour.STG && buildType == "release") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0"
            )
        } else if (productFlavour == ProductFlavour.PREPROD && buildType == "debug") {
            Pair(
                "app_jvEgXTi9zZUpoFck8XVxVY4zBgAEYZrPVTliIuaO0IQ",
                "dev_HnmEVN0GDZbhInJjmX767e7InveRC23LkSokuLLuA3s"
            )
        } else if (productFlavour == ProductFlavour.PREPROD && buildType == "release") {
            Pair(
                "app_jvEgXTi9zZUpoFck8XVxVY4zBgAEYZrPVTliIuaO0IQ",
                "prod_EjIC6dCuGaGr36p2qRvG3GkRIhuYf9vgBEGjQ3jBqLM"
            )
        } else if (productFlavour == ProductFlavour.PROD && buildType == "debug") {
            Pair(
                "app_DtOp3ipxDUi9AM7Bg3jv351hZ4DVrLgC9JZX4L46lIc",
                "dev_RAFVBmDKypdOr3kbd326JUoqGLr8iSvt2Lei4BK48qk"
            )
        } else if (productFlavour == ProductFlavour.PROD && buildType == "release") {
            Pair(
                "app_DtOp3ipxDUi9AM7Bg3jv351hZ4DVrLgC9JZX4L46lIc",
                "prod_MfjUF6Sh3GuNE2RtQMkXZTeCUSTS3K0v2CLeGCp0gzk"
            )
        } else {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "prod_MfjUF6Sh3GuNE2RtQMkXZTeCUSTS3K0v2CLeGCp0gzk"
            )
        }
    }

    private fun String.decode(): String {
        return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
    }

}