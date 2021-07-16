package com.digitify.identityscanner.camera;

import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

/**
 * The base class for receiving updates from a {@link CameraView} instance.
 * You can add and remove listeners using {@link CameraView#addCameraListener(CameraListener)}
 * and {@link CameraView#removeCameraListener(CameraListener)}.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public interface CameraListener {


    /**
     * Notifies that the camera was opened.
     * The {@link CameraOptions} object collects all supported options by the current camera.
     *
     * @param options camera supported options
     */
    @UiThread
    void onCameraOpened(@NonNull CameraOptions options);


    /**
     * Notifies that the camera session was closed.
     */
    @UiThread
    void onCameraClosed();


    /**
     * Notifies about an error during the camera setup or configuration.
     *
     * At this point you should inspect the {@link CameraException} reason using
     * {@link CameraException#getReason()} and see what should be done, if anything.
     * If the error is unrecoverable, this is the right moment to show an error dialog, for example.
     *
     * @param exception the error
     */
    @UiThread
    void onCameraError(@NonNull CameraException exception);


    /**
     * Notifies that a picture previously captured with {@link CameraView#takePicture()}
     * or {@link CameraView#takePictureSnapshot()} is ready to be shown or saved to file.
     *
     * If planning to show a bitmap, you can use
     * {@link PictureResult#toBitmap(int, int, BitmapCallback)} to decode the byte array
     * taking care about orientation and threading.
     *
     * @param result captured picture
     */
    @UiThread
    void onPictureTaken(@NonNull PictureResult result);

    /**
     * Notifies that the device was tilted or the window offset changed.
     * The orientation passed is exactly the counter-clockwise rotation that a View should have,
     * in order to appear correctly oriented to the user, considering the way she is
     * holding the device, and the native activity orientation.
     *
     * This is meant to be used for aligning views (e.g. buttons) to the current camera viewport.
     *
     * @param orientation either 0, 90, 180 or 270
     */
    @UiThread
    void onOrientationChanged(int orientation);


    /**
     * Notifies that user interacted with the screen and started metering with a gesture,
     * and touch metering routine is trying to focus around that area.
     * This callback can be used to draw things on screen.
     * Can also be triggered by {@link CameraView#startAutoFocus(float, float)}.
     *
     * @param point coordinates with respect to CameraView.getWidth() and CameraView.getHeight()
     */
    @UiThread
    void onAutoFocusStart(@NonNull PointF point);


    /**
     * Notifies that a touch metering event just ended, and the camera converged
     * to a new focus, exposure and possibly white balance.
     * This might succeed or not.
     * Can also be triggered by {@link CameraView#startAutoFocus(float, float)}.
     *
     * @param successful whether metering succeeded
     * @param point coordinates with respect to CameraView.getWidth() and CameraView.getHeight()
     */
    @UiThread
    void onAutoFocusEnd(boolean successful, @NonNull PointF point);


    /**
     * Notifies that a finger gesture just caused the camera zoom
     * to be changed. This can be used to draw, for example, a seek bar.
     *
     * @param newValue the new zoom value
     * @param bounds min and max bounds for newValue (fixed to 0 ... 1)
     * @param fingers finger positions that caused the event, null if not caused by touch
     */
    @UiThread
    void onZoomChanged(float newValue,
                       @NonNull float[] bounds,
                       @Nullable PointF[] fingers);


    /**
     * Noitifies that a finger gesture just caused the camera exposure correction
     * to be changed. This can be used to draw, for example, a seek bar.
     *
     * @param newValue the new correction value
     * @param bounds min and max bounds for newValue, as returned by {@link CameraOptions}
     * @param fingers finger positions that caused the event, null if not caused by touch
     */
    @UiThread
    void onExposureCorrectionChanged(float newValue,
                                     @NonNull float[] bounds,
                                     @Nullable PointF[] fingers);


}