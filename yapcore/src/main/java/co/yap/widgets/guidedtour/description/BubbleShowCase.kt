package co.yap.widgets.guidedtour.description

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import co.yap.yapcore.R
import java.lang.ref.WeakReference


/**
 * Created by Faheem Riaz
 */

class BubbleShowCase(builder: BubbleShowCaseBuilder) {
    private val SHARED_PREFS_NAME = "BubbleShowCasePrefs"

    private val FOREGROUND_LAYOUT_ID = 731

    private val DURATION_SHOW_CASE_ANIMATION = 200 //ms
    private val DURATION_BACKGROUND_ANIMATION = 700 //ms
    private val DURATION_BEATING_ANIMATION = 700 //ms

    private val MAX_WIDTH_MESSAGE_VIEW_TABLET = 420 //dp

    /**
     * Enum class which corresponds to each valid position for the BubbleMessageView arrow
     */
    enum class ArrowPosition {
        TOP, BOTTOM, LEFT, RIGHT
    }

    /**
     * Highlight mode. It represents the way that the target view will be highlighted
     * - VIEW_LAYOUT: Default value. All the view box is highlighted (the rectangle where the view is contained). Example: For a TextView, all the element is highlighted (characters and background)
     * - VIEW_SURFACE: Only the view surface is highlighted, but not the background. Example: For a TextView, only the characters will be highlighted
     */
    enum class HighlightMode {
        VIEW_LAYOUT, VIEW_SURFACE,VIEW_CIRCLE
    }


    private val mActivity: WeakReference<Activity> = builder.mActivity!!

    //BubbleMessageView params
    private val mImage: Drawable? = builder.mImage
    var mTitle: String? = builder.mTitle
    var mSubtitle: String? = builder.mSubtitle
    private val mCloseAction: Drawable? = builder.mCloseAction
    private val mBackgroundColor: Int? = builder.mBackgroundColor
    private val mTextColor: Int? = builder.mTextColor
    private val mTitleTextSize: Int? = builder.mTitleTextSize
    private val mSubtitleTextSize: Int? = builder.mSubtitleTextSize
    private val mShowOnce: String? = builder.mShowOnce
    private val mDisableCloseAction: Boolean = builder.mDisableCloseAction
    val mArrowPositionList: MutableList<ArrowPosition> = builder.mArrowPositionList
    var mTargetView: WeakReference<View>? = builder.mTargetView
    private val mBubbleShowCaseListener: BubbleShowCaseListener? = builder.mBubbleShowCaseListener

    //Sequence params
    private val mSequenceListener: SequenceShowCaseListener? = builder.mSequenceShowCaseListener
    private val isFirstOfSequence: Boolean = builder.mIsFirstOfSequence ?: false
    private val isLastOfSequence: Boolean = builder.mIsLastOfSequence ?: false

    //References
    private var backgroundDimLayout: RelativeLayout? = null
    internal var bubbleMessageViewBuilder: BubbleMessageView.Builder? = null
    var desBoxViewId: Int? = null
    var bubbleMessageView: BubbleMessageView? = null

    fun show() {
        if (mShowOnce != null) {
            if (isBubbleShowCaseHasBeenShowedPreviously(mShowOnce)) {
                notifyDismissToSequenceListener()
                return
            } else {
                registerBubbleShowCaseInPreferences(mShowOnce)
            }
        }

        val rootView = getViewRoot(mActivity.get()!!)
        backgroundDimLayout = getBackgroundDimLayout()
        bubbleMessageViewBuilder = getBubbleMessageViewBuilder()

        if (mTargetView != null && mArrowPositionList.size <= 1) {
            //Wait until the end of the layout animation, to avoid problems with pending scrolls or view movements
            val handler = Handler()
            handler.postDelayed({
                val target = mTargetView!!.get()!!
                //If the arrow list is empty, the arrow position is set by default depending on the targetView position on the screen
                if (mArrowPositionList.isEmpty()) {
                    if (TourUtils.isViewLocatedAtHalfTopOfTheScreen(
                            mActivity.get()!!,
                            target
                        )
                    ) mArrowPositionList.add(ArrowPosition.TOP) else mArrowPositionList.add(
                        ArrowPosition.BOTTOM
                    )
                    bubbleMessageViewBuilder = getBubbleMessageViewBuilder()
                }

                if (isVisibleOnScreen(target)) {
//                    addBubbleMessageViewDependingOnTargetView(
//                        target,
//                        bubbleMessageViewBuilder!!,
//                        backgroundDimLayout
//                    )
                } else {
                    dismiss()
                }
            }, DURATION_BACKGROUND_ANIMATION.toLong())
        }
        if (isFirstOfSequence) {
            //Add the background dim layout above the root view
            val animation = TourUtils.getFadeInAnimation(0, DURATION_BACKGROUND_ANIMATION)
            if (backgroundDimLayout?.parent == null) {
                backgroundDimLayout?.let {
                    rootView.addView(it)
                }
            }
        }
    }

    fun dismiss() {
        if (backgroundDimLayout != null && isLastOfSequence) {
            //Remove background dim layout if the BubbleShowCase is the last of the sequence
            finishSequence()
        } else {
            //Remove all the views created over the background dim layout waiting for the next BubbleShowCsse in the sequence
            backgroundDimLayout?.removeAllViews()
        }
        notifyDismissToSequenceListener()
    }

