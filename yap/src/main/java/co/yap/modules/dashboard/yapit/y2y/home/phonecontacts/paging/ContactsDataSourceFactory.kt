package co.yap.modules.dashboard.yapit.y2y.home.phonecontacts.paging

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.customers.requestdtos.Contact

class ContactsDataSourceFactory(private val context: Context, private val repo: CustomersRepository) :
    DataSource.Factory<Long, Contact>() {

    val contactDataSourceLiveData = MutableLiveData<ContactsDataSource>()

    override fun create(): DataSource<Long, Contact> {
        val contactDataSource = ContactsDataSource(context,repo)
        contactDataSourceLiveData.postValue(contactDataSource)
        return contactDataSource
    }
}