package co.yap.yapcore.helpers.rx

import co.yap.yapcore.helpers.rx.functions.PlainAction
import co.yap.yapcore.helpers.rx.functions.PlainConsumer
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

abstract class RxLoader<Source, Result>(private val mSource: Source) {
    protected abstract fun load(source: Source): Result?
    @JvmOverloads
    fun load(consumer: PlainConsumer<Result>, onError: PlainAction? = null) {
        val single =
            Single.create { e: SingleEmitter<Result> ->
                val result = load(mSource)
                if (result != null) {
                    e.onSuccess(result)
                } else {
                    e.onError(NullPointerException("Got null result"))
                }
            }
        single.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(
                consumer,
                Consumer { throwable: Throwable? ->
                    onError?.run()
                }
            )
    }

}