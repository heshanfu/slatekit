package test.meta

import org.junit.Assert
import org.junit.Test
import slatekit.common.DateTime
import slatekit.common.UniqueId
import slatekit.meta.Reflector
import slatekit.meta.models.Model
import slatekit.meta.models.ModelMapper
import test.setup.*
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createType


class ModelTests {


    @Test fun can_build_simple_model_from_reflection(){
        val model = ModelMapper.loadSchema(AuthorR::class, AuthorR::id.name)
        ensureAuthorModel(model)
    }


    @Test fun can_build_simple_model_from_schema(){
        val model = loadSchemaSpecification()
        ensureAuthorModel(model)
    }


    @Test fun can_build_complex_model_from_schema(){
        val model = ModelMapper.loadSchema(UserWithAddress::class, UserWithAddress::id.name)
        val addrProp = model.fields.find { it.name == "addr" }
        assert( addrProp != null)
        assert( addrProp!!.model != null)
        assert( addrProp!!.model?.dataType == Address::class)
    }


    @Test fun can_build_simple_model_with_nullable(){
        val model = ModelMapper.loadSchema(AuthorRNull::class, AuthorRNull::id.name)
        assert(model.hasId)
        assert(model.any)
        ensureField(model, "id"        , false, Long::class       )
        ensureField(model, "email"     , false, String::class     )
        ensureField(model, "isActive"  , false, Boolean::class    )
        ensureField(model, "age"       , false, Int::class        )
        ensureField(model, "status"    , false, StatusEnum::class )
        ensureField(model, "salary"    , false, Double::class     )
        ensureField(model, "createdAt" , false, DateTime::class   )
    }


    @Test fun can_load_nullable(){
        data class Test(val str1:String, val str2:String? = null)

        val a1Type  = Test::str1.returnType
        val a1Cls   = Test::str1.returnType.classifier as KClass<*>
        val a1Type2 = Test::str1.returnType
        val a1Type3 =(Test::str1.returnType.classifier as KClass<*>).createType()


        val a2Type  = Test::str2.returnType
        val a2Cls   = Test::str2.returnType.classifier as KClass<*>
        val a2Type2 = Test::str2.returnType
        val a2Type3 =(Test::str2.returnType.classifier as KClass<*>).createType()

        val props = Reflector.getProperties(Test::class)

        val prop1 = props.get(0)
        val prop1Type = prop1.returnType

        val prop2 = props.get(1)
        val prop2Type = prop2.returnType

        Assert.assertEquals(a1Type, a1Type2)
        Assert.assertEquals(a1Type, a1Type3)
        Assert.assertEquals(a1Type, prop1Type)
        Assert.assertEquals(a1Type.isMarkedNullable, a1Type3.isMarkedNullable)
        Assert.assertEquals(a1Type.isMarkedNullable, prop1Type.isMarkedNullable)

        Assert.assertEquals(a2Type, a2Type2)
        Assert.assertEquals(a2Type, prop2Type)
        //Assert.assertEquals(a2Type, a2Type3)
        Assert.assertEquals(a2Type.isMarkedNullable, prop2Type.isMarkedNullable)

    }


    fun ensureField(model: Model, name:String, required:Boolean, tpe: KClass<*>):Unit {
        val field = model.fields.find { it.name == name }
        assert(field != null)
        assert(field!!.isRequired == required)
        assert(field!!.dataCls == tpe)
    }


    fun ensureAuthorModel(model: Model):Unit {

        assert(model.hasId)
        assert(model.any)
        ensureField(model, "id"        , true, Long::class     )
        ensureField(model, "createdAt" , true, DateTime::class )
        ensureField(model, "createdBy" , true, Long::class     )
        ensureField(model, "updatedAt" , true, DateTime::class )
        ensureField(model, "updatedBy" , true, Long::class     )
        ensureField(model, "uuid"      , true, String::class   )
        ensureField(model, "email"     , true, String::class   )
        ensureField(model, "isActive"  , true, Boolean::class  )
        ensureField(model, "age"       , true, Int::class      )
        ensureField(model, "salary"    , true, Double::class   )
        ensureField(model, "uid"       , true, UUID::class     )
        ensureField(model, "shardId"   , true, UniqueId::class )
    }


    fun loadSchemaSpecification(): Model {
        val model = Model(AuthorR::class)
                .addId( AuthorR::id, true)
                .add( AuthorR::createdAt )
                .add( AuthorR::createdBy )
                .add( AuthorR::updatedAt )
                .add( AuthorR::updatedBy )
                .add( AuthorR::uuid  )
                .add( AuthorR::email     )
                .add( AuthorR::isActive  )
                .add( AuthorR::age       )
                .add( AuthorR::salary    )
                .add( AuthorR::uid       )
                .add( AuthorR::shardId   )
        return model
    }
}
