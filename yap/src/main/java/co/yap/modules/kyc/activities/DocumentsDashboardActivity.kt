package co.yap.modules.kyc.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.kyc.interfaces.IDocumentsDashboard
import co.yap.modules.kyc.viewmodels.DocumentsDashboardViewModel
import co.yap.networking.customers.responsedtos.documents.GetMoreDocumentsResponse
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.deleteTempFolder
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator
import java.io.File

class DocumentsDashboardActivity : BaseBindingActivity<IDocumentsDashboard.ViewModel>(), INavigator,
    IFragmentHolder {

    override val viewModel: IDocumentsDashboard.ViewModel
        get() = ViewModelProviders.of(this).get(DocumentsDashboardViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this, R.id.kyc_host_fragment)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_documents_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this should be only first time
        viewModel.name.value = intent.getValue(Constants.name, ExtraType.STRING.name) as? String
        viewModel.skipFirstScreen.value =
            intent.getValue(Constants.data, ExtraType.BOOLEAN.name) as? Boolean
        viewModel.document =
            intent.getParcelableExtra("document") as? GetMoreDocumentsResponse.Data.CustomerDocument.DocumentInformation

        addObserver()
    }

    private fun addObserver() {
        viewModel.finishKyc.observe(this, Observer {
            viewModel.paths.forEach { filePath ->
                File(filePath).deleteRecursively()
            }
            goToDashBoard(
                success = it.success,
                skippedPress = !it.success,
                status = it.status
            )
        })
    }


    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.kyc_host_fragment)
        viewModel.skipFirstScreen.value?.let {
            if (it) {
                viewModel.paths.forEach { filePath ->
                    File(filePath).deleteRecursively()
                }
                super.onBackPressed()
            } else {
                if (!BackPressImpl(fragment).onBackPressed()) {
                    viewModel.paths.forEach { filePath ->
                        File(filePath).deleteRecursively()
                    }
                    super.onBackPressed()
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.paths.forEach { filePath ->
            File(filePath).deleteRecursively()
        }
        context.deleteTempFolder()
        super.onDestroy()
    }

    private fun goToDashBoard(success: Boolean, skippedPress: Boolean, status: String = "") {
        val intent = Intent()
        intent.putExtra(Constants.result, success)
        intent.putExtra(Constants.skipped, skippedPress)
        intent.putExtra("status", status)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
