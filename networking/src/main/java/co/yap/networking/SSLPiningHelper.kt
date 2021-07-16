package co.yap.networking

import okhttp3.OkHttpClient
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.security.KeyStore
import java.util.*
import javax.net.ssl.*


object SSLPiningHelper {

    /*This is implementation with keystore file in assests
    private val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
    private val caInput: InputStream = BufferedInputStream(getInputStream("yap-cert.crt"))

    private fun getInputStream(name: String): InputStream? {
        return try {
            val inputStream = context.assets.open(name)
            BufferedInputStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private val ca: X509Certificate = caInput.use {
        cf.generateCertificate(it) as X509Certificate
    }
    private val keyStoreType = KeyStore.getDefaultType()
    private val keyStore = KeyStore.getInstance(keyStoreType).apply {
        load(null, null)
        setCertificateEntry("ca", ca)
    }

    private val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
    private val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
        init(keyStore)
    }

    fun getSSLFactory(): SSLSocketFactory {
        return TLSSocketFactory(tmf)
    }
    */


    fun setSSLContext(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + Arrays.toString(
                trustManagers
            )
        }
        val trustManager = trustManagers[0] as X509TrustManager
        val sslContext = TLSSocketFactory(trustManagerFactory)
        builder.sslSocketFactory(sslContext, trustManager)
        return builder
    }

    class TLSSocketFactory(tmf: TrustManagerFactory) : SSLSocketFactory() {
        private val delegate: SSLSocketFactory

        init {
            val context = SSLContext.getInstance("TLS")
            context.init(null, tmf.trustManagers, null)
            delegate = context.socketFactory
        }

        override fun getDefaultCipherSuites(): Array<String> {
            return delegate.defaultCipherSuites
        }

        override fun getSupportedCipherSuites(): Array<String> {
            return delegate.supportedCipherSuites
        }

        @Throws(IOException::class)
        override fun createSocket(): Socket? {
            return enableTLSOnSocket(delegate.createSocket())
        }

        @Throws(IOException::class)
        override fun createSocket(
            s: Socket?,
            host: String?,
            port: Int,
            autoClose: Boolean
        ): Socket? {
            return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose))
        }

        @Throws(IOException::class, UnknownHostException::class)
        override fun createSocket(host: String?, port: Int): Socket? {
            return enableTLSOnSocket(delegate.createSocket(host, port))
        }

        @Throws(IOException::class, UnknownHostException::class)
        override fun createSocket(
            host: String?,
            port: Int,
            localHost: InetAddress?,
            localPort: Int
        ): Socket? {
            return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort))
        }

        @Throws(IOException::class)
        override fun createSocket(host: InetAddress?, port: Int): Socket? {
            return enableTLSOnSocket(delegate.createSocket(host, port))
        }

        @Throws(IOException::class)
        override fun createSocket(
            address: InetAddress?,
            port: Int,
            localAddress: InetAddress?,
            localPort: Int
        ): Socket? {
            return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort))
        }

        private fun enableTLSOnSocket(socket: Socket?): Socket? {
            if (socket != null && socket is SSLSocket) {
                socket.enabledProtocols = arrayOf(
                    "TLSv1.1",
                    "TLSv1.2"
                )
            }
            return socket
        }
    }
}