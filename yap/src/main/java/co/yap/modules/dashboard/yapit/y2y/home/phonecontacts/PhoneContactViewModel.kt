package co.yap.modules.dashboard.yapit.y2y.home.phonecontacts

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.yapit.y2y.main.viewmodels.Y2YBaseViewModel
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.Contact
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.PagingState
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.managers.SessionManager
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlin.math.ceil

class PhoneContactViewModel(application: Application) :
    Y2YBaseViewModel<IPhoneContact.State>(application),
    IPhoneContact.ViewModel, IRepositoryHolder<CustomersRepository> {

    override val repository: CustomersRepository = CustomersRepository
    override val state: IPhoneContact.State = PhoneContactState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    var pagingState: MutableLiveData<PagingState> = MutableLiveData()
    override var phoneContactLiveData: MutableLiveData<List<Contact>> = MutableLiveData()

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun getState(): LiveData<PagingState> {
        return pagingState
    }

    override fun listIsEmpty(): Boolean {
        return phoneContactLiveData.value?.isEmpty() ?: true
    }

    private suspend fun getLocalContacts() = viewModelBGScope.async(Dispatchers.IO) {
        fetchContacts(context)
    }.await()

    override fun onCleared() {
        viewModelBGScope.close()
        super.onCleared()
    }

    override fun getY2YBeneficiaries() {

        pagingState.value = PagingState.LOADING
        if (listIsEmpty()) {
            launch {
                val localContacts = getLocalContacts()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    localContacts.removeIf { it.mobileNo == SessionManager.user?.currentCustomer?.mobileNo }
                } else {
                    localContacts.remove(localContacts.find { it.mobileNo == SessionManager.user?.currentCustomer?.mobileNo })
                }

                if (localContacts.isEmpty()) {
                    phoneContactLiveData.value = mutableListOf()
                    pagingState.value = PagingState.DONE
                } else {
                    val combineContacts = arrayListOf<Contact>()
                    val threshold = 3000
                    var lastCount = 0
                    val numberOfIteration =
                        ceil((localContacts.size.toDouble()) / threshold.toDouble()).toInt()
                    for (x in 1..numberOfIteration) {
                        val itemsToPost = localContacts.subList(
                            lastCount,
                            if ((x * threshold) > localContacts.size) localContacts.size else x * threshold
                        )
                        viewModelBGScope.async(Dispatchers.IO) {
                            when (val response =
                                repository.getY2YBeneficiaries(itemsToPost)) {
                                is RetroApiResponse.Success -> {
                                    response.data.data?.let { combineContacts.addAll(it) }
                                    if (combineContacts.size >= localContacts.size) {
                                        combineContacts.sortBy { it.title }
                                        phoneContactLiveData.postValue(combineContacts)
                                        pagingState.postValue(PagingState.DONE)
                                    }
                                }
                                is RetroApiResponse.Error -> {
                                    //state.toast = response.error.message
                                    pagingState.postValue(PagingState.ERROR)
                                    viewModelBGScope.close()
                                }
                            }
                        }
                        lastCount = x * threshold
                    }
                }
            }
        } else {
            phoneContactLiveData.postValue(phoneContactLiveData.value)
            pagingState.postValue(PagingState.DONE)
        }
    }

    private fun fetchContacts(context: Context): MutableList<Contact> {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val defaultCountryCode = Utils.getDefaultCountryCode(context)
            val phoneUtil = PhoneNumberUtil.getInstance()

            val contacts = LinkedHashMap<Long, Contact>()

            val projection = arrayOf(
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME_PRIMARY,
                //ContactsContract.Data.STARRED,
                //ContactsContract.Data.PHOTO_URI,
                ContactsContract.Data.PHOTO_THUMBNAIL_URI,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.MIMETYPE
                //ContactsContract.Data.IN_VISIBLE_GROUP
            )
            val cursor = context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                generateSelection(), null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC"
            )

            if (cursor != null) {
                cursor.moveToFirst()
                val idColumnIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                val displayNamePrimaryColumnIndex =
                    cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY)
                val thumbnailColumnIndex =
                    cursor.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI)
                val mimetypeColumnIndex = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE)
                val dataColumnIndex = cursor.getColumnIndex(ContactsContract.Data.DATA1)
                while (!cursor.isAfterLast) {
                    if (viewModelBGScope.isActive) {
                        val id = cursor.getLong(idColumnIndex)
                        var contact = contacts[id]
                        if (contact == null) {

                            contact = Contact()
                            val displayName = cursor.getString(displayNamePrimaryColumnIndex)
                            if (displayName != null && displayName.isNotEmpty()) {
                                contact.title = displayName
                            }
                            mapThumbnail(cursor, contact, thumbnailColumnIndex)
                            contacts[id] = contact
                        }
                        val mimetype = cursor.getString(mimetypeColumnIndex)
                        when (mimetype) {
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> mapEmail(
                                cursor,
                                contact,
                                dataColumnIndex
                            )
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                                var phoneNumber: String? = cursor.getString(dataColumnIndex)
                                if (phoneNumber != null && phoneNumber.isNotEmpty()) {
                                    phoneNumber = phoneNumber.replace("\\s+".toRegex(), "")

                                    try {
                                        val pn =
                                            phoneUtil.parse(phoneNumber, defaultCountryCode)
                                        contact.mobileNo = pn.nationalNumber.toString()
                                        contact.countryCode = "00${pn.countryCode}"
                                    } catch (e: Exception) {
                                    }
                                }
                            }
                        }
                        cursor.moveToNext()
                    } else break
                }
                cursor.close()
            }
            return (ArrayList(contacts.values) as MutableList<Contact>)
        }
        return mutableListOf()
    }

    private fun generateSelection(): String {
        val mSelectionBuilder = StringBuilder()
        if (mSelectionBuilder.isNotEmpty())
            mSelectionBuilder.append(" AND ")
        mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER)
            .append(" = 1")
        return mSelectionBuilder.toString()
    }

    private fun mapThumbnail(cursor: Cursor, contact: Contact, columnIndex: Int) {
        val uri = cursor.getString(columnIndex)
        if (uri != null && uri.isNotEmpty()) {
            contact.beneficiaryPictureUrl = uri//Uri.parse(uri)
        }
    }

    private fun mapEmail(cursor: Cursor, contact: Contact, columnIndex: Int) {
        val email = cursor.getString(columnIndex)
        if (email != null && email.isNotEmpty()) {
            contact.email = email
        }
    }

