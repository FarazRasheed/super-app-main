package co.yap.yapcore.helpers.extentions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import java.util.*


fun isValidLatLng(lat: Double, lng: Double): Boolean {
    if (lat < -90 || lat > 90) {
        return false
    } else if (lng < -180 || lng > 180) {
        return false
    } else if (lat == 0.0 && lng == 0.0) {
        return false
    }
    return true
}

fun Activity.openGoogleMapDirection(start: LatLng, destination: LatLng) {
    val uri: String = String.format(
        Locale.ENGLISH,
        "http://maps.google.com/maps?daddr=%f,%f",
        destination.latitude,
        destination.longitude
    )
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(uri)
    )
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        toast("Google Map App not installed. Please install it")
    }

}

fun Context.openGoogleMapDirection(start: LatLng, destination: LatLng) {
    val uri: String = String.format(
        Locale.ENGLISH,
        "http://maps.google.com/maps?daddr=%f,%f",
        destination.latitude,
        destination.longitude
    )
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(uri)
    )
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        toast("Google Map App not installed. Please install it")
    }

}

fun Fragment.openGoogleMapDirection(destination: LatLng) {
    val uri: String = String.format(
        Locale.ENGLISH,
        "http://maps.google.com/maps?daddr=%f,%f",
        destination.latitude,
        destination.longitude
    )
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(uri)
    )
    if (intent.resolveActivity(requireContext().packageManager) != null) {
        startActivity(intent)
    } else {
        toast("Google Map App not installed. Please install it")
    }

}

fun Fragment.openGoogleMapNavigation(start: LatLng, destination: LatLng) {
    val uri: String = String.format(
        Locale.ENGLISH,
        "google.navigation:q=%f,%f",
        destination.latitude,
        destination.longitude
    )
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(uri)
    )
    if (intent.resolveActivity(requireContext().packageManager) != null) {
        startActivity(intent)
    } else {
        toast("Google Map App not installed. Please install it")
    }

}

fun Activity.openGoogleMapNavigation(start: LatLng, destination: LatLng) {
    val uri: String = String.format(
        Locale.ENGLISH,
        "google.navigation:q=%f,%f",
        destination.latitude,
        destination.longitude
    )
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(uri)
    )
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        toast("Google Map App not installed. Please install it")
    }

}

fun Context.openGoogleMapNavigation(start: LatLng, destination: LatLng) {
    val uri: String = String.format(
        Locale.ENGLISH,
        "google.navigation:q=%f,%f",
        destination.latitude,
        destination.longitude
    )
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(uri)
    )
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        toast("Google Map App not installed. Please install it")
    }

}