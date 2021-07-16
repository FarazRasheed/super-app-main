package co.yap.modules.others.helper

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.statments.adaptor.CardStatementsAdaptor
import co.yap.modules.dashboard.home.status.StageProgress
import co.yap.networking.transactions.responsedtos.CardStatement
import co.yap.widgets.timelineview.TimelineView
import com.liveperson.infra.utils.Utils.getResources

object BindingHelper {

    @BindingAdapter("adaptorList")
    @JvmStatic
    fun setAdaptor(recycleview: RecyclerView, list: ObservableField<List<CardStatement>>) {
        if (!list.get().isNullOrEmpty())
            if (recycleview.adapter is CardStatementsAdaptor)
                (recycleview.adapter as CardStatementsAdaptor).setList(list.get() ?: emptyList())
    }
//
//    @BindingAdapter("adaptorListBankParams")
//    @JvmStatic
//    fun setAdaptorParam(recycleview: RecyclerView, list: ObservableField<List<BankParams>>) {
//        if (!list.get().isNullOrEmpty())
//            if (recycleview.adapter is AddBeneficiariesAdaptor)
//                (recycleview.adapter as AddBeneficiariesAdaptor).setList(list.get() as List<BankParams>)
//    }


/*
  Dashboard notification status
 */

    @BindingAdapter("status")
    @JvmStatic
    fun setStatus(
        view: androidx.appcompat.widget.AppCompatTextView,
        status: StageProgress
    ) {

        when (status) {

            StageProgress.COMPLETED -> {
                view.background.setColorFilter(
                    getResources().getColor(R.color.colorOpaquAqua),
                    PorterDuff.Mode.SRC_IN
                )
                view.text = "Completed"
            }

            StageProgress.IN_PROGRESS -> {
                view.background.setColorFilter(
                    getResources().getColor(R.color.colorOpaquSecondaryOrange),
                    PorterDuff.Mode.SRC_IN
                )
                view.text = "In progress"
            }
            else -> {
                view.background.setColorFilter(
                    getResources().getColor(R.color.transparent),
                    PorterDuff.Mode.SRC_IN
                )
                view.text = ""
            }

        }
    }

    @BindingAdapter("setOpacity")
    @JvmStatic
    fun setMarkerOpacity(view: TimelineView, isNotActive: Boolean = false) {
        if (isNotActive) {
            view.alpha = 0.4f
        }
    }

    @BindingAdapter("markerDrawable")
    @JvmStatic
    fun setMarkerDrawable(view: TimelineView, drawable: Drawable) {
        view.marker = drawable
    }

    @BindingAdapter("endLineColor")
    @JvmStatic
    fun setEndLineColor(view: TimelineView, hideLine: Boolean) {
        when (hideLine) {
            true -> {
                view.setEndLineColor(R.color.transparent, 3)
            }
            false -> {
                view.setEndLineColor(R.color.colorPrimary, 1)
            }
        }
    }
}
