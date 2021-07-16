package co.yap.yapcore.interfaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import co.yap.yapcore.BaseFragment
import co.yap.yapcore.IBase


abstract class BaseMapFragment<V: IBase.ViewModel<*>> : BaseFragment<V>() {

    private lateinit var viewDataBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
          viewDataBinding.setVariable(getBindingVariable(), viewModel)
          viewDataBinding.executePendingBindings()
        return viewDataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewDataBinding.setVariable(getBindingVariable(), viewModel)
//        viewDataBinding.executePendingBindings()
    }

    /**MV
     * Override for set binding variable
     *
     * @return variable id
     */

    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */

    @LayoutRes
    abstract fun getLayoutId(): Int

}