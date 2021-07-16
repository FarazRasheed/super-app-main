package co.yap.modules.dashboard.more.yapforyou.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.more.yapforyou.activities.YAPForYouActivity
import co.yap.modules.dashboard.more.yapforyou.interfaces.IYAPForYou
import co.yap.modules.dashboard.more.yapforyou.viewmodels.YAPForYouViewModel
import co.yap.networking.transactions.responsedtos.achievement.Achievement
import co.yap.translation.Strings
import co.yap.yapcore.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_yap_for_you.*

class YAPForYouFragment : YapForYouBaseFragment<IYAPForYou.ViewModel>() {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_yap_for_you

    override val viewModel: YAPForYouViewModel
        get() = ViewModelProviders.of(this).get(YAPForYouViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAchievements()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnView -> {
                    viewModel.parentViewModel?.selectedPosition = 0
                    if (!viewModel.parentViewModel?.achievements.isNullOrEmpty()) {
                        viewModel.parentViewModel?.achievement =
                            viewModel.parentViewModel?.achievements?.get(0)
                        viewModel.state.toolbarVisibility.set(false)
                        navigate(R.id.achievementDetailFragment)
                    }
                }
            }
        })
    }


    private fun setupRecycleView() {
        viewModel.adaptor.allowFullItemClickListener = true
        viewModel.adaptor.setItemListener(listener)
        rvYapForYou.adapter = viewModel.adaptor
//        viewModel.state.selectedAchievementPercentage =
//            viewModel.getAchievements()[0].percentage.toString()
//        viewModel.state.selectedAchievementImage =
//            viewModel.getAchievements()[0].achievmentIcons?.mainBadgeIcon
//        viewModel.state.selectedAchievementTitle = viewModel.getAchievements()[0].name ?: ""
    }

    private val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (pos in 3..5)
                return

            if (data is Achievement) {
                viewModel.parentViewModel?.selectedPosition = pos
                viewModel.parentViewModel?.achievement = data.copy()
                    .also { it.icon = viewModel.getAchievementIcon(pos, isWithBadged = true) }
                viewModel.state.toolbarVisibility.set(false)
                navigate(R.id.achievementDetailFragment)

            }
        }
    }

    private fun setSelectedAchievement(achievement: Achievement) {
        viewModel.parentViewModel?.achievement = achievement
        viewModel.state.selectedAchievementTitle = achievement.name ?: ""
//        viewModel.state.selectedAchievementImage = achievement.achievmentIcons?.mainBadgeIcon
        viewModel.state.selectedAchievementPercentage =
            getString(Strings.screen_yap_for_you_display_text_completed_percentage).format("${achievement.percentage}%")
    }

    override fun onToolBarClick(id: Int) {
        super.onToolBarClick(id)
        when (id) {
            R.id.ivLeftIcon -> (requireActivity() as YAPForYouActivity).onBackPressed()
            R.id.ivRightIcon -> {
                viewModel.state.toolbarVisibility.set(false)
                navigate(R.id.completedAchievementsFragment)
            }
        }
    }

}