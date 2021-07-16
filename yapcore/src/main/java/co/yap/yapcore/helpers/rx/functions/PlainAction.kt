package co.yap.yapcore.helpers.rx.functions

import io.reactivex.functions.Action

/**
 * Created by Muhammad Irfan Arshad
 * Like [Action] but without Exception
 */
interface PlainAction : Action {
    /**
     * Run the action
     */
    override fun run()
}