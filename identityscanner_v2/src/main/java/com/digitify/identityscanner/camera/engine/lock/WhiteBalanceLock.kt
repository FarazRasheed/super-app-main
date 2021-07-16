package com.digitify.identityscanner.camera.engine.lock

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.os.Build
import androidx.annotation.RequiresApi

import com.digitify.identityscanner.camera.CameraLogger
import com.digitify.identityscanner.camera.engine.action.Action
import com.digitify.identityscanner.camera.engine.action.ActionHolder

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class WhiteBalanceLock : BaseLock() {

    override fun checkIsSupported(holder: ActionHolder): Boolean {
        val isNotLegacy = readCharacteristic(
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL, -1) != CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY
        val awbMode = holder.getBuilder(this).get(CaptureRequest.CONTROL_AWB_MODE)
        val result = (isNotLegacy
                && awbMode != null
                && awbMode == CaptureRequest.CONTROL_AWB_MODE_AUTO)
        LOG.i("checkIsSupported:", result)
        return result
    }

    override fun checkShouldSkip(holder: ActionHolder): Boolean {
        val awbState = holder.getLastResult(this).get(CaptureResult.CONTROL_AWB_STATE)
        val result = awbState != null && awbState == CaptureRequest.CONTROL_AWB_STATE_LOCKED
        LOG.i("checkShouldSkip:", result)
        return result
    }

    override fun onStarted(holder: ActionHolder) {
        holder.getBuilder(this).set(CaptureRequest.CONTROL_AWB_LOCK, true)
        holder.applyBuilder(this)
    }

    override fun onCaptureCompleted(holder: ActionHolder,
                                    request: CaptureRequest,
                                    result: TotalCaptureResult) {
        super.onCaptureCompleted(holder, request, result)
        val awbState = result.get(CaptureResult.CONTROL_AWB_STATE)
        LOG.i("processCapture:", "awbState:", awbState)
        if (awbState == null) return
        when (awbState) {
            CaptureRequest.CONTROL_AWB_STATE_LOCKED -> {
                state = Action.STATE_COMPLETED
            }
            CaptureRequest.CONTROL_AWB_STATE_CONVERGED, CaptureRequest.CONTROL_AWB_STATE_INACTIVE, CaptureRequest.CONTROL_AWB_STATE_SEARCHING -> {
            }// Wait...
        }
    }

    companion object {

        private val TAG = WhiteBalanceLock::class.java.simpleName
        private val LOG = CameraLogger.create(TAG)
    }
}
