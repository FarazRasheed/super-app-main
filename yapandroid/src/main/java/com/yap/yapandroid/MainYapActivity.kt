package com.yap.yapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import co.yap.app.YAPApplication
import co.yap.modules.dashboard.main.activities.YapDashboardActivity
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.IBaseNavigator
import com.yap.yapandroid.main.IMain
import com.yap.yapandroid.main.MainActivity
import com.yap.yapandroid.main.MainViewModel

class MainYapActivity : BaseBindingActivity<IMain.ViewModel>(), INavigator, IFragmentHolder {
    override fun getLayoutId() = R.layout.activity_main

    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IMain.ViewModel
        get() = ViewModelProviders.of(this).get(MainViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this, R.id.main_nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        YAPApplication.AUTO_RESTART_APP = false
//        setContentView(R.layout.fragment_splash)

    }

    fun onclick(view: View){
//        startActivity(intent)
    }
}