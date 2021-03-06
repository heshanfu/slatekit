/**
<slate_header>
  url: www.slatekit.com
  git: www.github.com/code-helix/slatekit
  org: www.codehelix.co
  author: Kishore Reddy
  copyright: 2016 CodeHelix Solutions Inc.
  license: refer to website and/or github
  about: A Scala utility library, tool-kit and server backend.
  mantra: Simplicity above all else
</slate_header>
  */

package slate.core.apis.core

import slate.common.results.ResultSupportIn
import slate.common.{ApiKey, Result, Strings}
import slate.core.apis.ApiConstants._
import slate.core.apis.Request
import slate.core.auth.AuthFuncs._

/**
 * Class used to authenticate an api with support for 3 modes:
 * 1. app-key : user needs to supply an api-key to authenticate
 * 2. app-role: application specific role mode ( will validate against roles )
 * 3. key-role: dual authentication mode that will validate key + role.
 *
 * Need to initialize with api-keys
 */
class Auth(private val keys:Option[List[ApiKey]],
              private val callback:Option[(String, Request, String, String ) => Result[Boolean]],
              private val headerApiKeyName:String = "api-key")
  extends ResultSupportIn {

  private val _keyLookup = convertKeys(keys)


  /**
   * whether or not the authorization is valid for the mode, roles supplied.
    *
    * @param mode
   * @param roles
   * @param roleParents
   * @return
   */
  def isAuthorized(cmd:Request, mode:String, roles:String, roleParents:String):Result[Boolean] =
  {
    // CASE 1: no roles ? authorization not applicable
    if (Strings.isNullOrEmpty(roles))
      ok()

    // CASE 2: Guest
    else if (roles == "?")
      ok()

    // CASE 3: App Roles + Key Roles mode
    else if (Strings.isMatch(AuthModeKeyRole, mode)) {
      isKeyRoleValid(cmd, roles, roleParents)
    }
    // CASE 4: App-Role mode
    else if (Strings.isMatch(AuthModeAppRole, mode)) {
      isAppRoleValid(cmd, roles, roleParents)
    }
    // CASE 5: api-key + role
    else if (Strings.isMatch(AuthModeAppKey, mode)) {
      val keyResult = isKeyRoleValid(cmd, roles, roleParents)
      if(!keyResult.success){
        keyResult
      }
      else {
        isAppRoleValid(cmd, roles, roleParents)
      }
    }
    else
      unAuthorized()
  }


  def isKeyRoleValid(cmd:Request, actionRoles:String, parentRoles:String):Result[Boolean] = {
    callback.fold[Result[Boolean]](
      isKeyValid(cmd.opts, _keyLookup, headerApiKeyName, actionRoles, parentRoles))( call => {
        call(AuthModeAppKey, cmd, actionRoles, parentRoles)
      })
  }


  def isAppRoleValid(cmd:Request, actionRoles:String, parentRoles:String):Result[Boolean] = {

    // Check 1: Callback supplied ( so as to avoid subclassing this class )
    if(callback.isDefined) {
      callback.map(c => c(AuthModeAppRole, cmd, actionRoles, parentRoles))
        .getOrElse(unAuthorized())
    }
    else {
      // Get the expected role from either action or possible reference to parent
      val expectedRoles = getReferencedValue(actionRoles, parentRoles)

      // Get the user roles
      val actualRole = getUserRoles(cmd)
      val actualRoles = Strings.splitToMap(actualRole, ',')

      // Now match.
      matchRoles(expectedRoles, actualRoles)
    }
  }


  protected def getUserRoles(cmd:Request):String = {
    ""
  }
}
