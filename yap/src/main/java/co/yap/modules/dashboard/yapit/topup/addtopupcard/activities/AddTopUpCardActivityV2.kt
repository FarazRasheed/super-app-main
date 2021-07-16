package co.yap.modules.dashboard.yapit.topup.addtopupcard.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.modules.dashboard.yapit.topup.addtopupcard.AddTopUpCardDialog
import co.yap.modules.dashboard.yapit.topup.addtopupcard.interfaces.IAddTopUpCard
import co.yap.modules.dashboard.yapit.topup.addtopupcard.viewmodels.AddTopUpCardViewModel
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.widgets.AdvancedWebView
import co.yap.widgets.MultiStateView
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.extentions.toast
import kotlinx.android.synthetic.main.activity_add_top_up_card_v2.*

class AddTopUpCardActivityV2 : BaseBindingActivity<IAddTopUpCard.ViewModel>(), IAddTopUpCard.View,
    AdvancedWebView.Listener, MultiStateView.OnReloadListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_add_top_up_card_v2
    var errors: String? = null
    var alias: String? = null
    var cardColor: String? = null
    var sessionId: String? = null
    var cardNumber: String? = null
    var type: String? = null

    override val viewModel: IAddTopUpCard.ViewModel
        get() = ViewModelProviders.of(this).get(AddTopUpCardViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(Constants.KEY) && intent.hasExtra(Constants.TYPE)) {
            viewModel.state.url = intent.getStringExtra(Constants.KEY)
            type = intent.getStringExtra(Constants.TYPE)
            viewModel.state.toolbarVisibility.set(type == Constants.TYPE_ADD_CARD)
//            if (type == Constants.TYPE_ADD_CARD) {
//                viewModel.state.toolbarVisibility.set(true)
//                //setupWebViewForAddCard()
//
//            } else if (type == Constants.TYPE_TOP_UP_TRANSACTION) {
//                viewModel.state.toolbarVisibility.set(false)
//                //setupWebViewForTopUpCardTransaction()
//            }
            initAdvanceWebView()
        }
        setObservers()
    }

    private fun initAdvanceWebView() {
        webView?.setListener(this, this)
        webView?.setGeolocationEnabled(false)
        webView?.setMixedContentAllowed(true)
        webView?.setCookiesEnabled(true)
        webView?.setThirdPartyCookiesEnabled(true)
//        webView?.webViewClient = object : WebViewClient() {
//
//            override fun onPageFinished(view: WebView, url: String) {
//                 multiStateView.viewState = MultiStateView.ViewState.CONTENT
//            }
//        }
//        webView?.webChromeClient = object : WebChromeClient() {
//
//            override fun onReceivedTitle(view: WebView, title: String) {
//                super.onReceivedTitle(view, title)
//
//                multiStateView.viewState = MultiStateView.ViewState.CONTENT
//            }
//
//        }
        webView?.addHttpHeader("X-Requested-With", "")


        if (type == Constants.TYPE_ADD_CARD) {
            webView?.loadUrl(viewModel.state.url)

        } else if (type == Constants.TYPE_TOP_UP_TRANSACTION) {
            webView?.setGeolocationEnabled(true)
            webView.settings.setSupportZoom(true)
            //        wb.loadUrl(viewModel.state.url)
            val base64 =
                android.util.Base64.encodeToString(
                    viewModel.state.url.toByteArray(),
                    android.util.Base64.NO_PADDING
                )
            webView?.loadData(base64, "text/html", "base64")

        }


    }

    override fun onReload(view: View) {

    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        if (type == Constants.TYPE_TOP_UP_TRANSACTION) {
            url?.let {
                if (it.contains("yap.co") || it.contains("transactions")) {
                    webView?.visibility = View.GONE
                    setDataForTopUpTransaction(true)
                    finish()
                } else {
                    multiStateView.viewState = MultiStateView.ViewState.LOADING
                }
            }

        }
    }

    override fun onPageFinished(url: String?) {
        multiStateView.viewState = MultiStateView.ViewState.CONTENT
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        multiStateView.viewState = MultiStateView.ViewState.EMPTY
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
    }

    override fun onExternalPageRequest(url: String?) {
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.let {
            if (it.url.toString().startsWith("yap-app://")) {
                onNewIntent(intent)
                errors = it.url.getQueryParameter("errors")
                if (errors == null) {
                    sessionId = it.url.getQueryParameter("sessionID")
                    cardColor = it.url.getQueryParameter("color")
                    alias = it.url.getQueryParameter("alias")
                    cardNumber = it.url.getQueryParameter("number")
                    viewModel.addTopUpCard(
                        sessionId.toString(),
                        alias.toString(),
                        cardColor.toString(),
                        cardNumber.toString()
                    )
                } else {
                    toast(errors.toString())
                    recreate()
                }
                return true
            }
        }
        return false
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        webView?.onResume()
        // ...
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        webView?.onPause()
        // ...
        super.onPause()
    }

    override fun onDestroy() {
        webView?.onDestroy()
        // ...
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        webView?.onActivityResult(requestCode, resultCode, intent)
        // ...
    }

//    @SuppressLint("SetJavaScriptEnabled")
//    private fun setupWebViewForTopUpCardTransaction() {
//        wb.webViewClient = object : WebViewClient() {
//            override
//            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
////                val serverCertificate: SslCertificate = error?.certificate!!
//                handler?.proceed()
//            }
//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                url?.let {
//                    if (it.contains("yap.co") || it.contains("transactions")) {
//                        view?.visibility = View.GONE
//                        setDataForTopUpTransaction(true)
//                        finish()
//                    } else {
//                        super.onPageStarted(view, url, favicon)
//                    }
//                }
//
//            }
//        }
//        wb.settings.javaScriptEnabled = true
//        wb.settings.setSupportZoom(true)
//        //        wb.loadUrl(viewModel.state.url)
//        val base64 =
//            android.util.Base64.encodeToString(
//                viewModel.state.url.toByteArray(),
//                android.util.Base64.NO_PADDING
//            )
//        wb.loadData(base64, "text/html", "base64")
//    }


    fun setObservers() {
        viewModel.isCardAdded?.observe(this, Observer {
            if (it != null)
                showAddCardDialog(it)
        })

    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> onBackPressed()
        }
    }

    private fun setData(card: TopUpCard?) {
        val intent = Intent()
        intent.putExtra("card", card)
        intent.putExtra("isCardAdded", true)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun showAddCardDialog(card: TopUpCard) {
        AddTopUpCardDialog.newInstance(object : AddTopUpCardDialog.OnProceedListener {
            override fun onProceed(id: Int) {
                when (id) {
                    R.id.done -> {
                        setData(card)
                        finish()
                    }
                    R.id.btnLater -> {
                        setData(null)
                        finish()
                    }
                }
            }

        }, this).show()
    }

    private fun setDataForTopUpTransaction(isStartPooling: Boolean = false) {
        val intent = Intent()
        intent.putExtra(Constants.START_POOLING, isStartPooling)
        setResult(Activity.RESULT_OK, intent)
    }
}
