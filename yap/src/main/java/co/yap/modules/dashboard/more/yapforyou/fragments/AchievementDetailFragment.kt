package co.yap.modules.dashboard.more.yapforyou.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.databinding.FragmentAchievementDetailBinding
import co.yap.modules.dashboard.more.yapforyou.interfaces.IAchievementDetail
import co.yap.modules.dashboard.more.yapforyou.viewmodels.AchievementDetailViewModel
import co.yap.widgets.MultiStateView
import co.yap.yapcore.BR
import kotlinx.android.synthetic.main.fragment_achievement_detail.*

class AchievementDetailFragment : YapForYouBaseFragment<IAchievementDetail.ViewModel>(),
    IAchievementDetail.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_achievement_detail

    override val viewModel: AchievementDetailViewModel
        get() = ViewModelProviders.of(this).get(AchievementDetailViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.parentViewModel?.achievement?.let {
            multiStateView?.viewState =
                if (it.features.isNullOrEmpty()) MultiStateView.ViewState.EMPTY else MultiStateView.ViewState.CONTENT
        }
        getBinding().rvAchievements.adapter = viewModel.adapter
    }

    override fun getBinding(): FragmentAchievementDetailBinding {
        return (viewDataBinding as FragmentAchievementDetailBinding)
    }

}