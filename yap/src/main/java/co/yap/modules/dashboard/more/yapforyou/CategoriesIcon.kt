package co.yap.modules.dashboard.more.yapforyou

import co.yap.R

class CategoriesIcon constructor(
    percentage: Double?,
    colorCode: String
) {
    var percentage: Double?
    var colorCode: String
    var badge: Int = 0
    var roundBadge: Int = 0
    lateinit var achievmentIcons: AchievmentIcons

    init {
        this.percentage = percentage
        this.colorCode = colorCode
        this.badge = badge
        this.roundBadge = roundBadge



        when (Integer.valueOf(colorCode)) {

            R.color.colorDarkPurple -> {
                if (percentage == 100.00) achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_dark_purple,
                    R.drawable.ic_badge_dark_purple
                ) else achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_light_purple,
                    R.drawable.ic_badge_light_purple
                )
            }

            R.color.colorDarkBlue -> {
                if (percentage == 100.00) achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_dark_blue,
                    R.drawable.ic_badge_dark_blue
                ) else achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_light_blue,
                    R.drawable.ic_badge_light_blue
                )

            }
            R.color.colorDarkPink -> {
                if (percentage == 100.00) achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_dark_pink,
                    R.drawable.ic_badge_dark_pink
                ) else achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_light_pink,
                    R.drawable.ic_badge_light_pink
                )
            }
            R.color.colorDarkGrey -> {
                if (percentage == 100.00) achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_dark_grey,
                    R.drawable.ic_badge_dark_grey
                ) else achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_light_grey,
                    R.drawable.ic_badge_light_grey
                )
            }
            R.color.colorDarkPeach -> {
                if (percentage == 100.00) achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_dark_peach,
                    R.drawable.ic_badge_dark_peach
                ) else achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_light_peach,
                    R.drawable.ic_badge_light_peach
                )
            }
            R.color.colorDarkAqua -> {
                if (percentage == 100.00) achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_dark_green,
                    R.drawable.ic_badge_dark_green
                ) else achievmentIcons = AchievmentIcons(
                    R.drawable.ic_round_badge_light_green,
                    R.drawable.ic_badge_light_green
                )
            }
        }
    }
}
