package co.yap.yapcore

import androidx.databinding.ViewDataBinding

abstract class BaseBindingHolder(val binding: ViewDataBinding) {

    var adapterPosition: Int = 0
    var itemView = binding.root
    abstract fun bind(obj: Any, binding: ViewDataBinding?)
    abstract fun getBindingVariable(): Int
}
