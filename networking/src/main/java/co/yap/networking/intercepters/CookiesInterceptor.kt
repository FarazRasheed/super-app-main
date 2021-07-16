package co.yap.networking.intercepters

import android.text.TextUtils
import co.yap.networking.CookiesManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

const val KEY_X_XSRF_TOKEN = "X-XSRF-TOKEN"
const val KEY_XSRF_TOKEN = "XSRF-TOKEN="
const val KEY_AUTHORIZATION = "Authorization"
const val KEY_COOKIE = "Cookie"
const val KEY_SET_COOKIE = "Set-Cookie"
const val KEY_PATH_AUTH = "; Path=/auth"
const val KEY_PATH_CUSTOMERS = "; Path=/customers"
const val KEY_PATH_CARDS = "; Path=/cards"
const val KEY_PATH_EMPTY = "; Path=/"
const val KEY_BEARER = "Bearer "
const val KEY_EMPTY = ""
const val KEY_EXPIRES = "Expires"

internal class CookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = addCookiesInRequest(chain.request())
        val response = chain.proceed(request)
        extractCookiesFromResponse(response)
        return response
    }

    private fun addCookiesInRequest(request: Request): Request {
        val builder = request.newBuilder()
        for (cookie in CookiesManager.cookieSet) {
            builder.addHeader(KEY_COOKIE, KEY_XSRF_TOKEN + cookie)
            builder.addHeader(KEY_X_XSRF_TOKEN, cookie)
        }
        if (!TextUtils.isEmpty(CookiesManager.jwtToken)) {
            builder.addHeader(KEY_AUTHORIZATION, KEY_BEARER + CookiesManager.jwtToken)
        }
        return builder.build()
    }

    private fun extractCookiesFromResponse(response: Response) {
        val cookies = HashSet<String>()
        if (response.headers(KEY_SET_COOKIE).isNotEmpty()) {
            for (header in response.headers(KEY_SET_COOKIE)) {
                if (!header.contains(KEY_EXPIRES, true)) {
                    var cookie = header.replace(KEY_XSRF_TOKEN, KEY_EMPTY)
                    cookie = cookie.replace(KEY_XSRF_TOKEN, KEY_EMPTY)
                    cookie = cookie.replace(KEY_PATH_AUTH, KEY_EMPTY)
                    cookie = cookie.replace(KEY_PATH_CUSTOMERS, KEY_EMPTY)
                    cookie = cookie.replace(KEY_PATH_CARDS, KEY_EMPTY)
                    cookie = cookie.replace(KEY_PATH_EMPTY, KEY_EMPTY)
                    cookies.add(cookie)
                    CookiesManager.cookieSet = cookies
                }
            }
        }
    }
}
