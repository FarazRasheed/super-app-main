package co.yap.modules.location.tax

import android.os.Parcelable
import androidx.databinding.ObservableField
import co.yap.countryutils.country.Country
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaxModel(
    var countries: ArrayList<Country> = arrayListOf(),
    var reasons: ArrayList<String> = arrayListOf(),
    var selectedReason: String = reasons.first(),

    var options: ArrayList<String> = arrayListOf("No", "Yes"),
    var selectedOption: ObservableField<String> = ObservableField("No"),

    var tinNumber: ObservableField<String> = ObservableField(""),
    val taxRowTitle: ObservableField<String> = ObservableField("Select country of tax residence"),

    var taxRowNumber: ObservableField<Boolean>,
    var canAddMore: ObservableField<Boolean>,
    var selectedCountry: Country? = null
) : Parcelable