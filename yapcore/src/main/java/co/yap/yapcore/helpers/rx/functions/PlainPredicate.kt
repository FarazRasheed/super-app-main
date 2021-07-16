package co.yap.yapcore.helpers.rx.functions

import io.reactivex.functions.Predicate

/**
 * Created by Muhammad Irfan Arshad
 * Like [but without exception][Predicate]
 */
interface PlainPredicate<T> : Predicate<T> {
    /**
     * Test the given input value and return a boolean.
     * @param t the value
     * @return the boolean result
     */
    override fun test(t: T): Boolean
}