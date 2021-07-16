package co.yap.modules.dashboard.more.main.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.main.interfaces.IMore
import co.yap.modules.dashboard.more.main.states.MoreStates
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.responsedtos.documents.GetMoreDocumentsResponse
import co.yap.networking.interfaces.IRepositoryHolder

class MoreViewModel(application: Application) :
    MoreBaseViewModel<IMore.State>(application),
    IMore.ViewModel, IRepositoryHolder<CustomersRepository> {

    override var BadgeVisibility: Boolean = false
    override var document: GetMoreDocumentsResponse.Data.CustomerDocument.DocumentInformation? =
        null
    override val repository: CustomersRepository = CustomersRepository
    override val state: MoreStates = MoreStates()

    override fun handlePressOnTickButton() {

    }


}