package co.yap.yapcore.defaults

import androidx.appcompat.app.AppCompatActivity

class DefaultNavigator(override val activity: AppCompatActivity, override val navHostId: Int) :
    BaseNavigator(activity, navHostId)