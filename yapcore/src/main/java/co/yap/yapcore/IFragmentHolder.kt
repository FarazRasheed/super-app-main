package co.yap.yapcore

interface IFragmentHolder {
    fun onFragmentAttached() {}
    fun onFragmentDetached(tag: String) {}
}