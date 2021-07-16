package co.yap.yapcore.helpers.rx.functions

import io.reactivex.functions.Function

interface PlainFunction<T, R> : Function<T, R> {
    /**
     * Apply some calculation to the input value and return some other value.
     * @param t the input value
     * @return the output value
     */
    override fun apply(t: T): R
}