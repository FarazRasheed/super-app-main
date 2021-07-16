package co.yap.networking

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

class TLSSocketFactory : SSLSocketFactory() {
    private val internalSSLSocketFactory: SSLSocketFactory

    init {
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(systemDefaultTrustManager()), null)
        internalSSLSocketFactory = sslContext.socketFactory
    }

    override fun getDefaultCipherSuites(): Array<String?>? {
        return internalSSLSocketFactory.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String?>? {
        return internalSSLSocketFactory.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(
        s: Socket?,
        host: String?,
        port: Int,
        autoClose: Boolean
    ): Socket? {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                s,
                host,
                port,
                autoClose
            )
        )
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String?, port: Int): Socket? {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(
        host: String?,
        port: Int,
        localHost: InetAddress?,
        localPort: Int
    ): Socket? {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                host,
                port,
                localHost,
                localPort
            )
        )
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress?, port: Int): Socket? {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress?,
        port: Int,
        localAddress: InetAddress?,
        localPort: Int
    ): Socket? {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                address,
                port,
                localAddress,
                localPort
            )
        )
    }

    private fun enableTLSOnSocket(socket: Socket?): Socket? {
        if (socket != null && socket is SSLSocket) {
            (socket).enabledProtocols = arrayOf(
                "TLSv1.1",
                "TLSv1.2"
            )
        }
        return socket
    }

    companion object {
        fun systemDefaultTrustManager(): X509TrustManager {
            return try {
                val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
                // From https://www.washington.edu/itconnect/security/ca/load-der.crt
                val caInput: InputStream = BufferedInputStream(FileInputStream("load-der.crt"))
                val ca: X509Certificate = caInput.use {
                    cf.generateCertificate(it) as X509Certificate
                }

                // Create a KeyStore containing our trusted CAs
                val keyStoreType = KeyStore.getDefaultType()
                val keyStore = KeyStore.getInstance(keyStoreType).apply {
                    load(null, null)
                    setCertificateEntry("ca", ca)
                }

                // Create a TrustManager that trusts the CAs inputStream our KeyStore
                val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
                val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
                    init(keyStore)
                }

                // Create an SSLContext that uses our TrustManager
                val context: SSLContext = SSLContext.getInstance("TLS").apply {
                    init(null, tmf.trustManagers, null)
                }

                //context.socketFactory
                val trustManagers: Array<TrustManager> =
                    tmf.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    ("Unexpected default trust managers:"
                            + trustManagers.contentToString())
                }
                trustManagers[0] as X509TrustManager
            } catch (e: GeneralSecurityException) {
                throw AssertionError() // The system has no TLS. Just give up.
            }
        }
    }

}