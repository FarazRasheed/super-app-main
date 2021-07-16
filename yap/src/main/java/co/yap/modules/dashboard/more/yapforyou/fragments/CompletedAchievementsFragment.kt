package co.yap.modules.dashboard.more.yapforyou.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.databinding.FragmentCompletedAchievementsBinding
import co.yap.modules.dashboard.more.yapforyou.interfaces.ICompletedAchievements
import co.yap.modules.dashboard.more.yapforyou.viewmodels.CompletedAchievementsViewModel
import co.yap.yapcore.BR

class CompletedAchievementsFragment : YapForYouBaseFragment<ICompletedAchievements.ViewModel>(),
    ICompletedAchievements.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_completed_achievements

    override val viewModel: ICompletedAchievements.ViewModel
        get() = ViewModelProviders.of(this).get(CompletedAchievementsViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().rvCompletedAchievements.adapter = viewModel.adapter
    }

    override fun getBinding(): FragmentCompletedAchievementsBinding {
        return viewDataBinding as FragmentCompletedAchievementsBinding
    }

}