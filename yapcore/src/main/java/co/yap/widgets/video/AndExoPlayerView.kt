package co.yap.widgets.video

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import co.yap.widgets.video.EnumAspectRatio
import co.yap.widgets.video.EnumResizeMode
import co.yap.yapcore.R
import co.yap.yapcore.helpers.extentions.getScreenWidth
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import java.util.*

class AndExoPlayerView : LinearLayout {
    private var mContext: Context? = null
    private var currSource = ""
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isPreparing = false
    private var typedArray: TypedArray? = null
    private var currPlayWhenReady = false
    private val showController = true
    private val currResizeMode = EnumResizeMode.FILL
    private var currAspectRatio = EnumAspectRatio.ASPECT_16_9
    var player: SimpleExoPlayer? = null
        private set
    var playerView: PlayerView? = null
    private var componentListener: ComponentListener? = null
    private var bandwidthMeter: BandwidthMeter? = null
    private var extractorsFactory: ExtractorsFactory? = null
    private var trackSelectionFactory: TrackSelection.Factory? = null
    private var trackSelector: TrackSelector? = null
    private var exoPlayerCallBack: ExoPlayerCallBack? = null

    inner class ComponentListener : Player.EventListener {
        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            exoPlayerCallBack?.onPlayerStateChanged(playWhenReady, playbackState)
            when (playbackState) {

                Player.STATE_READY -> {
                    if (isPreparing) { // this is accurate
                        isPreparing = false
                    }
                }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            exoPlayerCallBack?.onRepeatModeChanged(repeatMode)
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
            exoPlayerCallBack?.onTracksChanged(trackGroups, trackSelections)
        }

        override fun onLoadingChanged(isLoading: Boolean) {}
        override fun onPlayerError(error: ExoPlaybackException) { // showRetry();
            exoPlayerCallBack?.onError()
        }

        override fun onPositionDiscontinuity(reason: Int) {
            exoPlayerCallBack?.onPositionDiscontinuity(reason)
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        }

        override fun onSeekProcessed() {
        }
    }

