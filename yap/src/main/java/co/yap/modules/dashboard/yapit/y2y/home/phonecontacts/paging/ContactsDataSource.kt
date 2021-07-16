package co.yap.modules.dashboard.yapit.y2y.home.phonecontacts.paging

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.Contact
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.helpers.PagingState
import co.yap.yapcore.helpers.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ContactsDataSource(
    private val context: Context,
    private val repo: CustomersRepository
) :
    PageKeyedDataSource<Long, Contact>() {
    var state: MutableLiveData<PagingState> = MutableLiveData()


    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Contact>
    ) {
        updateState(PagingState.LOADING)

        GlobalScope.launch {
            val response = fetchContacts(context)
            if (response.isNotEmpty()) {
                when (val response =
                    repo.getY2YBeneficiaries(response)) {
                    is RetroApiResponse.Success -> {
                        response.data.data?.let {
                            callback.onResult(
                                it,
                                null,
                                null
                            )
                        }
                        updateState(PagingState.DONE)
                    }
                    is RetroApiResponse.Error -> {
                        callback.onResult(
                            listOf(),
                            null,
                            null
                        )
                        updateState(PagingState.ERROR)
                    }
                }
            } else {
                callback.onResult(
                    listOf(),
                    null,
                    null
                )
                updateState(PagingState.ERROR)
            }
        }
    }

    private fun getPhotoUri(contactId: Long): Uri? {
        val cursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI, null,
            ContactsContract.Data.CONTACT_ID
                    + "="
                    + contactId
                    + " AND "

                    + ContactsContract.Data.MIMETYPE
                    + "='"
                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                    + "'", null, null
        )
        try {
            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    cursor.close()
                    return null // no photo
                }
            } else {
                return null // error in cursor process
            }

        } catch (e: Exception) {
            cursor?.close()
            e.printStackTrace()
            return null
        }
        val person = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI, contactId
        )
        return Uri.withAppendedPath(
            person,
            ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
        )
    }

    private fun fetchContactsEmail(id: Long): String {
        var emlAdd = ""
        val PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.CommonDataKinds.Email.DATA
        )
        val order = ("CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE")

        val filter =
            ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE '' AND " + ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + (id)
        val cur = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            PROJECTION,
            filter,
            null,
            order
        )
        if (cur!!.moveToFirst()) {
            do {
                emlAdd = cur.getString(1)
            } while (cur.moveToNext())
        }

        cur.close()
        return emlAdd
    }

    private fun fetchContacts(context: Context): MutableList<Contact> {
        val defaultCountryCode = Utils.getDefaultCountryCode(context)
        val contacts: MutableList<Contact> = ArrayList()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        try {

            if ((cursor?.count ?: 0) > 0) {
                while (cursor!!.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    val phoneWihtoutCountryCode =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    val phoneNo = Utils.getPhoneWithoutCountryCode(
                        defaultCountryCode,
                        phoneWihtoutCountryCode
                    )
                    val countryCode =
                        Utils.getPhoneNumberCountryCodeForAPI(
                            defaultCountryCode,
                            phoneWihtoutCountryCode
                        )
                    val contactId =
                        cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID))
                    val contactId2 =
                        cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID))
                    val email = fetchContactsEmail(contactId)

                    var photoContentUri: Uri? = getPhotoUri(contactId2)
                    if (photoContentUri== null) photoContentUri= Uri.EMPTY

                    val contact = Contact(
                        name,
                        countryCode,
                        phoneNo,
                        email,
                        photoContentUri?.toString(),
                        false, null
                    )
                    contacts.add(contact)
                }
            }
            cursor?.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return contacts
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Contact>) {
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Contact>) {
    }

    private fun updateState(state: PagingState) {
        this.state.postValue(state)
    }
}