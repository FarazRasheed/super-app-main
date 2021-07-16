package co.yap.security

interface SignatureValidator {
    fun onValidate(isValid: Boolean, originalSign: AppSignature?)
}