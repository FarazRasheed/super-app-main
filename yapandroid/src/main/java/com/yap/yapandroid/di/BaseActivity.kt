package com.yap.yapandroid.di

import android.os.Bundle
import com.yap.yapandroid.di.component.DaggerActivityComponent
import co.yap.yapcore.BaseActivity
import co.yap.yapcore.IBase
import com.yap.yapandroid.di.module.ActivityModule

abstract class BaseActivity<V: IBase.ViewModel<*>> : BaseActivity<V>() {
    lateinit var daggerComponent: DaggerActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performInjection()
    }

    private fun performInjection() {
        daggerComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build() as DaggerActivityComponent
    }
}