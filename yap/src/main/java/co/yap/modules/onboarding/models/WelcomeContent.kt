package co.yap.modules.onboarding.models

import co.yap.BR
import co.yap.yapcore.interfaces.IBindable

data class WelcomeContent(val title: String, val subTitle: String, val imageResource: Int): IBindable {
    override var bindingVariable: Int = BR.content
}