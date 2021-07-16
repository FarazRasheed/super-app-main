package co.yap.yapcore.managers

import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.enums.UserAccessRestriction
import co.yap.yapcore.helpers.extentions.getBlockedFeaturesList

object FeatureProvisioning {
    private var blockedFeatures: ArrayList<FeatureSet> = arrayListOf()
    private var restrictions: ArrayList<UserAccessRestriction> = arrayListOf()
    fun configure(
        blockedFeatures: ArrayList<FeatureSet>,
        restrictions: ArrayList<UserAccessRestriction>
    ) {
        this.blockedFeatures = blockedFeatures
        this.restrictions = restrictions
    }

    fun getFeatureProvisioning(screenType: FeatureSet): Boolean {
        return blockedFeatures.contains(screenType)
    }

    fun getUserAccessRestriction(screenType: FeatureSet): UserAccessRestriction {
        return restrictions.find {
            SessionManager.user.getBlockedFeaturesList(it).contains(screenType)
        } ?: UserAccessRestriction.NONE
    }

    fun getTopUserAccessRestriction(): UserAccessRestriction? {
        return restrictions.firstOrNull()
    }
}