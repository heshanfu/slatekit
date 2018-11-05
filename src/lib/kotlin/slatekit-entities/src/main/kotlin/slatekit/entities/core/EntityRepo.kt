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

package slatekit.entities.core

import slatekit.common.naming.Namer
import slatekit.common.encrypt.Encryptor
import slatekit.meta.models.Model
import slatekit.common.query.IQuery
import slatekit.common.query.Query
import slatekit.entities.Consts.idCol
import slatekit.entities.databases.vendors.MySqlConverter
import slatekit.meta.models.ModelMapper
import kotlin.reflect.KClass

/**
 * Base Entity repository using generics with support for all the CRUD methods.
 * NOTE: This is basically a GenericRepository implementation
 * @param entityType : The data type of the entity/model
 * @param entityIdType : The data type of the primary key/identity field
 * @param entityMapper : The entity mapper that maps to/from entities / records
 * @param nameOfTable : The name of the table ( defaults to entity name )
 * @tparam T
 */
abstract class EntityRepo<TEntity>(
    entityType: KClass<*>,
    entityIdType: KClass<*>? = null,
    entityMapper: EntityMapper? = null,
    nameOfTable: String? = null,
    encryptor: Encryptor? = null,
    namer: Namer? = null,
    encodedChar: Char = '`',
    queryBuilder: (() -> Query)? = null
) : EntityRepoWithId<Long, TEntity>(
    entityType,
    entityIdType,
    entityMapper,
    nameOfTable,
    encryptor,
    namer,
    encodedChar,
    queryBuilder
) where TEntity : Entity
