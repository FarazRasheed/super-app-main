package co.yap.yapcore.helpers.extentions

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import co.yap.widgets.popmenu.*
import co.yap.yapcore.R

fun Context.getCurrencyPopMenu(
    lifecycleOwner: LifecycleOwner?, itemList: List<PopupMenuItem>,
    onMenuItemClickListener: OnMenuItemClickListener<PopupMenuItem?>?,
    onDismissedListener: OnDismissedListener?
): PopupMenu {
    return PopupMenu.Builder(this)
        .addItemList(itemList)
        .setAutoDismiss(true)
        .setLifecycleOwner(lifecycleOwner!!)
        .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
        .setCircularEffect(CircularEffect.INNER)
        .setMenuRadius(10f)
        .setMenuShadow(10f)
        .setTextColor(ContextCompat.getColor(this, R.color.greyDark))
        .setTextSize(12).setBackgroundAlpha(0f)
        .setTextGravity(Gravity.CENTER)
        .setSelectedTextColor(Color.BLACK)
        .setMenuColor(Color.WHITE)
        .setSelectedMenuColor(ContextCompat.getColor(this, R.color.greySoft))
        .setOnMenuItemClickListener(onMenuItemClickListener)
        .setOnDismissListener(onDismissedListener)
        .setInitializeRule(Lifecycle.Event.ON_CREATE, 0)
        .build()
}