    constructor(context: Context) : super(context) {
        initializeView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AndExoPlayerView,
            0, 0
        )
        initializeView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AndExoPlayerView,
            0, 0
        )
        initializeView(context)
    }

    private fun initializeView(context: Context) {
        this.mContext = context
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =
            inflater.inflate(R.layout.layout_video_player_base, this, true)
        playerView = view.findViewById(R.id.simpleExoPlayerView)
        componentListener = ComponentListener()
        if (typedArray != null) {
            if (typedArray!!.hasValue(R.styleable.AndExoPlayerView_andexo_resize_mode)) {
                val resizeMode = typedArray!!.getInteger(
                    R.styleable.AndExoPlayerView_andexo_resize_mode,
                    EnumResizeMode.FILL.value
                )
                setResizeMode(EnumResizeMode.get(resizeMode))
            }
            if (typedArray!!.hasValue(R.styleable.AndExoPlayerView_andexo_aspect_ratio)) {
                val aspectRatio = typedArray!!.getInteger(
                    R.styleable.AndExoPlayerView_andexo_aspect_ratio,
                    EnumAspectRatio.ASPECT_16_9.value
                )
                setAspectRatio(EnumAspectRatio.get(aspectRatio))
            }
            if (typedArray!!.hasValue(R.styleable.AndExoPlayerView_andexo_play_when_ready)) {
                setPlayWhenReady(
                    typedArray!!.getBoolean(
                        R.styleable.AndExoPlayerView_andexo_play_when_ready,
                        false
                    )
                )
            }
            playerView?.hideController()
            playerView?.useController = false
            typedArray!!.recycle()
        }
        initializePlayer()
    }

    private fun initializePlayer() {
        if (player == null) {
            bandwidthMeter = DefaultBandwidthMeter()
            extractorsFactory = DefaultExtractorsFactory()
            trackSelectionFactory = AdaptiveTrackSelection.Factory()
            trackSelector = DefaultTrackSelector()
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
            playerView?.player = player
            player?.playWhenReady = currPlayWhenReady
            player?.seekTo(currentWindow, playbackPosition)
            player?.addListener(componentListener)
        }
    }

    fun setSource(source: String?) {
        val mediaSource = buildMediaSource(source, null)
        //if (player != null) { //showProgress();
        player?.prepare(mediaSource, true, false)
        //}
    }

    fun setSource(
        source: String?,
        extraHeaders: HashMap<String, String>?
    ) {
        val mediaSource = buildMediaSource(source, extraHeaders)
        if (mediaSource != null) {
            //showProgress();
            player?.prepare(mediaSource, true, false)
//            }
        }
    }

    fun setSource(rawResId: Int) {

        val uri = RawResourceDataSource.buildRawResourceUri(rawResId)
        val audioSource = ExtractorMediaSource(
            uri,
            DefaultDataSourceFactory(mContext, "MyExoplayer"),
            DefaultExtractorsFactory(),
            null,
            null
        )
        player?.repeatMode = Player.REPEAT_MODE_ONE
        player?.prepare(audioSource)
    }

    private fun buildMediaSource(
        source: String?,
        extraHeaders: HashMap<String, String>?
    ): MediaSource? {
        if (source == null) {
            Toast.makeText(
                context,
                "Input Is Invalid.",
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
        currSource = source
        val validUrl = URLUtil.isValidUrl(source)
        val uri = Uri.parse(source)
        if (uri == null || uri.lastPathSegment == null) {
            Toast.makeText(
                context,
                "Uri Converter Failed, Input Is Invalid.",
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
        return if (validUrl && uri.lastPathSegment?.contains(KEY_MP4) == true) {
            val sourceFactory =
                DefaultHttpDataSourceFactory(KEY_USER_AGENT)
            if (extraHeaders != null) {
                for ((key, value) in extraHeaders) sourceFactory.defaultRequestProperties[key] =
                    value
            }
            ProgressiveMediaSource.Factory(sourceFactory)
                .createMediaSource(uri)
        } else if (!validUrl && uri.lastPathSegment?.contains(KEY_MP4) == true) {
            ProgressiveMediaSource.Factory(
                DefaultDataSourceFactory(
                    context,
                    KEY_USER_AGENT
                )
            )
                .createMediaSource(uri)
        } else if (uri.lastPathSegment?.contains(KEY_HLS) == true) {
            val sourceFactory =
                DefaultHttpDataSourceFactory(KEY_USER_AGENT)
            if (extraHeaders != null) {
                for ((key, value) in extraHeaders) sourceFactory.defaultRequestProperties[key] =
                    value
            }
            HlsMediaSource.Factory(sourceFactory)
                .createMediaSource(uri)
        } else if (uri.lastPathSegment?.contains(KEY_MP3) == true) {
            val sourceFactory =
                DefaultHttpDataSourceFactory(KEY_USER_AGENT)
            if (extraHeaders != null) {
                for ((key, value) in extraHeaders) sourceFactory.defaultRequestProperties[key] =
                    value
            }
            ProgressiveMediaSource.Factory(sourceFactory)
                .createMediaSource(uri)
        } else {
            val dashChunkSourceFactory =
                DefaultDashChunkSource.Factory(
                    DefaultHttpDataSourceFactory(
                        "ua",
                        DefaultBandwidthMeter()
                    )
                )
            val manifestDataSourceFactory =
                DefaultHttpDataSourceFactory(KEY_USER_AGENT)
            DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                .createMediaSource(uri)
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            currPlayWhenReady = player!!.playWhenReady
            //player?.removeListener(componentListener)
            player?.release()
            player = null
        }
    }

    fun setPlayWhenReady(playWhenReady: Boolean) {
        currPlayWhenReady = playWhenReady
        player?.playWhenReady = playWhenReady
    }

    fun stopPlayer() {
        player?.stop()
    }

    fun pausePlayer() {
        player?.playWhenReady = false
    }

    fun setShowController(showController: Boolean) {
        if (playerView == null) return
        if (showController) {
            playerView?.showController()
            playerView?.useController = true
        } else {
            playerView?.hideController()
            playerView?.useController = false
        }
    }

    fun setResizeMode(resizeMode: EnumResizeMode?) {
        when (resizeMode) {
            EnumResizeMode.FIT -> playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            EnumResizeMode.FILL -> playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            EnumResizeMode.ZOOM -> playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            else -> playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
    }

    //    public void setShowFullScreen(boolean showFullScreen) {
//        if (showFullScreen)
//            frameLayoutFullScreenContainer.setVisibility(VISIBLE);
//        else
//            frameLayoutFullScreenContainer.setVisibility(GONE);
//    }
    fun setAspectRatio(aspectRatio: EnumAspectRatio) {
        currAspectRatio = aspectRatio
        val value = getScreenWidth()
        when (aspectRatio) {
            EnumAspectRatio.ASPECT_1_1 -> playerView?.layoutParams = FrameLayout.LayoutParams(
                value,
                value
            )
            EnumAspectRatio.ASPECT_4_3 -> playerView?.layoutParams = FrameLayout.LayoutParams(
                value,
                3 * value / 4
            )
            EnumAspectRatio.ASPECT_16_9 -> playerView?.layoutParams = FrameLayout.LayoutParams(
                value,
                9 * value / 16
            )
            EnumAspectRatio.ASPECT_MATCH -> playerView?.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            EnumAspectRatio.ASPECT_MP3 -> {
                playerView?.controllerShowTimeoutMs = 0
                playerView?.controllerHideOnTouch = false
                val mp3Height =
                    context.resources.getDimensionPixelSize(R.dimen._40sdp)
                playerView?.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    mp3Height
                )
            }
            EnumAspectRatio.UNDEFINE -> {
                val baseHeight =
                    resources.getDimension(R.dimen._220sdp).toInt()
                playerView?.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    baseHeight
                )
            }
            else -> {
                val baseHeight =
                    resources.getDimension(R.dimen._220sdp).toInt()
                playerView?.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    baseHeight
                )
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        releasePlayer()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // First Hide other objects (listview or recyclerview), better hide them using Gone.
            hideSystemUi()
            val params =
                playerView?.layoutParams as FrameLayout.LayoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            playerView?.layoutParams = params
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { // unhide your objects here.
            showSystemUi()
            setAspectRatio(currAspectRatio)
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        if (playerView == null) return
        playerView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @SuppressLint("InlinedApi")
    private fun showSystemUi() {
        if (playerView == null) return
        playerView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    fun setExoPlayerCallBack(exoPlayerCallBack: ExoPlayerCallBack?) {
        this.exoPlayerCallBack = exoPlayerCallBack
    }


    companion object {
        const val KEY_MP3 = "mp3"
        const val KEY_MP4 = "mp4"
        const val KEY_HLS = "m3u8"
        const val KEY_USER_AGENT = "exoplayer-codelab"
        const val PLAYER_HEIGHT = 500
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }
}