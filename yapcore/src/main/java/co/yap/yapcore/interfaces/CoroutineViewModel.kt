package co.yap.yapcore.interfaces

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

internal interface CoroutineViewModel {
    val viewModelJob: Job
    val viewModelScope: CoroutineScope
    fun cancelAllJobs()
    fun launch(block: suspend () -> Unit)
    fun launchBG(block: suspend () -> Unit) :Any
}