package co.yap.yapcore.helpers.biometric

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
@Deprecated("Use co.yap.yapcore.helpers.biometric.BiometricManagerX")
class BiometricManager private constructor(biometricBuilder: BiometricBuilder) : BiometricManagerV23() {


    private var mCancellationSignal = CancellationSignal()

    init {
        this.context = biometricBuilder.context
        this.title = biometricBuilder.title
        this.negativeButtonText = biometricBuilder.negativeButtonText
    }


    fun authenticate(biometricCallback: BiometricCallback) {

        if (title == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog title cannot be null")
            return
        }

        if (negativeButtonText == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog negative button text cannot be null")
            return
        }

        if (!BiometricUtil.isFingerprintSupported) {
            biometricCallback.onSdkVersionNotSupported()
            return
        }

        if (!context?.let { BiometricUtil.isPermissionGranted(it) }!!) {
            biometricCallback.onBiometricAuthenticationPermissionNotGranted()
            return
        }

        if (!BiometricUtil.isHardwareSupported(context!!)) {
            biometricCallback.onBiometricAuthenticationNotSupported()
            return
        }

        if (!BiometricUtil.isFingerprintAvailable(context!!)) {
            biometricCallback.onBiometricAuthenticationNotAvailable()
            return
        }

        displayBiometricDialog(biometricCallback)
    }

    fun cancelAuthentication() {
        if (BiometricUtil.isBiometricPromptEnabled) {
            if (!mCancellationSignal.isCanceled)
                mCancellationSignal.cancel()
        } else {
            if (!mCancellationSignalV23.isCanceled)
                mCancellationSignalV23.cancel()
        }
    }


    private fun displayBiometricDialog(biometricCallback: BiometricCallback) {

        if (BiometricUtil.isBiometricPromptEnabled) {
            displayBiometricPrompt(biometricCallback)
        } else {
            displayBiometricPromptV23(biometricCallback)
        }
    }


    @TargetApi(Build.VERSION_CODES.P)
    private fun displayBiometricPrompt(biometricCallback: BiometricCallback) {
        BiometricPrompt.Builder(context)
            .setTitle(title?:"")
            .setNegativeButton(
                negativeButtonText?:"",
                context!!.mainExecutor,
                DialogInterface.OnClickListener { _, i -> biometricCallback.onAuthenticationCancelled() })
            .build()
            .authenticate(
                mCancellationSignal, context?.mainExecutor!!,
                BiometricCallbackV28(biometricCallback)
            )
    }


    class BiometricBuilder(val context: Context) {

        var title: String? = null
        var negativeButtonText: String? = null

        fun setTitle(title: String): BiometricBuilder {
            this.title = title
            return this
        }

        fun setNegativeButtonText(negativeButtonText: String): BiometricBuilder {
            this.negativeButtonText = negativeButtonText
            return this
        }

        fun build(): BiometricManager {
            return BiometricManager(this)
        }
    }
}
