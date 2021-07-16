package co.yap.widgets.video

import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

interface ExoPlayerCallBack {
    fun onError()
    fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    )

    fun onPositionDiscontinuity(reason: Int)
    fun onPlayerStateChanged(
        playWhenReady: Boolean,
        playbackState: Int
    )
    fun onRepeatModeChanged(repeatMode: Int)
}