package com.yap.yapandroid.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class MainChildFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is MainChildViewModel<*>) {
            activity?.let {
                (viewModel as MainChildViewModel<*>).parentViewModel =
                    ViewModelProviders.of(it).get(MainViewModel::class.java)
            }
        }
    }
}