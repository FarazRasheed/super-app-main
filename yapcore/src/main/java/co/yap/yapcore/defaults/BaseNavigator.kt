package co.yap.yapcore.defaults

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import co.yap.yapcore.interfaces.IBaseNavigator

open class BaseNavigator(open val activity: AppCompatActivity, override val navHostId: Int) : IBaseNavigator, LifecycleObserver {

    private lateinit var appBarConfiguration: AppBarConfiguration
    override lateinit var navController: NavController

    init {

        (activity as LifecycleOwner).lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun bindNavigation() {
        navController =
            (activity.supportFragmentManager.findFragmentById(navHostId) as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(activity, navController, appBarConfiguration)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            val dest: String = try {
//                resources.getResourceName(destination.id)
//            } catch (e: Resources.NotFoundException) {
//                Integer.toString(destination.id)
//            }
//        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return super.supportNavigation
//    }
}