package com.digitify.identityscanner.camera.engine.mappers

import android.hardware.camera2.CameraCharacteristics
import android.os.Build
import android.util.Pair
import androidx.annotation.RequiresApi

import com.digitify.identityscanner.camera.controls.Control
import com.digitify.identityscanner.camera.controls.Engine
import com.digitify.identityscanner.camera.controls.Facing
import com.digitify.identityscanner.camera.controls.Flash
import com.digitify.identityscanner.camera.controls.Hdr
import com.digitify.identityscanner.camera.controls.WhiteBalance

import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

/**
 * A Mapper maps camera engine constants to CameraView constants.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class Camera2Mapper private constructor() {

    fun mapFlash(flash: Flash): List<Pair<Int, Int>> {
        val result = ArrayList<Pair<Int, Int>>()
        when (flash) {
            Flash.ON -> {
                result.add(Pair(
                        CameraCharacteristics.CONTROL_AE_MODE_ON_ALWAYS_FLASH,
                        CameraCharacteristics.FLASH_MODE_OFF))
            }
            Flash.AUTO -> {
                result.add(Pair(
                        CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH,
                        CameraCharacteristics.FLASH_MODE_OFF))
                result.add(Pair(
                        CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE,
                        CameraCharacteristics.FLASH_MODE_OFF))
            }
            Flash.OFF -> {
                result.add(Pair(
                        CameraCharacteristics.CONTROL_AE_MODE_ON,
                        CameraCharacteristics.FLASH_MODE_OFF))
                result.add(Pair(
                        CameraCharacteristics.CONTROL_AE_MODE_OFF,
                        CameraCharacteristics.FLASH_MODE_OFF))
            }
            Flash.TORCH -> {
                // When AE_MODE is ON or OFF, we can finally use the flash mode
                // low level control to either turn flash off or open the torch
                result.add(Pair(
                        CameraCharacteristics.CONTROL_AE_MODE_ON,
                        CameraCharacteristics.FLASH_MODE_TORCH))
                result.add(Pair(
                        CameraCharacteristics.CONTROL_AE_MODE_OFF,
                        CameraCharacteristics.FLASH_MODE_TORCH))
            }
        }
        return result
    }

    fun mapFacing(facing: Facing): Int {

        return FACING[facing]!!
    }

    fun mapWhiteBalance(whiteBalance: WhiteBalance): Int {

        return WB[whiteBalance]!!
    }

    fun mapHdr(hdr: Hdr): Int {

        return HDR[hdr]!!
    }

    fun unmapFlash(cameraConstant: Int): Set<Flash> {
        val result = HashSet<Flash>()
        when (cameraConstant) {
            CameraCharacteristics.CONTROL_AE_MODE_OFF, CameraCharacteristics.CONTROL_AE_MODE_ON -> {
                result.add(Flash.OFF)
                result.add(Flash.TORCH)
            }
            CameraCharacteristics.CONTROL_AE_MODE_ON_ALWAYS_FLASH -> {
                result.add(Flash.ON)
            }
            CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH, CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE -> {
                result.add(Flash.AUTO)
            }
            CameraCharacteristics.CONTROL_AE_MODE_ON_EXTERNAL_FLASH -> {
            }
            else -> {
            }
        }// we don't support external flash
        return result
    }

    fun unmapFacing(cameraConstant: Int): Facing? {
        return reverseLookup(FACING, cameraConstant)
    }

    fun unmapWhiteBalance(cameraConstant: Int): WhiteBalance? {
        return reverseLookup(WB, cameraConstant)
    }

    fun unmapHdr(cameraConstant: Int): Hdr? {
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

        private var sInstance: Camera2Mapper? = null

        fun get(): Camera2Mapper {
            if (sInstance == null) {
                sInstance = Camera2Mapper()
            }
            return sInstance!!
        }

        private val FACING = HashMap<Facing, Int>()
        private val WB = HashMap<WhiteBalance, Int>()
        private val HDR = HashMap<Hdr, Int>()

        init {
            FACING[Facing.BACK] = CameraCharacteristics.LENS_FACING_BACK
            FACING[Facing.FRONT] = CameraCharacteristics.LENS_FACING_FRONT
            WB[WhiteBalance.AUTO] = CameraCharacteristics.CONTROL_AWB_MODE_AUTO
            WB[WhiteBalance.CLOUDY] = CameraCharacteristics.CONTROL_AWB_MODE_CLOUDY_DAYLIGHT
            WB[WhiteBalance.DAYLIGHT] = CameraCharacteristics.CONTROL_AWB_MODE_DAYLIGHT
            WB[WhiteBalance.FLUORESCENT] = CameraCharacteristics.CONTROL_AWB_MODE_FLUORESCENT
            WB[WhiteBalance.INCANDESCENT] = CameraCharacteristics.CONTROL_AWB_MODE_INCANDESCENT
            HDR[Hdr.OFF] = CameraCharacteristics.CONTROL_SCENE_MODE_DISABLED
            HDR[Hdr.ON] = 18
        }
    }
}
