package co.yap.modules.location.tax

import android.view.View
import co.yap.yapcore.R
import co.yap.yapcore.databinding.ItemTaxInfoBinding
import co.yap.yapcore.interfaces.OnItemClickListener

class TaxInfoItemViewModel(
    val taxModel: TaxModel,
    val position: Int,val itemTaxInfoBinding: ItemTaxInfoBinding?,
    val onItemClickListener: OnItemClickListener?
) {
    val spinnerItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is String)
                if (taxModel.options.contains(data)) {
                    taxModel.selectedOption.set(data)
                    view.id = R.id.optionsSpinner
                    onItemClickListener?.onItemClick(view, taxModel, position)
                } else {
                    taxModel.selectedReason = data
                    view.id = R.id.reasonsSpinner
                    onItemClickListener?.onItemClick(view, taxModel, position)
                }
        }
    }

    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.ivCross -> {
                itemTaxInfoBinding?.bcountries?.reset()
                taxModel.taxRowNumber.set(false)
            }
            R.id.lyAddCountry -> {
                taxModel.canAddMore.set(false)
            }
        }
        onItemClickListener?.onItemClick(view, taxModel, position)
    }
}