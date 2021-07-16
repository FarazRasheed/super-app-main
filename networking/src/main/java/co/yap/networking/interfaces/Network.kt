package co.yap.networking.interfaces

import android.app.Application
import co.yap.networking.AppData

internal interface Network {
    fun initWith(application: Application, appData: AppData)
    fun <T> createService(serviceInterface: Class<T>): T
}