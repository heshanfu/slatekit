/**
 * <slate_header>
 * url: www.slatekit.com
 * git: www.github.com/code-helix/slatekit
 * org: www.codehelix.co
 * author: Kishore Reddy
 * copyright: 2016 CodeHelix Solutions Inc.
 * license: refer to website and/or github
 * about: A tool-kit, utility library and server-backend
 * mantra: Simplicity above all else
 * </slate_header>
 */

package slatekit.common

import slatekit.common.results.*
import slatekit.common.results.ResultCode.FAILURE
import slatekit.common.results.ResultCode.SUCCESS
import slatekit.common.results.ResultCode.UNEXPECTED_ERROR

/**
 * Container for a Success/Failure value of type T with additional values to represent
 * a string message, code, tag, error and more.
 *
 * @tparam T      : Type T
 */
@Suppress("UNCHECKED_CAST")
sealed class Result<out T, out E> {
    abstract val success: Boolean
    abstract val code: Int
    abstract val msg: String

    companion object {

        inline fun <T> of(f: () -> T): Result<T, String> =
            try {
                Success(f())
            } catch (e: Exception) {
                val err = e.message ?: ""
                Failure(err, FAILURE, err)
            }

        inline fun <T> attempt(f: () -> T): Result<T, Exception> =
            try {
                val data = f()

                // Avoid nested Result<T, E>
                val result = when (data) {
                    is Result<*, *> -> (data as Result<T, Any>).toResultEx()
                    else -> Success(data)
                }
                result
            } catch (e: Exception) {
                val err = e.message ?: ""
                Failure(e, UNEXPECTED_ERROR, err)
            }
    }
}

/**
 * Success branch of the Result
 */
data class Success<out T>(
    val data: T,
    override val code: Int = SUCCESS,
    override val msg: String = "success"
) : Result<T, Nothing>() {

    override val success = true
}

/**
 * Failure branch of the result
 */
data class Failure<out E>(
    val err: E,
    override val code: Int = FAILURE,
    override val msg: String = "failure"
) : Result<Nothing, E>() {

    override val success = false
}

/**
 * Used for representing results with that are simple strings
 */
typealias ResultMsg<T> = Result<T, String>

/**
 * Used for representing results from code that can throw exceptions
 */
typealias ResultEx<T> = Result<T, Exception>

/**
 * Used for representing results from code that can model complex errors using the Err interface
 * NOTE: This should be used in cases to avoid exception handling in place of functional error handling.
 */
typealias ResultErr<T> = Result<T, Err>

inline fun <T1, T2, E> Result<T1, E>.map(f: (T1) -> T2): Result<T2, E> =
    when (this) {
        is Success -> Success(f(this.data), this.code, this.msg)
        is Failure -> this
    }

inline fun <T1, T2, E> Result<T1, E>.flatMap(f: (T1) -> Result<T2, E>): Result<T2, E> =
    when (this) {
        is Success -> f(this.data)
        is Failure -> this
    }

inline fun <T1, T2, E> Result<T1, E>.fold(onSuccess: (T1) -> T2, onError: (E) -> T2): T2 =
    when (this) {
        is Success -> onSuccess(this.data)
        is Failure -> onError(this.err)
    }

inline fun <T, E> Result<T, E>.getOrElse(f: () -> T): T =
    when (this) {
        is Success -> this.data
        is Failure -> f()
    }

inline fun <T, E> Result<T, E>.exists(f: (T) -> Boolean): Boolean =
    when (this) {
        is Success -> f(this.data)
        is Failure -> false
    }

inline fun <T, E> Result<T, E>.onSuccess(f: (T) -> Unit) =
    when (this) {
        is Success -> f(this.data)
        is Failure -> {
        }
    }

inline fun <T, E> Result<T, E>.onFailure(f: (E) -> Unit) =
    when (this) {
        is Success -> {
        }
        is Failure -> {
            f(this.err)
        }
    }

inline fun <T1, T2, E1, E2> Result<T1, E1>.transform(
    onSuccess: (T1) -> Result<T2, E2>,
    onFailure: (E1) -> Result<T2, E2>
): Result<T2, E2> =
    when (this) {
        is Success -> onSuccess(this.data)
        is Failure -> onFailure(this.err)
    }

@Suppress("UNCHECKED_CAST")
fun <T, E> Result<T, E>.toResultEx(): Result<T, Exception> =
    when (this) {
        is Success -> this
        is Failure -> {
            when (this.err) {
                is Exception -> this as Result<T, Exception>
                else -> Failure(Exception(this.msg), this.code, this.msg)
            }
        }
    }
