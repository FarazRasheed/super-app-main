package co.yap.yapcore.helpers.rx

import co.yap.yapcore.helpers.rx.functions.PlainFunction
import co.yap.yapcore.helpers.rx.functions.PlainPredicate
import java.util.*

/**
 * Created by Muhammad Irfan Arshad
 * Utilities functions for functional reactive programming
 */
object Rx {
    /**
     * Map a input list to output list by a [PlainFunction]
     * @param input input List
     * @param func function to map
     * @param <T> Input Type
     * @param <R> Return Type
     * @return mapped list
    </R></T> */
    fun <T, R> map(
        input: List<T>,
        func: PlainFunction<T, R>
    ): List<R> {
        val output: MutableList<R> = ArrayList()
        for (t in input) {
            output.add(func.apply(t))
        }
        return output
    }

    /**
     * Filter a list by a [PlainPredicate]
     * @param input input list
     * @param predicate predicate to filter
     * @param <T> input Type
     * @return filtered list
    </T> */
    fun <T> filter(
        input: List<T>,
        predicate: PlainPredicate<T>
    ): List<T> {
        val out: MutableList<T> = ArrayList()
        for (t in input) {
            if (predicate.test(t)) {
                out.add(t)
            }
        }
        return out
    }

    /**
     * Filter a array by a [PlainPredicate]
     * @param input input list
     * @param predicate predicate to filter
     * @param <T> input Type
     * @return filtered list
    </T> */
    fun <T> filter(
        input: Array<T>,
        predicate: PlainPredicate<T>
    ): List<T> {
        val out: MutableList<T> = ArrayList()
        for (t in input) {
            if (predicate.test(t)) {
                out.add(t)
            }
        }
        return out
    }

    fun <T> filterIndex(
        input: List<T>,
        predicate: PlainPredicate<T>
    ): BooleanArray {
        val result = BooleanArray(input.size)
        var i = 0
        for (t in input) {
            result[i++] = predicate.test(t)
        }
        return result
    }
}