    fun finishSequence() {
        val rootView = getViewRoot(mActivity.get()!!)
        rootView.removeView(backgroundDimLayout)
        backgroundDimLayout = null
    }

    private fun notifyDismissToSequenceListener() {
        mSequenceListener?.let { mSequenceListener.onDismiss() }
    }

    fun getViewRoot(activity: Activity): ViewGroup {
        val androidContent = activity.findViewById<ViewGroup>(android.R.id.content)
        return androidContent.parent.parent as ViewGroup
    }

    private fun getBackgroundDimLayout(): RelativeLayout {
        if (mActivity.get()!!.findViewById<RelativeLayout>(FOREGROUND_LAYOUT_ID) != null)
            return mActivity.get()!!.findViewById(FOREGROUND_LAYOUT_ID)
        val backgroundLayout = RelativeLayout(mActivity.get()!!)
        backgroundLayout.id = FOREGROUND_LAYOUT_ID
        backgroundLayout.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        backgroundLayout.setBackgroundColor(
            ContextCompat.getColor(
                mActivity.get()!!,
                R.color.transparent_grey //TODO: change bg color

            )
        )
        backgroundLayout.isClickable = true
        return backgroundLayout
    }

    private fun getBubbleMessageViewBuilder(): BubbleMessageView.Builder {
        return BubbleMessageView.Builder(mActivity.get()!!)
            .from(mActivity.get()!!)
            .arrowPosition(mArrowPositionList)
            .backgroundColor(mBackgroundColor)
            .textColor(mTextColor)
            .titleTextSize(mTitleTextSize)
            .subtitleTextSize(mSubtitleTextSize)
            .title(mTitle)
            .subtitle(mSubtitle)
            .image(mImage)
            .closeActionImage(mCloseAction)
            .disableCloseAction(mDisableCloseAction)
            .listener(object : OnBubbleMessageViewListener {
                override fun onBubbleClick() {
                    mBubbleShowCaseListener?.onBubbleClick(this@BubbleShowCase)
                }

                override fun onCloseActionImageClick() {
                    mBubbleShowCaseListener?.onCloseActionImageClick(this@BubbleShowCase)
                }
            })
    }

    private fun isBubbleShowCaseHasBeenShowedPreviously(id: String): Boolean {
        val mPrefs = mActivity.get()!!.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        return getString(mPrefs, id) != null
    }

    private fun registerBubbleShowCaseInPreferences(id: String) {
        val mPrefs = mActivity.get()!!.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        setString(mPrefs, id, id)
    }

    private fun getString(mPrefs: SharedPreferences, key: String): String? {
        return mPrefs.getString(key, null)
    }

    private fun setString(mPrefs: SharedPreferences, key: String, value: String) {
        val editor = mPrefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * This function creates the BubbleMessageView depending the position of the target and the desired arrow position. This new view is also set on the layout passed by param
     */
    fun addBubbleMessageViewDependingOnTargetView(
        targetView: View?,
        bubbleMessageViewBuilder: BubbleMessageView.Builder
    ): RelativeLayout.LayoutParams? {
        if (targetView == null) return null
        val showCaseParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        when (bubbleMessageViewBuilder.mArrowPosition[0]) {
            ArrowPosition.TOP -> {
                showCaseParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                showCaseParams.setMargins(
                    0, getYposition(targetView) + targetView.height, 0, 0
                )
            }
            ArrowPosition.BOTTOM -> {
                showCaseParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                showCaseParams.setMargins(
                    0, 0, 0, getScreenHeight(mActivity.get()!!) - getYposition(targetView)
                )
            }
        }
        return showCaseParams
    }

    private fun isVisibleOnScreen(targetView: View?): Boolean {
        if (targetView != null) {
            if (getXposition(targetView) >= 0 && getYposition(targetView) >= 0) {
                return getXposition(targetView) != 0 || getYposition(targetView) != 0
            }
        }
        return false
    }

    fun getXposition(targetView: View): Int {
        return TourUtils.getAxisXpositionOfViewOnScreen(targetView) - getScreenHorizontalOffset()
    }

    fun getYposition(targetView: View): Int {
        return TourUtils.getAxisYpositionOfViewOnScreen(targetView) - getScreenVerticalOffset()
    }

     fun getScreenHeight(context: Context): Int {
        return TourUtils.getScreenHeight(context) - getScreenVerticalOffset()
    }

    private fun getScreenWidth(context: Context): Int {
        return TourUtils.getScreenWidth(context) - getScreenHorizontalOffset()
    }

    private fun getScreenVerticalOffset(): Int {
        return if (backgroundDimLayout != null) TourUtils.getAxisYpositionOfViewOnScreen(
            backgroundDimLayout!!
        ) else 0
    }

    private fun getScreenHorizontalOffset(): Int {
        return if (backgroundDimLayout != null) TourUtils.getAxisXpositionOfViewOnScreen(
            backgroundDimLayout!!
        ) else 0
    }

    fun getView(): BubbleMessageView? {
        return bubbleMessageView
    }

}