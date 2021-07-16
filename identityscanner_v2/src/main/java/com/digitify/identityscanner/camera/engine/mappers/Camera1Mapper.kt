package com.digitify.identityscanner.camera.engine.mappers

import android.hardware.Camera
import android.os.Build

import com.digitify.identityscanner.camera.controls.Control
import com.digitify.identityscanner.camera.controls.Facing
import com.digitify.identityscanner.camera.controls.Flash
import com.digitify.identityscanner.camera.controls.Hdr
import com.digitify.identityscanner.camera.controls.WhiteBalance

import java.util.HashMap

/**
 * A Mapper maps camera engine constants to CameraView constants.
 */
class Camera1Mapper private constructor() {

    fun mapFlash(flash: Flash): String {

        return FLASH[flash]!!
    }

    fun mapFacing(facing: Facing): Int {

        return FACING[facing]!!
    }

    fun mapWhiteBalance(whiteBalance: WhiteBalance): String {

        return WB[whiteBalance]!!
    }

    fun mapHdr(hdr: Hdr): String {

        return HDR[hdr]!!
    }

    fun unmapFlash(cameraConstant: String): Flash? {
        return reverseLookup(FLASH, cameraConstant)
    }

    fun unmapFacing(cameraConstant: Int): Facing? {
        return reverseLookup(FACING, cameraConstant)
    }

    fun unmapWhiteBalance(cameraConstant: String): WhiteBalance? {
        return reverseLookup(WB, cameraConstant)
    }

    fun unmapHdr(cameraConstant: String): Hdr? {
        return reverseLookup(HDR, cameraConstant)
    }

    private fun <C : Control, T> reverseLookup(map: Map<C, T>, `object`: T): C? {
        for (value in map.keys) {
            if (`object` == map[value]) {
                return value
            }
        }
        return null
    }

    companion object {

        private var sInstance: Camera1Mapper? = null

        fun get(): Camera1Mapper {
            if (sInstance == null) {
                sInstance = Camera1Mapper()
            }
            return sInstance!!
        }

        private val FLASH = HashMap<Flash, String>()
        private val WB = HashMap<WhiteBalance, String>()
        private val FACING = HashMap<Facing, Int>()
        private val HDR = HashMap<Hdr, String>()

        init {
            FLASH[Flash.OFF] = Camera.Parameters.FLASH_MODE_OFF
            FLASH[Flash.ON] = Camera.Parameters.FLASH_MODE_ON
            FLASH[Flash.AUTO] = Camera.Parameters.FLASH_MODE_AUTO
            FLASH[Flash.TORCH] = Camera.Parameters.FLASH_MODE_TORCH
            FACING[Facing.BACK] = Camera.CameraInfo.CAMERA_FACING_BACK
            FACING[Facing.FRONT] = Camera.CameraInfo.CAMERA_FACING_FRONT
            WB[WhiteBalance.AUTO] = Camera.Parameters.WHITE_BALANCE_AUTO
            WB[WhiteBalance.INCANDESCENT] = Camera.Parameters.WHITE_BALANCE_INCANDESCENT
            WB[WhiteBalance.FLUORESCENT] = Camera.Parameters.WHITE_BALANCE_FLUORESCENT
            WB[WhiteBalance.DAYLIGHT] = Camera.Parameters.WHITE_BALANCE_DAYLIGHT
            WB[WhiteBalance.CLOUDY] = Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT
            HDR[Hdr.OFF] = Camera.Parameters.SCENE_MODE_AUTO
            if (Build.VERSION.SDK_INT >= 17) {
                HDR[Hdr.ON] = Camera.Parameters.SCENE_MODE_HDR
            } else {
                HDR[Hdr.ON] = "hdr"
            }
        }
    }
}
