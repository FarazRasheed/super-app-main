package co.yap.modules.pdf

import android.app.Application
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.extentions.createTempFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class PDFViewModel(application: Application) :
    BaseViewModel<IPDFActivity.State>(application),
    IPDFActivity.ViewModel {
    override val state: PDFState = PDFState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override var file: File? = null

    override fun downloadFile(filePath: String, success: (file: File?) -> Unit) {
        launch {
            state.loading = true
            getPDFFileFromWeb(filePath)?.let {
                file = it
                success.invoke(it)
            } ?: success.invoke(null)
            state.loading = false
        }
    }

    override fun handlePressView(id: Int) {
        clickEvent.setValue(id)
    }

    private suspend fun getPDFFileFromWeb(path: String) = viewModelBGScope.async(Dispatchers.IO) {
        downloadPDF(path)
    }.await()

    private fun downloadPDF(path: String): File? {
        try {
            val url = URL(path)
            val urlConnection = url.openConnection() as HttpURLConnection
            val file = context.createTempFile("pdf")
            val fileOutput = FileOutputStream(file)
            val inputStream = urlConnection.inputStream

            val buffer = ByteArray(1024)
            var bufferLength: Int = 0
            while (true) {
                bufferLength = inputStream.read(buffer)
                if (bufferLength < 0) {
                    break
                } else
                    fileOutput.write(buffer, 0, bufferLength)
            }
            fileOutput.close()
            return file
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}