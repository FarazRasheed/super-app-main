package co.yap.modules.dashboard.store.household.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.store.household.interfaces.IHouseHoldLanding
import co.yap.modules.dashboard.store.household.viewmodels.HouseHoldLandingViewModel
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue

class HouseHoldLandingActivity : BaseBindingActivity<IHouseHoldLanding.ViewModel>(),
    IHouseHoldLanding.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_house_hold_landing

    override val viewModel: IHouseHoldLanding.ViewModel
        get() = ViewModelProviders.of(this).get(HouseHoldLandingViewModel::class.java)

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, HouseHoldLandingActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnGetHouseHoldAccount -> {
                    startActivityForResult(
                        SubscriptionSelectionActivity.newIntent(this),
                        RequestCodes.REQUEST_ADD_HOUSE_HOLD
                    )
                }
                R.id.imgClose -> {
                    setIntentResult(false)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.REQUEST_ADD_HOUSE_HOLD) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val finishScreen =
                        data.getValue(
                            RequestCodes.REQUEST_CODE_FINISH,
                            ExtraType.BOOLEAN.name
                        ) as? Boolean
                    finishScreen?.let { it ->
                        if (it) {
                            setIntentResult(true)
                        } else {
                            // other things?
                        }
                    }
                }
            }
        }
    }

    private fun setIntentResult(shouldFinished: Boolean) {
        val intent = Intent()
        intent.putExtra(RequestCodes.REQUEST_CODE_FINISH, shouldFinished)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clickEvent.removeObservers(this)
    }
}