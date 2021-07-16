package co.yap.yapcore.helpers.glide

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat.getColor
import co.yap.yapcore.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.io.File


val BASE_IMAGES_URL = ""//BuildConfig.BASE_IMAGES_URL

fun setImage(imageView: ImageView, url: String) {

    val mUrl = getUrl(url)
    val fallbackDrawables = getFallbackDrawables(imageView.context)
    val index = Math.abs(mUrl.hashCode() % fallbackDrawables.size)
    Glide.with(imageView).load(mUrl).error(fallbackDrawables[index])
        .placeholder(fallbackDrawables[index]).into(imageView)
}

fun getUrl(url: String?): String {
    return if (url == null || TextUtils.isEmpty(url)) "http:" else BASE_IMAGES_URL + url
}

fun getUrls(url: String?) = if (url == null || TextUtils.isEmpty(url)) "http:" else url

fun getFallbackDrawables(context: Context): Array<Drawable> {
    return arrayOf(
        ColorDrawable(getColor(context, R.color.colorPrimaryAlt)),
        ColorDrawable(getColor(context, R.color.colorSecondaryBlue)),
        ColorDrawable(getColor(context, R.color.colorPrimary)),
        ColorDrawable(getColor(context, R.color.colorPrimaryLight)),
        ColorDrawable(getColor(context, R.color.colorPrimaryDark))
    )
}

fun setImage(imageView: ImageView, url: String, fallback: Drawable) {
    Glide.with(imageView).load(getUrl(url)).error(fallback).fitCenter()
        .placeholder(fallback).into(imageView)
}

fun setImage1(context: Context, imageView: ImageView, url: String) {
    Glide.with(context).load(getUrl(url)).into(imageView)
}

fun setImage(context: Context, imageView: ImageView, uri: Uri) {

    val mUrl = uri.path
    val fallbackDrawables = getFallbackDrawables(imageView.context)
    val index = Math.abs(mUrl!!.hashCode() % fallbackDrawables.size)
    Glide.with(imageView).load(uri).error(fallbackDrawables[index])
        .placeholder(fallbackDrawables[index]).centerCrop().into(imageView)
}

fun setImageNew(context: Context, imageView: ImageView, url: String) {

    val mUrl = getUrl(url)
    val fallbackDrawables = getFallbackDrawables(imageView.context)
    val requestManager = Glide.with(context)
    val requestBuilder = requestManager.load(getUrl(url))
    val requestOptions = RequestOptions()
    // Set place holder image resource id.
    val index = Math.abs(mUrl.hashCode() % fallbackDrawables.size)
    requestOptions.placeholder(fallbackDrawables[index])

    requestOptions.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
    /* Set load image error place holder resource id. When image can not be loaded show this image.*/
    requestOptions.error(fallbackDrawables[index])
    requestBuilder.apply(requestOptions)
    requestBuilder.into(imageView)

}

fun setImage(context: Context, imageView: ImageView, file: File, fallback: Drawable) {
    Glide.with(context).load(file).error(fallback)
        .placeholder(fallback).centerCrop().into(imageView)
}

fun setCircleCropImage(imageView: AppCompatImageView, url: String) {

    val mUrl = getUrl(url)
    val fallbackDrawables = getFallbackDrawables(imageView.context)
    val index = Math.abs(mUrl.hashCode() % fallbackDrawables.size)
    Glide.with(imageView).load(mUrl)
        .error(fallbackDrawables[index])
        .placeholder(fallbackDrawables[index])
        .into(imageView)
}

fun setCircleCropImage(imageView: ImageView, url: String, fallback: Int) {

    val mUrl = getUrl(url)
    Glide.with(imageView).load(mUrl)
        .error(fallback)
        .placeholder(fallback)
        .into(imageView)
}

fun setCircleCropImage(imageView: ImageView, url: String, fallback: Drawable) {

    if (url.isNullOrEmpty()) {
        imageView.setImageDrawable(fallback)
    } else {
        val mUrl = getUrl(url)
        Glide.with(imageView).load(mUrl)
            .error(fallback)
            .placeholder(fallback)
            .into(imageView)
    }
}

fun setCirculerCenterCropImage(imageView: ImageView, url: String, fallback: Drawable) {
    Glide.with(imageView.context)
        .asBitmap().load(url).placeholder(fallback)
        .transforms(CenterCrop(), RoundedCorners(15))
        .into(imageView)
}

//fun setBlurImage(imageView: AppCompatImageView, uri: Uri) {
//    Glide.with(imageView).load(uri).apply(RequestOptions.bitmapTransform(BlurTransformation(10, 3)))
//        .into(imageView)
////    val glide = Glide.with(imageView).load(file)
////    if (applyCircle) {
////        glide.apply(RequestOptions.circleCropTransform()).into(this)
////    } else {
////        glide.into(this)
////    }
//}

fun setImage(imageView: ImageView, uri: Uri) {
    Glide.with(imageView).load(uri).into(imageView)
//    val glide = Glide.with(imageView).load(file)
//    if (applyCircle) {
//        glide.apply(RequestOptions.circleCropTransform()).into(this)
//    } else {
//        glide.into(this)
//    }
}

fun setImage(imageView: ImageView, file: File, applyCircle: Boolean = false) {
    Glide.with(imageView).load(file).into(imageView)
//    val glide = Glide.with(imageView).load(file)
//    if (applyCircle) {
//        glide.apply(RequestOptions.circleCropTransform()).into(this)
//    } else {
//        glide.into(this)
//    }
}

