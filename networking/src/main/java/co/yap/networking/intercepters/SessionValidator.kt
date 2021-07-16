package co.yap.networking.intercepters

import co.yap.networking.CookiesManager
import co.yap.networking.authentication.AuthRepository
import co.yap.networking.authentication.requestdtos.TokenRefreshRequest
import co.yap.networking.interfaces.TokenValidator
import co.yap.networking.models.RetroApiResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal abstract class SessionValidator : TokenValidator, Interceptor {

    override var tokenRefreshInProgress: Boolean = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        // Check if user is logged in and server revoked the access token.
        if (CookiesManager.isLoggedIn && response.code() == 401) {
            // need to refresh the token since previous token was invalid

            if (!tokenRefreshInProgress) {
                // Refresh token
                tokenRefreshInProgress = true
                when (runBlocking {
                    AuthRepository.refreshJWTToken(
                        TokenRefreshRequest(
                            id_token = CookiesManager.jwtToken,
                            grant_type = "refresh"
                        )
                    )
                }) {
                    is RetroApiResponse.Success -> {
                        val builder =
                            request.newBuilder()
                                .header(KEY_AUTHORIZATION, KEY_BEARER + CookiesManager.jwtToken)
                                .method(request.method(), request.body())
                        response = chain.proceed(builder.build())
                    }
                    else -> {
                        // Error in Refreshing token, Invalidate user now
                        CookiesManager.isLoggedIn = false
                        CookiesManager.jwtToken = ""
                        invalidate()
                    }
                }
                tokenRefreshInProgress = false
            }
        }
        return response
    }


}