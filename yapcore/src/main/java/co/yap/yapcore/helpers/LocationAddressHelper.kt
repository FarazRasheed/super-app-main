package co.yap.yapcore.helpers


import android.location.Geocoder
import android.os.Bundle
import co.yap.yapcore.constants.Constants
import java.io.IOException

object LocationAddressHelper {

    private val TAG = LocationAddressHelper::class.java.simpleName

    fun getAddressFromLocation(geocoder: Geocoder, latitude: Double, longitude: Double): Bundle {

        var errorMessage: String? = null
        var status = 0
        //val geocoder = Geocoder(context, Locale.getDefault())
        var result: String? = null
        val bundle = Bundle()
        bundle.putDouble(Constants.LONGITUDE, latitude)
        bundle.putDouble(Constants.LONGITUDE, longitude)
        try {
            val addressList = geocoder.getFromLocation(
                latitude, longitude, 1
            )
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    val line = address.getAddressLine(i)
                    if (line != null) {
                        sb.append(address.getAddressLine(i)).append(" ")
                    }
                }

                if (address.subLocality != null)
                    sb.append(address.subLocality).append(" ")
                if (address.locality != null)
                    sb.append(address.locality).append(" ")
                if (address.adminArea != null)
                    sb.append(address.adminArea).append(" ")
                if (address.countryName != null)
                    sb.append(address.countryName).append(" ")

                if (address.postalCode != null)
                    sb.append(address.postalCode).append(" ")

                if (address.featureName != null)
                    sb.append(address.featureName).append(" ")


                result = sb.toString()
                status = 1
            } else {
                errorMessage = ""//Application.string(R.string.no_address_found);
                status = 0
            }
        } catch (e: IOException) {
            errorMessage = ""//Application.string(R.string.service_not_available);
            status = 0
        } catch (e2: IllegalArgumentException) {
            errorMessage = ""//Application.string(R.string.invalid_lat_long_used);
            status = 0
        } finally {
            bundle.putString(Constants.LOCATION_ADDRESS, result)
            bundle.putString(Constants.ERROR_MESSAGE, errorMessage)
            bundle.putInt(Constants.SUCCESS_RESULT, status)
            bundle.putDouble(Constants.LATITUDE, latitude)
            bundle.putDouble(Constants.LONGITUDE, longitude)
            return bundle
        }

    }

}
