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

package slatekit.common.results

/**
 * Minimal subset of http status codes to be used as general purpose codes.
 *
 * DESIGN: One may think that http status code should have no connection
 * outside of an Http context. The http codes in a services/logic layer
 * could be considered an implementation leak. However, consider the following:
 *
 * 1. Many of the http status code are actual quite general purpose.
 *    Such as Ok, BadRequest, Unexpected Error, Unauthorized, etc.
 *
 * 2. They are purposely designed to be extendable
 *    So you can for example create custom codes in the 1000+ range
 *
 * 3. There is a similarity of existing Java / Kotlin exceptions that
 *    map quite nicely to status codes. These include:
 *    Java                     Http
 *    ArgumentException        Bad_Request
 *    SecurityException        Unauthorized
 *    NotImplementedError      NotImplemented
 *    Exception                Server_Error
 *
 * NOTES:
 * You can also use any codes in the Result<S, F> component which models
 * successes and failures. Then just before you return an response at your
 * application boundary ( such as an Http Endpoint ), you can then convert
 * your application code to a Http Status code.
 *
 * The point is you can use any codes you want and some of the http status
 * code are ( on a philosophical level ) considered general purpose in slatekit.
 * 
 */

object ResultCode {
    const val SUCCESS = 200
    const val CONFIRM = 230

    const val FAILURE = 400
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val CONFLICT = 409
    const val DEPRECATED = 426
    const val FILTERED = 450

    const val UNEXPECTED_ERROR = 500
    const val NOT_IMPLEMENTED = 501
    const val NOT_AVAILABLE = 503

    const val HELP = 1000
    const val MISSING = 1404
    const val EXIT = 1001
    const val VERSION = 1002
}
