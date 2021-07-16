package co.yap.modules.pdf

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.R
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.deleteTempFolder
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.interfaces.BackPressImpl
import com.pdfview.PDFView

class PDFActivity : BaseBindingActivity<IPDFActivity.ViewModel>(), IPDFActivity.View {

    companion object {
        private const val URL = "URL"
        private const val CROSS_VISIBILITY = "CROSS_VISIBILITY"
        fun newIntent(context: Context, url: String, bool : Boolean): Intent {
            val intent = Intent(context, PDFActivity::class.java)
            intent.putExtra(URL, url)
            intent.putExtra(CROSS_VISIBILITY, bool)
            return intent
        }
    }

    override val viewModel: IPDFActivity.ViewModel
        get() = ViewModelProviders.of(this).get(PDFViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_pdf

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupData()
        viewModel.clickEvent.observe(this, listener)
    }

    private fun setupData() {
        val visibility = intent?.getValue(CROSS_VISIBILITY, ExtraType.BOOLEAN.name) as? Boolean
        viewModel.state.hideCross = visibility
        val url = intent?.getValue(URL, ExtraType.STRING.name) as? String
        url?.let {
            viewModel.downloadFile(it) { file ->
                file?.let {
                    val pdfView = findViewById<PDFView>(R.id.pdfView)
                    pdfView.fromFile(file).show()
                }
            }
        } ?: close()
    }

    val listener = Observer<Int> {
        when (it) {
            R.id.ivCancelPdf -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }

    private fun close() {
        showToast("Invalid file")
        finish()
    }

    override fun onDestroy() {
        context.deleteTempFolder()
        super.onDestroy()
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
          R.id.ivLeftIcon -> {
              finish()
            }
        }
    }
}