//its for local instance testing
//    interface UploadIdCardRetroService {
//
//        @POST(CustomersRepository.URL_Y2Y_BENEFICIARIES)
//        fun getY2YBeneficiaries(@Body contacts: List<Contact>): Call<Y2YBeneficiariesResponse>
//    }

//fun uploadDocument(list: MutableList<Contact>) {
//    val logger = HttpLoggingInterceptor()
//    logger.level = HttpLoggingInterceptor.Level.BODY
//    val client = OkHttpClient.Builder()
//        .connectTimeout(100, TimeUnit.SECONDS)
//        .writeTimeout(100, TimeUnit.SECONDS)
//        .readTimeout(100, TimeUnit.SECONDS)
//        //.addInterceptor(CookiesInterceptor())
//        .addInterceptor(logger)
//        .build()
//    val retro: Retrofit = Retrofit.Builder()
//        .baseUrl("http://192.168.0.96:8080/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(client).build()
//    val service = retro.create(UploadIdCardRetroService::class.java)
////        val file = File(result.document.files[1].croppedFile)
////        val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
////        val part =
////            MultipartBody.Part.createFormData("image", file.name, fileReqBody)
////        state.loading = true
//    list.apply {
//        list.addAll(list)
//        list.addAll(list)
//        list.addAll(list)
//
//    }
//
//    Timber.d(
//        ("contact time live request start ${list.size}" + android.text.format.DateFormat.format(
//            "yyyy-MM-dd hh:mm:ss a",
//            java.util.Date()
//        ))
//    )
//    service.getY2YBeneficiaries(list).enqueue(object : Callback<Y2YBeneficiariesResponse> {
//        override fun onFailure(call: Call<Y2YBeneficiariesResponse>, t: Throwable) {
//            t.printStackTrace()
//            Timber.d(
//                ("contact time live request End" + android.text.format.DateFormat.format(
//                    "yyyy-MM-dd hh:mm:ss a",
//                    java.util.Date()
//                ))
//            )
//            state.toast = t.message.toString()
//            pagingState.postValue(PagingState.ERROR)
//        }
//
//        override fun onResponse(
//            call: Call<Y2YBeneficiariesResponse>,
//            response: Response<Y2YBeneficiariesResponse>?
//        ) {
//
//            Timber.d(
//                ("contact time live request End" + android.text.format.DateFormat.format(
//                    "yyyy-MM-dd hh:mm:ss a",
//                    java.util.Date()
//                ))
//            )
//            phoneContactLiveData.postValue(response?.body()?.data)
//            pagingState.postValue(PagingState.DONE)
//
//        }
//    })
//}

    //    override fun getY2YBeneficiaries() {
//
//        pagingState.value = PagingState.LOADING
//        launch {
//            val localContacts = getLocalContacts()
//            if (localContacts.isEmpty()) {
//                phoneContactLiveData.value = mutableListOf()
//                pagingState.value = PagingState.DONE
//            } else {
//                viewModelBGScope.async(Dispatchers.IO) {
//                    when (val response =
//                        repository.getY2YBeneficiaries(
//                            localContacts
//                        )) {
//                        is RetroApiResponse.Success -> {
//                            phoneContactLiveData.postValue(response.data.data)
//                            pagingState.postValue(PagingState.DONE)
//
//                        }
//                        is RetroApiResponse.Error -> {
//                            launch { state.toast = response.error.message }
//                            pagingState.postValue(PagingState.ERROR)
//                        }
//                    }
//                }.await()
//            }
//        }
//    }
}