package co.yap.yapcore

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.AnimBuilder
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.helpers.extentions.showBlockedFeatureAlert
import co.yap.yapcore.managers.FeatureProvisioning

abstract class BaseNavFragment : Fragment() {

    private val defaultAnimation: AnimBuilder = anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }

    private val defaultNavOptions = navOptions {
        anim { defaultAnimation }
    }

    protected fun navigate(
        destinationId: Int,
        args: Bundle? = null,
        screenType: FeatureSet = FeatureSet.NONE,
        navOptions: NavOptions? = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
    ) {
        if (FeatureProvisioning.getFeatureProvisioning(screenType)) {
            showBlockedFeatureAlert(requireActivity(), screenType)
        } else {
            findNavController().navigate(destinationId, args, navOptions)
        }
    }

    protected fun navigate(
        navDirection: NavDirections,
        screenType: FeatureSet = FeatureSet.NONE,
        navOptions: NavOptions? = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
    ) {
        if (FeatureProvisioning.getFeatureProvisioning(screenType)) {
            showBlockedFeatureAlert(requireActivity(), screenType)
        } else {
            findNavController().navigate(navDirection, navOptions)
        }
    }

    /* protected fun navigate(destinationId: Int, args: Bundle? = null, optionsBuilder: NavOptions.Builder.() -> Unit?) {
         findNavController().navigate(destinationId, args, NavOptions.Builder().apply {
             setEnterAnim(defaultAnimation.enter)
             setExitAnim(defaultAnimation.exit)
             setPopEnterAnim(defaultAnimation.popEnter)
             setPopExitAnim(defaultAnimation.popExit)
             optionsBuilder
         }.build())
     }*/

    protected fun navigateBack(destinationId: Int = -1, inclusive: Boolean = false) {
        if (destinationId != -1) {
            findNavController().popBackStack(destinationId, inclusive)
        } else {
            findNavController().popBackStack()
        }
    }

    private fun anim(animBuilder: AnimBuilder.() -> Unit): AnimBuilder =
        AnimBuilder().apply(animBuilder)
}