package co.yap.modules.dashboard.more.profile.viewmodels

import android.app.Application
import co.yap.R
import co.yap.modules.dashboard.more.profile.intefaces.ISuccess
import co.yap.modules.dashboard.more.profile.states.SuccessState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*

class SuccessViewModel(application: Application) :
    BaseViewModel<ISuccess.State>(application), ISuccess.ViewModel {
    override val buttonClickEvent: SingleClickEvent = SingleClickEvent()
    override val state: SuccessState = SuccessState()

    override fun handlePressOnDoneButton() {
        buttonClickEvent.call()
    }

    override fun placesApiCall(photoPlacedId: String, success: () -> Unit) {
        Places.initialize(
            context,
            getString(R.string.google_maps_key)
        )
        val placesClient: PlacesClient = Places.createClient(context)
        val placeId = photoPlacedId
        val fields = listOf(Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)
        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
                val metada = place.photoMetadatas
                if (metada == null || metada.isEmpty()) {
                    return@addOnSuccessListener
                }
                val photoMetadata = metada.first()
                val attributions = photoMetadata?.attributions
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(R.dimen._480sdp) // Optional.
                    .setMaxHeight(R.dimen._280sdp) // Optional.
                    .build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                        val bitmap = fetchPhotoResponse.bitmap
                        state.placeBitmap = bitmap
                        success.invoke()
                    }.addOnFailureListener {}
            }
    }
}