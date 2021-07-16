package com.digitify.identityscanner.docscanner.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.translation.Strings
import co.yap.translation.Translator.getString
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.rx.Task
import com.digitify.identityscanner.BR
import com.digitify.identityscanner.R
import com.digitify.identityscanner.base.BaseFragment
import com.digitify.identityscanner.camera.CameraException
import com.digitify.identityscanner.camera.CameraListener
import com.digitify.identityscanner.camera.CameraOptions
import com.digitify.identityscanner.camera.PictureResult
import com.digitify.identityscanner.components.Overlay
import com.digitify.identityscanner.components.TransparentCardView
import com.digitify.identityscanner.databinding.FragmentCameraBinding
import com.digitify.identityscanner.docscanner.activities.IdentityScannerActivity
import com.digitify.identityscanner.docscanner.enums.DocumentPageType
import com.digitify.identityscanner.docscanner.enums.DocumentType
import com.digitify.identityscanner.docscanner.interfaces.Cropper
import com.digitify.identityscanner.docscanner.interfaces.ICamera
import com.digitify.identityscanner.docscanner.viewmodels.CameraViewModel
import com.digitify.identityscanner.docscanner.viewmodels.IdentityScannerViewModel
import com.digitify.identityscanner.utils.ImageUtils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.text.TextRecognition
import id.zelory.compressor.overWrite
import java.io.File
import java.io.IOException

