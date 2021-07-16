package co.yap.modules.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProviders
import co.yap.translation.Strings
import co.yap.widgets.AdvancedWebView
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.R
import co.yap.yapcore.constants.Constants.PAGE_URL
import co.yap.yapcore.constants.Constants.TOOLBAR_TITLE
import co.yap.yapcore.helpers.permissions.PermissionHelper
import kotlinx.android.synthetic.main.fragment_webview.*


class WebViewFragment : BaseBindingFragment<IWebViewFragment.ViewModel>(), IWebViewFragment.View,
    AdvancedWebView.Listener, View.OnKeyListener {
    override fun getBindingVariable() = BR.webViewFragmentViewModel

    override fun getLayoutId() = R.layout.fragment_webview
    private var pageUrl: String? = null
    private var title: String? = null
    var permissionHelper: PermissionHelper? = null

    override val viewModel: IWebViewFragment.ViewModel
        get() = ViewModelProviders.of(this).get(WebViewFragmentViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            pageUrl = it.getString(PAGE_URL, "")
            title = it.getString(
                TOOLBAR_TITLE,
                getString(Strings.screen_help_support_display_text_title)
            )
        }
        viewModel.state.toolbarTitle =
            title ?: getString(
                Strings.screen_help_support_display_text_title
            )
        initAdvanceWebView()

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    activity?.finish()
                }
            }
        }
    }

    private fun initAdvanceWebView() {
        webView?.setListener(activity, this)
        webView?.setGeolocationEnabled(false)
        webView?.setMixedContentAllowed(true)
        webView?.setCookiesEnabled(true)
        webView?.setThirdPartyCookiesEnabled(true)
        webView?.setOnKeyListener(this)
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                progressBar?.let {
                    it.visibility = ProgressBar.GONE
                }
            }
        }
        webView?.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                progressBar?.let {
                    it.visibility = ProgressBar.GONE
                }
            }

        }
        webView?.addHttpHeader("X-Requested-With", "")
        webView?.loadUrl(pageUrl ?: "")
    }


    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        progressBar?.let {
            it.visibility = ProgressBar.VISIBLE
        }
    }

    override fun onPageFinished(url: String?) {
        progressBar?.let {
            it.visibility = ProgressBar.GONE
        }
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        progressBar?.let {
            it.visibility = ProgressBar.GONE
        }
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
        checkPermission(url, suggestedFilename)
    }

    override fun onExternalPageRequest(url: String?) {
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url.toString().startsWith("tel:")) {
            callPhoneNumber(request?.url.toString())
            return true
        } else {
            if (request?.url.toString().startsWith("mailto")) {
                sendEmail(request?.url.toString())
                return true
            }

        }
        return false
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        webView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        webView?.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        webView?.onActivityResult(requestCode, resultCode, intent)
    }

    private fun checkPermission(url: String?, suggestedFilename: String?) {
        permissionHelper = PermissionHelper(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 100
        )
        permissionHelper?.request(object : PermissionHelper.PermissionCallback {
            override fun onPermissionGranted() {
                if (url != null && suggestedFilename != null)
                    if (AdvancedWebView.handleDownload(requireContext(), url, suggestedFilename)) {
                    } else {
                        showToast("Unable to download file")
                    }
            }

            override fun onIndividualPermissionGranted(grantedPermission: Array<String>) {
                showToast("Can't proceed without permissions")
            }

            override fun onPermissionDenied() {
                showToast("Can't proceed without permissions")
            }

            override fun onPermissionDeniedBySystem() {
                showToast("Can't proceed without permissions")
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestPermissionsResult(
            requestCode,
            permissions as Array<String>,
            grantResults
        )
    }

    private fun callPhoneNumber(url: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
        startActivity(intent)
    }

    private fun sendEmail(url: String) {
        val mail = url.replaceFirst("mailto:", "")
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/plain"
        intent.data = Uri.parse("mailto:$mail")
        intent.putExtra(Intent.EXTRA_EMAIL, mail)
        startActivity(Intent.createChooser(intent, "Send Email"))
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event?.action == MotionEvent.ACTION_UP
            && webView.canGoBack()
        ) {
            webView.goBack()
            return true
        }
        return false
    }
}
