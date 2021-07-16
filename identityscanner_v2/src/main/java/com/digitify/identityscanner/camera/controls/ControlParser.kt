package com.digitify.identityscanner.camera.controls

import android.content.Context
import android.content.res.TypedArray

import com.digitify.identityscanner.R

/**
 * Parses controls from XML attributes.
 */
class ControlParser(context: Context, array: TypedArray) {

    private val preview: Int
    private val facing: Int
    private val flash: Int
    private val grid: Int
    private val whiteBalance: Int
    private val mode: Int
    private val hdr: Int
    private val engine: Int

    init {
        preview = array.getInteger(R.styleable.CameraView_cameraPreview, Preview.DEFAULT.value())
        facing = array.getInteger(R.styleable.CameraView_cameraFacing,
                Facing.DEFAULT(context).value())
        flash = array.getInteger(R.styleable.CameraView_cameraFlash, Flash.DEFAULT.value())
        grid = array.getInteger(R.styleable.CameraView_cameraGrid, Grid.DEFAULT.value())
        whiteBalance = array.getInteger(R.styleable.CameraView_cameraWhiteBalance,
                WhiteBalance.DEFAULT.value())
        mode = array.getInteger(R.styleable.CameraView_cameraMode, Mode.DEFAULT.value())
        hdr = array.getInteger(R.styleable.CameraView_cameraHdr, Hdr.DEFAULT.value())
        engine = array.getInteger(R.styleable.CameraView_cameraEngine, Engine.DEFAULT.value())
    }

    fun getPreview(): Preview {
        return Preview.fromValue(preview)
    }

    fun getFacing(): Facing {

        return Facing.fromValue(facing)!!
    }

    fun getFlash(): Flash {
        return Flash.fromValue(flash)
    }

    fun getGrid(): Grid {
        return Grid.fromValue(grid)
    }

    fun getMode(): Mode {
        return Mode.fromValue(mode)
    }

    fun getWhiteBalance(): WhiteBalance {
        return WhiteBalance.fromValue(whiteBalance)
    }

    fun getHdr(): Hdr {
        return Hdr.fromValue(hdr)
    }

    fun getEngine(): Engine {
        return Engine.fromValue(engine)
    }
}