class YapCameraFragment : BaseFragment(),
    ICamera.View, CameraListener {
    private var overlay: Overlay? = null
    private var progress: Dialog? = null

    //    private var cardOverlay: TransparentCardView? = null
    private var viewModel: CameraViewModel? = null
    private var parentViewModel: IdentityScannerViewModel? = null
    private lateinit var binding: FragmentCameraBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)
        activity?.let { actiivty ->
            parentViewModel = ViewModelProviders.of(actiivty).get(
                IdentityScannerViewModel::class.java
            )
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        val view = binding.root
        binding.model = viewModel
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        viewModel?.onStart()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.camera.setLifecycleOwner(this)
        binding.camera.addCameraListener(this)
        if (progress == null) {
            activity?.let { activity ->
                progress = Utils.createProgressDialog(activity)
            }
        }
        binding.camera.visibility = View.INVISIBLE
        binding.cardOverlay.isDrawn.observe(this, Observer {
            if (it) {
//                binding.camera.layoutParams.width = binding.cardOverlay.width
//                binding.camera.layoutParams.height = binding.cardOverlay.cardHeight+((binding.cardOverlay.cardHeight/10)*2)
                val params = RelativeLayout.LayoutParams(
                    binding.cardOverlay.width,
                    binding.cardOverlay.cardHeight + ((binding.cardOverlay.cardHeight / 10) * 2)
                )
                params.setMargins(
                    0,
                    binding.cardOverlay.cardTop - (binding.cardOverlay.cardHeight / 10),
                    0,
                    0
                )
                binding.camera.layoutParams = params
                binding.camera.visibility = View.VISIBLE
            }
        })
    }

    override fun getScreenTitle(): String {
        return getString(R.string.activity_scan_title)
    }

    private fun init() {
        overlay = binding.drawView //,view!!.findViewById(R.id.drawView)
//        cardOverlay = view!!.findViewById(R.id.cardOverlay)
        binding.cardOverlay.cardRatio =
            if (parentViewModel?.documentType == DocumentType.PASSPORT) TransparentCardView.PASSPORT_RATIO else TransparentCardView.ID_CARD_RATIO
        viewModel?.documentType = parentViewModel?.documentType
        viewModel?.scanMode = parentViewModel?.state?.scanMode
    }

    private fun capturePicture() {
        /*   val runnable = Runnable {
               progress?.show()
           }*/
        activity?.run { progress?.show() }
//        Thread(runnable).start()
        if (binding.camera.isTakingPicture) return
        viewModel?.state?.isCapturing = false
        binding.camera.takePicture()
    }

    private val propertyChangedCallback: OnPropertyChangedCallback =
        object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(
                sender: Observable,
                propertyId: Int
            ) {
                if (propertyId == BR.scanMode) {
                    viewModel?.scanMode = parentViewModel?.state?.scanMode
                }

//            else if (propertyId == com.digitify.identityscanner.BR.cardRect) {
////                updateDrawView(getViewModel().getState().getCardRect());
//            }
            }
        }

    override fun openCropper(filename: String) {
        if (!TextUtils.isEmpty(filename)) {
            detectObject(filename = filename)
        } else {
            activity?.let { activity ->
                setInstructions(
                    getString(
                        activity,
                        Strings.idenetity_scanner_sdk_screen_review_info_display_text_error_saving_file
                    )
                )
            }
        }
    }

    private val cropper: Cropper = object : Cropper() {
        override fun onCropped(uri: Uri) {
            IdentityScannerActivity.imageFiles.add(uri.path)
            onCaptureProcessCompleted(uri.path!!)
        }

        override fun onCropFailed(e: Exception) {
            setInstructions(e.localizedMessage)
        }
    }

    override fun onCaptureProcessCompleted(filename: String) {
        context?.let { context ->
            setInstructions(
                getString(
                    context,
                    Strings.idenetity_scanner_sdk_screen_review_info_display_text_capture_process_complete
                )
            )
        }
        parentViewModel?.onPictureTaken(filename)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        cropper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        viewModel?.capturedImage?.observe(
            this,
            Observer { filename: String ->
                openCropper(
                    filename
                )
            }
        )
        viewModel?.capturedDocument?.observe(
            this,
            Observer { filename: String ->
                onCaptureProcessCompleted(
                    filename
                )
            }
        )
        viewModel?.state?.addOnPropertyChangedCallback(propertyChangedCallback)
        parentViewModel?.state?.addOnPropertyChangedCallback(propertyChangedCallback)
    }


    override fun onPause() {
        viewModel?.onStop()
        viewModel?.state?.removeOnPropertyChangedCallback(propertyChangedCallback)
        parentViewModel?.state?.removeOnPropertyChangedCallback(propertyChangedCallback)
        super.onPause()
    }

    override fun setInstructions(inst: String) {
        viewModel?.setInstructions(inst)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        binding.camera.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCameraOpened(options: CameraOptions) {
        viewModel?.state?.isCapturing = true
        binding.camFab.isEnabled = true
        binding.camFab.setOnClickListener { v: View? -> capturePicture() }
    }

    override fun onCameraClosed() {
        binding.camFab.setOnClickListener(null)
        binding.camFab.isEnabled = false
        viewModel?.state?.isCapturing = false
    }

    override fun onCameraError(exception: CameraException) {
        viewModel?.state?.isCapturing = false
        showToast(exception.message)
    }

    override fun onPictureTaken(result: PictureResult) {
        result.toFile(
            ImageUtils.getFilePrivately(activity) ?: File(result.toString())
        ) { file: File? -> viewModel?.handleOnPressCapture(file) }
    }

    override fun onOrientationChanged(orientation: Int) {}
    override fun onAutoFocusStart(point: PointF) {}
    override fun onAutoFocusEnd(successful: Boolean, point: PointF) {}
    override fun onZoomChanged(
        newValue: Float,
        bounds: FloatArray,
        fingers: Array<PointF>?
    ) {
    }

    override fun onExposureCorrectionChanged(
        newValue: Float,
        bounds: FloatArray,
        fingers: Array<PointF>?
    ) {
    }

    private fun detectObject(filename: String) {
        IdentityScannerActivity.imageFiles.add(filename)
        setInstructions("")
        val runnable = Runnable {
            val bitmap = BitmapFactory.decodeFile(filename)
            setOrientation(filename, bitmap) {
                val options = ObjectDetectorOptions.Builder()
                    .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                    .enableClassification()  // Optional
                    .build()
                val objectDetector = ObjectDetection.getClient(options)
                var image: InputImage
                try {
                    image = InputImage.fromBitmap(it, 0)
                    objectDetector.process(image)
                        .addOnSuccessListener { detectedObjects ->
                            if (detectedObjects.isEmpty()) {
                                progress?.hide()
                                setInstructions("Please readjust your card and scan again")
                            } else {
                                var croppedBmp: Bitmap? = null
                                for (detectedObject: DetectedObject in detectedObjects) {
                                    val boundingBox: Rect = detectedObject.boundingBox
                                    val sourceBitmap: Bitmap = it
                                    croppedBmp = Bitmap.createBitmap(
                                        sourceBitmap,
                                        (boundingBox.exactCenterX() - boundingBox.width() / 2).toInt(),
                                        (boundingBox.exactCenterY() - boundingBox.height() / 2).toInt(),
                                        boundingBox.width(),
                                        boundingBox.height()
                                    )
                                    if (parentViewModel?.state?.scanMode != DocumentPageType.BACK) {
                                        reWriteImage(filename, croppedBmp)
                                    } else {
                                        image = InputImage.fromBitmap(croppedBmp, 0)
                                        extractText(image) { success ->
                                            if (success) {
                                                reWriteImage(filename, croppedBmp)
                                            } else {
                                                activity?.runOnUiThread(Runnable {
                                                    progress?.hide()
                                                    setInstructions("Please rescan the card, it's not card back side")
                                                })
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                            activity?.runOnUiThread(Runnable {
                                progress?.hide()
                                setInstructions("Please rescan the card")
                            })
                        }

                } catch (e: IOException) {
                    e.printStackTrace()
                    activity?.runOnUiThread(Runnable {
                        progress?.hide()
                    })
                }

            }
        }
        Thread(runnable).start()

    }

    private fun reWriteImage(filename: String, croppedBmp: Bitmap) {
        var file: File? = null
        Task.runSafely({
            file = overWrite(File(filename), croppedBmp, quality = 90)
        }, {
            activity?.runOnUiThread(Runnable {
                progress?.hide()
            })

            onCaptureProcessCompleted(filename)
        }, true)
    }


    private fun extractText(inputImage: InputImage, callback: (Boolean) -> Unit) {
        val recognizer = TextRecognition.getClient()
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                callback(visionText.text.contains("<<"))
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                activity?.runOnUiThread(Runnable {
                    progress?.hide()
                    setInstructions("Please rescan the card")
                })
            }
    }

    override fun onDestroyView() {
        progress?.dismiss()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        progress = null
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun setOrientation(
        photoPath: String?,
        bitmap: Bitmap,
        success: (file: Bitmap) -> Unit
    ) {
        val ei = ExifInterface(photoPath ?: "")
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var rotatedBitmap: Bitmap
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270F)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }

        success.invoke(rotatedBitmap)
    }
}