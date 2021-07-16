package co.yap.networking

import android.app.Application
import android.content.Context
import android.os.Environment
import co.yap.networking.intercepters.CookiesInterceptor
import co.yap.networking.intercepters.NetworkConstraintsInterceptor
import co.yap.networking.intercepters.SessionValidator
import co.yap.networking.interfaces.Network
import co.yap.networking.interfaces.NetworkConstraintsListener
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object RetroNetwork : Network {
    private const val READ_TIMEOUT_SECONDS = 60L
    private const val CONNECTION_TIMEOUT_SECONDS = 60L
    private const val DISK_CACHE_SIZE = (10 * 1024 * 1024).toLong() // 10 MB

    private var retro: Retrofit? = null
    private var networkConstraintsListener: NetworkConstraintsListener? = null
        get() {
            if (field == null) field = NetworkConstraintsListener.DEFAULT
            return field
        }

    override fun initWith(application: Application, appData: AppData) {
        build(application, appData)
    }

    @Throws(IllegalStateException::class)
    override fun <T> createService(serviceInterface: Class<T>): T {
        if (retro == null) throw IllegalStateException("RetroNetwork is not initialised. Make sure you have called 'initWith' before calling this function ")
        return retro?.create(serviceInterface)!!
    }

    private fun build(context: Context, appData: AppData): Retrofit {
        if (retro == null) {
            retro = Retrofit.Builder()
                .baseUrl(appData.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildOkHttpClient(context, appData)).build()
        }
        return retro!!
    }

    private fun buildOkHttpClient(context: Context, appData: AppData): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(getCache())
            .addInterceptor(getHttpLoggingInterceptor(appData))
            .addInterceptor(CookiesInterceptor())
            .addInterceptor(object : NetworkConstraintsInterceptor(context) {
                override fun onInternetUnavailable() {
                    networkConstraintsListener?.onInternetUnavailable()
                }

                override fun onCacheUnavailable() {
                    networkConstraintsListener?.onCacheUnavailable()
                }
            })
            .addInterceptor(object : SessionValidator() {
                override fun invalidate() {
                    networkConstraintsListener?.onSessionInvalid()
                }
            })
        return sslImplementation(okHttpClientBuilder, appData)
    }

    private fun sslImplementation(
        builder: OkHttpClient.Builder,
        appData: AppData
    ): OkHttpClient {
        return if (appData.isReleaseMode()) {
            SSLPiningHelper.setSSLContext(builder)
            builder.certificatePinner(getCertificatePinner(appData)).build()
            /* implementation with certificate in assets
            builder.sslSocketFactory(
                SSLPiningHelper(context).getSSLFactory(),
                SSLPiningHelper(context).getDefaultTrustManager()
            ).build()
            */
        } else {
            builder.build()
        }
    }

    private fun getCertificatePinner(appData: AppData): CertificatePinner {
        return CertificatePinner.Builder()
            //PrePod server
            // For testing removed all other pin leave only below
            //.add("*.yap.co", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            //.add("*.yap.co", "sha256/xYUxUshCD5PVwQ1AgAakwEG6dLIId5QMvqbNVBn1vFw=") // charles
            //.add("*.yap.co", "sha256/Yf/ZlETuML9yDZbbwEFNdRnXKM/Nci/pXaCLCcH8yrU=") // charles
            .add(appData.sslHost ?: "", appData.sslPin1)
            .add(appData.sslHost ?: "", appData.sslPin2)
            .add(appData.sslHost ?: "", appData.sslPin3)
            .build()
    }

    private fun getHttpLoggingInterceptor(
        appData: AppData
    ): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level =
            if (appData.isReleaseMode()) {
                HttpLoggingInterceptor.Level.NONE
            } else {
                HttpLoggingInterceptor.Level.BODY
            }
        return logger
    }

    fun listenNetworkConstraints(listener: NetworkConstraintsListener) {
        this.networkConstraintsListener = listener
    }

    private fun getCache(): Cache {
        val cacheDir = File(Environment.getDataDirectory(), "cache")
        return Cache(cacheDir, DISK_CACHE_SIZE)
    }

}