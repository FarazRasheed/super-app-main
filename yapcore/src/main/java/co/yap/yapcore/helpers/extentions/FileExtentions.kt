package co.yap.yapcore.helpers.extentions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.annotation.RequiresApi
import co.yap.yapcore.helpers.DateUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

fun File.sizeInMb(): Int {
    return if (!exists()) 0 else {
        val size = length().toDouble()
        val sizeInKb = size / 1024
        val sizeInMb = sizeInKb / 1024
        return sizeInMb.toInt()
    }
}

fun Context.createTempFile(extension: String): File {
    val dir = File(this.filesDir, "yapTemp")
    if (!dir.exists()) {
        dir.mkdirs()
        dir.mkdir()
    }
    val time = System.currentTimeMillis().toString()
    return File(dir, "${time}.$extension")
}

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
@Throws(IOException::class)
fun Context.dummyEID(): File? {
    val file = this.createTempFile(".jpg")
    if (!file.exists()) {
        val asset: InputStream = this.assets.open("eid_doc.jpg")
        val output = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var size: Int
        while (asset.read(buffer).also { size = it } != -1) {
            output.write(buffer, 0, size)
        }
        asset.close()
        output.close()
        return file
    }
    return null
}

fun Context.getTempFolder(): File {
    return File(this.filesDir, "yapTemp")
}

fun Context.deleteTempFolder(): Boolean {
    val file = File(this.filesDir, "yapTemp")
    return file.deleteRecursively()
}

fun File.deleteRecursivelyYap(): Boolean {
    return deleteRecursively()
}

fun Context.storeBitmap(rootView: View, success: (filePath: String?) -> Unit) {
    val bitmap: Bitmap = takeScreenshotForView(rootView)
    val randomNum = (0..10).random()
    val imageDate = "$randomNum " + getCurrentDateTime()
    val imageName = "YAP-${imageDate}-qrCode"
    val root = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    ).toString()
    val myDir = File("$root/yap_qr_codes")
    myDir.mkdirs()
    val fileName = "${imageName}.jpg"
    val file = File(myDir, fileName)
    if (file.exists()) file.delete()
    try {
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
        success.invoke("$root/yap_qr_codes")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    MediaScannerConnection.scanFile(
        this, arrayOf(file.toString()), null
    ) { _, _ ->
    }
}

fun Context.shareImage(rootView: View) {
    val bitmap: Bitmap = takeScreenshotForView(rootView)
    val imageName = "YAP-qrCode"
    val root = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    ).toString()
    val myDir = File("$root/yap_qr_codes")
    myDir.mkdirs()
    val fileName = "${imageName}.jpg"
    val file = File(myDir, fileName)
    if (file.exists()) file.delete()
    try {
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val share = Intent(Intent.ACTION_SEND)
    share.type = "image/jpeg"
    share.putExtra(
        Intent.EXTRA_STREAM,
        Uri.parse(file.toString())
    )
    this.startActivity(Intent.createChooser(share, "Share Image"))
    file.deleteOnExit()
}

fun takeScreenshotForView(view: View): Bitmap {
    view.measure(
        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
    )
    view.layout(
        view.x.toInt(),
        view.y.toInt(),
        view.x.toInt() + view.measuredWidth,
        view.y.toInt() + view.measuredHeight
    )
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

fun getCurrentDateTime(): String {
    val currentCalendar: Calendar = Calendar.getInstance()
    val date = DateUtils.dateToString(currentCalendar.time, "dd-mm-yyyy")
    return date
}