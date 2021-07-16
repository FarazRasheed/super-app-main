package co.yap.household.onboard.otherscreens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.yap.household.R
import co.yap.household.onboard.onboarding.main.OnBoardingHouseHoldActivity
import co.yap.translation.Strings
import co.yap.yapcore.defaults.DefaultActivity
import kotlinx.android.synthetic.main.activity_onboarding_house_hold_success.*

class HouseHoldSuccessActivity : DefaultActivity() {
    companion object {
        const val BUNDLE_DATA = "bundle_data"
        const val EXISTING_USER = "existingYapUser"
        fun getIntent(context: Context, bundle: Bundle = Bundle()): Intent {
            val intent = Intent(context, HouseHoldSuccessActivity::class.java)
            intent.putExtra(BUNDLE_DATA, bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_house_hold_success)

        setMessage()
        btnCompleteVerification.setOnClickListener {
            startActivity(
                OnBoardingHouseHoldActivity.getIntent(
                    this,
                    intent.getBundleExtra(BUNDLE_DATA)
                )
            )
        }
    }

    /*
     * In this function set dynamic message.
    * */
    private fun setMessage() {
        val message =
            getString(Strings.screen_on_boarding_existing_message).format(
                "Mirza Adil"
            )

        tvOnBoardingExistingMessage?.text = message
    }


}
