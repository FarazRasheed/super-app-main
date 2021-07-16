package co.yap.modules.frame

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.MenuItem
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import co.yap.yapcore.*
import co.yap.yapcore.constants.Constants.EXTRA
import co.yap.yapcore.constants.Constants.FRAGMENT_CLASS
import co.yap.yapcore.databinding.DailogActivityFrameBinding
import co.yap.yapcore.helpers.extentions.createFragmentInstance
import co.yap.yapcore.helpers.extentions.instantiateFragment

class FrameDialogActivity : BaseBindingActivity<IFrameActivity.ViewModel>(),
    IFrameActivity.View, IFragmentHolder {

    private lateinit var fragment: BaseBindingFragment<*>
    override fun getBindingVariable() = BR.frameActivityViewModel
    override fun getLayoutId() = R.layout.dailog_activity_frame
    override val viewModel: IFrameActivity.ViewModel
        get() = ViewModelProviders.of(this).get(FrameActivityViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent
        if (extras == null) {
            finish()
            return
        }
        if (!extras.hasExtra(FRAGMENT_CLASS)) {
            finish()
            return
        }

        val fragmentName = extras.getStringExtra(FRAGMENT_CLASS)
        if (fragmentName == null || TextUtils.isEmpty(fragmentName)) {
            finish()
            return
        }
        fragment = instantiateFragment<Fragment>(
            fragmentName
        ) as BaseBindingFragment<*>
        if (extras.hasExtra(EXTRA)) {
            createFragmentInstance(fragment, extras.getBundleExtra(EXTRA)!!)
        } else {
            createFragmentInstance(fragment, Bundle())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fragment.onBackPressed()
    }

    fun getBinding() = viewDataBinding as DailogActivityFrameBinding

    override fun onOptionsItemSelected(item: MenuItem?) =
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setLayoutParams() {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window.attributes)
        val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.54).toInt()
        layoutParams.width = width
        layoutParams.height = height
        layoutParams.gravity = Gravity.CENTER
        window.attributes = layoutParams
    }
}
