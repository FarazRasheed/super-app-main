package com.yap.yapandroid.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.yap.yapandroid.BR
import co.yap.app.YAPApplication
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator
import co.yap.yapcore.managers.SessionManager

import com.yap.yapandroid.R

class MainActivity : BaseBindingActivity<IMain.ViewModel>(), INavigator, IFragmentHolder {

    override fun getLayoutId() = R.layout.activity_main

    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IMain.ViewModel
        get() = ViewModelProviders.of(this).get(MainViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this@MainActivity, R.id.main_nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("MainActivity===>", "onCreate")
        YAPApplication.AUTO_RESTART_APP = false
        SessionManager.expireUserSession()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }

    fun onBackPressedDummy() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("MainActivity===>", "onDestroy")

    }
}

