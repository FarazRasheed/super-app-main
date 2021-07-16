/*
 *   Copyright 2014 Oguz Bilgener
 */
package co.yap.widgets.arcmenu

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import co.yap.widgets.arcmenu.animation.DefaultAnimationHandler
import co.yap.widgets.arcmenu.animation.MenuAnimationHandler
import co.yap.yapcore.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

/**
 * Provides the main structure of the menu.
 */

class FloatingActionMenu
/**
 * Constructor that takes the parameters collected using [FloatingActionMenu.Builder]
 *
 * @param mainActionView
 * @param startAngle
 * @param endAngle
 * @param radius
 * @param subActionItems
 * @param animationHandler
 * @param animated
 */
    (
    /**
     * Reference to the view (usually a button) to trigger the menu to show
     */
    val mainActionView: View,
    /**
     * The angle (in degrees, modulus 360) which the circular menu starts from
     */
    private val startAngle: Int,
    /**
     * The angle (in degrees, modulus 360) which the circular menu ends at
     */
    private val endAngle: Int,
    /**
     * Distance of menu items from mainActionView
     */
    /**
     * @return the specified raduis of the menu
     */
    val radius: Int,
    /**
     * List of menu items
     */
    /**
     * @return a reference to the sub action items list
     */
    val subActionItems: List<Item>,
    /**
     * Reference to the preferred [MenuAnimationHandler] object
     */
    private val animationHandler: MenuAnimationHandler?,
    /**
     * whether the openings and closings should be animated or not
     */
    private val animated: Boolean,
    /**
     * Reference to a listener that listens open/close actions
     */
    private var stateChangeListener: MenuStateChangeListener?,
    val alphaOverlay: View?,
    val txtYapIt: View?

) {
    /**
     * whether the menu is currently open or not
     */
    /**
     * @return whether the menu is open or not
     */
    var isOpen: Boolean = false
        private set


    /**
     * Gets the coordinates of the main action view
     * This method should only be called after the main layout of the Activity is drawn,
     * such as when a user clicks the action button.
     *
     * @return a Point containing x and y coordinates of the top left corner of action view
     */
    // This method returns a x and y values that can be larger than the dimensions of the device screen.
    // So, we need to deduce the offsets.
    private val actionViewCoordinates: Point
        get() {
            val coords = IntArray(2)
            mainActionView.getLocationOnScreen(coords)
            val activityFrame = Rect()
            activityContentView.getWindowVisibleDisplayFrame(activityFrame)
            coords[0] -= screenSize.x - activityContentView.measuredWidth
            coords[1] -= activityFrame.height() + activityFrame.top - activityContentView.measuredHeight
//            }
            return Point(coords[0], coords[1])
        }

    /**
     * Returns the center point of the main action view
     *
     * @return the action view center point
     */
    val actionViewCenter: Point
        get() {
            val point = actionViewCoordinates
            point.x += mainActionView.measuredWidth / 2
            point.y += mainActionView.measuredHeight / 2
            return point
        }

    /**
     * Finds and returns the main content view from the Activity context.
     *
     * @return the main content view
     */
    //view.setBackgroundColor(Color.BLACK);
    private val activityContentView: View
        get() {
            try {

                val v =
                    (mainActionView.context as Activity).window.decorView.findViewById<View>(android.R.id.content)
                val view = mainActionView.parent.parent as View
                return v
            } catch (e: ClassCastException) {
                throw ClassCastException("Please provide an Activity context for this FloatingActionMenu.")
            }

        }

    val mainActionViewParent: View
        get() {
            try {
                return mainActionView.parent as View
            } catch (e: ClassCastException) {
                throw ClassCastException("Please provide a parent view for this mainActionView.")
            }

        }

    /**
     * Intended to use for systemOverlay mode.
     *
     * @return the WindowManager for the current context.
     */
    private val windowManager: WindowManager
        get() = mainActionView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = mainActionView.context.resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
            )
            if (resourceId > 0) {
                result = mainActionView.context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    /**
     * Retrieves the screen size from the Activity context
     *
     * @return the screen size as a Point object
     */
    private val screenSize: Point
        get() {
            val size = Point()
            windowManager.defaultDisplay.getSize(size)
            return size
        }

    init {
        // The menu is initially closed.
        this.isOpen = false

        // Listen click events on the main action view
        // In the future, touch and drag events could be listened to offer an alternative behaviour
        this.mainActionView.isClickable = true
        this.mainActionView.setOnClickListener(ActionViewClickListener())
        this.alphaOverlay?.setOnTouchListener(ActionOnTouchListener())

        // Do not forget to set the menu as self to our customizable animation handler
        if (animationHandler != null) {
            animationHandler.menu = this
        }
        // Find items with undefined sizes
        for (item in subActionItems) {
            if (item.width == 0 || item.height == 0) {

                // Figure out the size by temporarily adding it to the Activity content view hierarchy
                // and ask the size from the system
                addViewToCurrentContainer(item.view)
                // Make item view invisible, just in case
                item.view.alpha = 0f
                // Wait for the right time
                item.view.post(ItemViewQueueListener(item))
            }
        }
    }

    /**
     * Simply opens the menu by doing necessary calculations.
     *
     * @param animated if true, this action is executed by the current [MenuAnimationHandler]
     */
    fun open(animated: Boolean) {

        // Get the center of the action view from the following function for efficiency
        // populate destination x,y coordinates of Items
        val center = calculateItemPositions()

        if (animated && animationHandler != null) {
            // If animations are enabled and we have a MenuAnimationHandler, let it do the heavy work
            if (animationHandler.isAnimating()) {
                // Do not proceed if there is an animation currently going on.
                return
            }

            for (i in subActionItems.indices) {
                // It is required that these Item views are not currently added to any parent
                // Because they are supposed to be added to the Activity content view,
                // just before the animation starts
                if (subActionItems[i].view.parent != null) {
                    throw RuntimeException("All of the sub action items have to be independent from a parent.")
                }

                // Initially, place all items right at the center of the main action view
                // Because they are supposed to start animating from that point.
                val params = FrameLayout.LayoutParams(
                    subActionItems[i].width,
                    subActionItems[i].height,
                    Gravity.TOP or Gravity.LEFT
                )
                params.setMargins(
                    center.x - subActionItems[i].width / 2,
                    center.y - subActionItems[i].height / 2,
                    0,
                    0
                )
                addViewToCurrentContainer(subActionItems[i].view, params)
            }
            // Tell the current MenuAnimationHandler to animate from the center
            animationHandler.animateMenuOpening(center)
        } else {
            // If animations are disabled, just place each of the items to their calculated destination positions.
            for (i in subActionItems.indices) {
                // This is currently done by giving them large margins

                val params = FrameLayout.LayoutParams(
                    subActionItems[i].width,
                    subActionItems[i].height,
                    Gravity.TOP or Gravity.LEFT
                )
                params.setMargins(subActionItems[i].x, subActionItems[i].y, 0, 0)
                subActionItems[i].view.layoutParams = params
                // Because they are placed into the main content view of the Activity,
                // which is itself a FrameLayout
                addViewToCurrentContainer(subActionItems[i].view, params)
            }
        }
        // do not forget to specify that the menu is open.
        isOpen = true

        if (stateChangeListener != null) {
            stateChangeListener!!.onMenuOpened(this)
        }

    }

    /**
     * Closes the menu.
     *
     * @param animated if true, this action is executed by the current [MenuAnimationHandler]
     */
    fun close(view: View, animated: Boolean) {
        // If animations are enabled and we have a MenuAnimationHandler, let it do the heavy work
        if (animated && animationHandler != null) {
            if (animationHandler.isAnimating()) {
                // Do not proceed if there is an animation currently going on.
                return
            }
            animationHandler.animateMenuClosing(actionViewCenter)
        } else {
            // If animations are disabled, just detach each of the Item views from the Activity content view.
            for (i in subActionItems.indices) {
                removeViewFromCurrentContainer(subActionItems[i].view)
            }
            //detachOverlayContainer()
        }
        // do not forget to specify that the menu is now closed.
        isOpen = false

        if (stateChangeListener != null) {
            stateChangeListener!!.onMenuClosed(this, view.id)
        }
    }

    /**
     * Toggles the menu
     *
     * @param animated if true, the open/close action is executed by the current [MenuAnimationHandler]
     */
    fun toggle(view: View, animated: Boolean) {
        val rotationArray = if (isOpen) floatArrayOf(10f, 0f) else floatArrayOf(-195f, -180f)
        val animator1 =
            ObjectAnimator.ofFloat(mainActionView, "rotation", if (isOpen) 0f else -180f)
        animator1.repeatCount = 0
        animator1.duration = 400

        val animator2 = ObjectAnimator.ofFloat(mainActionView, "rotation", *rotationArray)
        animator2.repeatCount = 0
        animator2.duration = 400
        val alphaArray = if (isOpen) floatArrayOf(1f, 0f) else floatArrayOf(0f, 1f)
        var alphaAnimation: ObjectAnimator? = null
        if (alphaOverlay != null) {
            alphaOverlay.alpha = if (isOpen) 1f else 0f
            alphaAnimation = ObjectAnimator.ofFloat(
                alphaOverlay,
                View.ALPHA,
                *alphaArray
            )
            alphaAnimation.duration = if (isOpen) 400 else 400
            alphaAnimation.repeatCount = 0
        }

        val set = AnimatorSet()
        set.interpolator = AccelerateDecelerateInterpolator()
        set.play(animator1).before(animator2)
        if (alphaAnimation != null) {
            set.play(alphaAnimation)
        }
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mainActionView.isClickable = true
                mainActionView.setOnClickListener(ActionViewClickListener())
                alphaOverlay?.setOnTouchListener(ActionOnTouchListener())
                if (alphaOverlay?.visibility == View.VISIBLE && !isOpen) {
                    alphaOverlay.visibility = View.GONE
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                animationHandler?.setAnimating(true)
                mainActionView.isClickable = false
                mainActionView.setOnClickListener(null)
                alphaOverlay?.setOnTouchListener(null)
            }
        })
        if (isOpen) {
            txtYapIt?.visibility = View.VISIBLE
            //alphaOverlay?.visibility = View.GONE
            close(view, animated)
        } else {
            txtYapIt?.visibility = View.INVISIBLE
            alphaOverlay?.visibility = View.VISIBLE
            open(animated)
        }
    }

    fun isAnimating() = animationHandler?.isAnimating()

    /**
     * Recalculates the positions of each sub action item on demand.
     */
    fun updateItemPositions() {
        // Only update if the menu is currently open
        if (!isOpen) {
            return
        }
        // recalculate x,y coordinates of Items
        calculateItemPositions()

        // Simply update layout params for each item
        for (i in subActionItems.indices) {
            // This is currently done by giving them large margins
            val params = FrameLayout.LayoutParams(
                subActionItems[i].width,
                subActionItems[i].height,
                Gravity.TOP or Gravity.LEFT
            )
            params.setMargins(subActionItems[i].x, subActionItems[i].y, 0, 0)
            subActionItems[i].view.layoutParams = params
        }
    }

    /**
     * Calculates the desired positions of all items.
     *
     * @return getActionViewCenter()
     */
    private fun calculateItemPositions(): Point {
        // Create an arc that starts from startAngle and ends at endAngle
        // in an area that is as large as 4*radius^2
        val center = actionViewCenter
        val area = RectF(
            (center.x - radius).toFloat(),
            (center.y - radius).toFloat(),
            (center.x + radius).toFloat(),
            (center.y + radius).toFloat()
        )

        val orbit = Path()
        orbit.addArc(area, startAngle.toFloat(), (endAngle - startAngle).toFloat())

        val measure = PathMeasure(orbit, false)

        // Prevent overlapping when it is a full circle
        val divisor: Int
        if (Math.abs(endAngle - startAngle) >= 360 || subActionItems.size <= 1) {
            divisor = subActionItems.size
        } else {
            divisor = subActionItems.size - 1
        }

        // Measure this path, in order to find points that have the same distance between each other
        for (i in subActionItems.indices) {
            val coords = floatArrayOf(0f, 0f)
            measure.getPosTan(i * measure.length / divisor, coords, null)
            // get the x and y values of these points and set them to each of sub action items.
            subActionItems[i].x = coords[0].toInt() - subActionItems[i].width / 2
            subActionItems[i].y =
                coords[1].toInt() - subActionItems[i].height - mainActionView.height / 2
        }
        return center
    }

    private fun addViewToCurrentContainer(view: View, layoutParams: ViewGroup.LayoutParams?) {
//        view.findViewById<FloatingActionButton>(R.id.ivYapIt)
        view.findViewById<FloatingActionButton>(R.id.ivYapIt).setOnClickListener {
            if (isOpen && !animationHandler?.isAnimating()!!)
                toggle(view, true)

        }

        try {
            if (layoutParams != null) {
                val lp = layoutParams as FrameLayout.LayoutParams?
                (activityContentView as ViewGroup).addView(view, lp)
            } else {
                (activityContentView as ViewGroup).addView(view)
            }
        } catch (e: ClassCastException) {
            throw ClassCastException("layoutParams must be an instance of " + "FrameLayout.LayoutParams.")
        }

    }

    fun addViewToCurrentContainer(view: View) {
        addViewToCurrentContainer(view, null)
    }

    fun removeViewFromCurrentContainer(view: View) {
//        if (isSystemOverlay) {
//            overlayContainer!!.removeView(view)
//        } else {
        (activityContentView as ViewGroup).removeView(view)
//        }
    }

    fun setStateChangeListener(listener: MenuStateChangeListener) {
        this.stateChangeListener = listener
    }

    /**
     * A simple click listener used by the main action view
     */
    inner class ActionViewClickListener : View.OnClickListener {

        override fun onClick(v: View) {
            if (!animationHandler?.isAnimating()!!)
                toggle(v, animated)
        }
    }

    inner class ActionOnTouchListener : View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (isOpen && !animationHandler?.isAnimating()!!) {
                toggle(v!!, animated)
            }
            return true
        }
    }

    /**
     * This runnable calculates sizes of Item views that are added to the menu.
     */
    private inner class ItemViewQueueListener(private val item: Item) : Runnable {
        private val tries: Int = 0
        private val MAX_TRIES = 10

        override fun run() {
            // Wait until the the view can be measured but do not push too hard.
            if (item.view.measuredWidth == 0 && tries < MAX_TRIES) {
                item.view.post(this)
                return
            }
            // Measure the size of the item view
            item.width = item.view.measuredWidth
            item.height = item.view.measuredHeight

            // Revert everything back to normal
            item.view.alpha = item.alpha
            // Remove the item view from view hierarchy
            removeViewFromCurrentContainer(item.view)
        }
    }

    /**
     * A simple structure to put a view and its x, y, width and height values together
     */
    class Item(var view: View, var width: Int, var height: Int, var title: String) {
        var x: Int = 0
        var y: Int = 0

        var alpha: Float = 0.toFloat()

        init {
            alpha = view.alpha
            x = 0
            y = 0

        }
    }

    /**
     * A listener to listen open/closed state changes of the Menu
     */
    interface MenuStateChangeListener {
        fun onMenuOpened(menu: FloatingActionMenu)

        fun onMenuClosed(menu: FloatingActionMenu, subActionButtonId: Int)
    }

    /**
     * A builder for [FloatingActionMenu] in conventional Java Builder format
     */
    class Builder @JvmOverloads constructor(
        context: Context
    ) {

        private var startAngle: Int = 0
        private var endAngle: Int = 0
        private var radius: Int = 0
        private var actionView: View? = null
        private val subActionItems: MutableList<Item>
        private var animationHandler: MenuAnimationHandler? = null
        private var animated: Boolean = false
        private var stateChangeListener: MenuStateChangeListener? = null
        private var alphaOverlay: View? = null
        private var txtYapIt: View? = null

        init {
            // val activity = context as BaseBindingActivity<*>
            subActionItems = ArrayList()
            // Default settings
            radius = context.resources.getDimensionPixelSize(R.dimen.action_menu_radius)
            startAngle = 180
            endAngle = 270
            animationHandler = DefaultAnimationHandler()
            animated = true
        }

        fun setStartAngle(startAngle: Int): Builder {
            this.startAngle = startAngle
            return this
        }

        fun setEndAngle(endAngle: Int): Builder {
            this.endAngle = endAngle
            return this
        }

        fun setRadius(radius: Int): Builder {
            this.radius = radius
            return this
        }

        fun addSubActionView(subActionView: View, width: Int, height: Int, title: String): Builder {
            subActionItems.add(Item(subActionView, width, height, title))
            return this
        }

        fun setAlphaOverlay(alphaOverlay: View): Builder {
            this.alphaOverlay = alphaOverlay
            return this
        }

        fun setTxtYapIt(txtYapIt: View): Builder {
            this.txtYapIt = txtYapIt
            return this
        }

        /**
         * Adds a sub action view that is already alive, but not added to a parent View.
         *
         * @param subActionView a view for the menu
         * @return the builder object itself
         */
        fun addSubActionView(subActionView: View): Builder {

            return this.addSubActionView(subActionView, 0, 0, "")
        }

        /**
         * Inflates a new view from the specified resource id and adds it as a sub action view.
         *
         * @param resId   the resource id reference for the view
         * @param context a valid context
         * @return the builder object itself
         */
        fun addSubActionView(layoutResId: Int, context: Context): Builder {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(layoutResId, null, false)
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return this.addSubActionView(view, view.measuredWidth, view.measuredHeight, "")
        }

        fun addSubActionView(
            title: String,
            iconResId: Int,
            layoutResId: Int,
            context: Context,
            position: Int
            //clickListener: View.OnClickListener
        ): Builder {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(layoutResId, null, false)
            view.findViewById<TextView>(R.id.tvTitle).text = title
            val actionButton = view.findViewById<FloatingActionButton>(R.id.ivYapIt)
            actionButton.setImageResource(iconResId)
            // actionButton.setOnClickListener(clickListener)
            view.id = position
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return this.addSubActionView(view, view.measuredWidth, view.measuredHeight, title)
        }

        /**
         * Sets the current animation handler to the specified MenuAnimationHandler child
         *
         * @param animationHandler a MenuAnimationHandler child
         * @return the builder object itself
         */
        fun setAnimationHandler(animationHandler: MenuAnimationHandler): Builder {
            this.animationHandler = animationHandler
            return this
        }

        fun enableAnimations(): Builder {
            animated = true
            return this
        }

        fun disableAnimations(): Builder {
            animated = false
            return this
        }

        fun setStateChangeListener(listener: MenuStateChangeListener): Builder {
            stateChangeListener = listener
            return this
        }

        /**
         * Attaches the whole menu around a main action view, usually a button.
         * All the calculations are made according to this action view.
         *
         * @param actionView
         * @return the builder object itself
         */
        fun attachTo(actionView: View): Builder {
            this.actionView = actionView
            return this
        }


        fun build(): FloatingActionMenu {
            return FloatingActionMenu(
                actionView!!,
                startAngle,
                endAngle,
                radius,
                subActionItems,
                animationHandler,
                animated,
                stateChangeListener,
                alphaOverlay, txtYapIt
            )
        }
    }
}