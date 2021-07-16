package co.yap.yapcore.helpers

class YapStoreItemViewModel(val footerItem: FooterItem) {

    // Custom logic if there any and add only observable or mutable dataList if your really need it.
    fun onBind(footerItem: FooterItem) {
    }
}

class FooterItem(
    var id: Int,
    var name: String
)