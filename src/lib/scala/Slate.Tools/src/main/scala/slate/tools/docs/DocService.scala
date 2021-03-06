/**
  * <slate_header>
  * url: www.slatekit.com
  * git: www.github.com/code-helix/slatekit
  * org: www.codehelix.co
  * author: Kishore Reddy
  * copyright: 2016 CodeHelix Solutions Inc.
  * license: refer to website and/or github
  * about: A Scala utility library, tool-kit and server backend.
  * mantra: Simplicity above all else
  * </slate_header>
  */

package slate.tools.docs

import java.io.File
import java.nio.file.Path

import slate.common._
import slate.common.console.ConsoleWriter
import slate.common.results.ResultSupportIn

import scala.collection.immutable.List
import scala.collection.mutable.Map

class DocService(val _rootdir:String, val _outputDir:String, val templatePath:String) extends ResultSupportIn {

  private val _templatePath = new File(_rootdir, templatePath).toString
  private var _template = ""
  private val _writer = new ConsoleWriter()


  private val _docs = List[Doc](
     new Doc("Args"         ,  "slate.common.args.Args"                    , "1.4.0", "Example_Args"             , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "A lexical command line argument parser for command line parsing and support specifying routes / method calls")
    ,new Doc("Config"       ,  "slate.common.conf.Config"                  , "1.4.0", "Example_Config"           , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "Thin wrapper over typesafe config with decryption support, uri loading, and mapping of database connections and api keys")
    ,new Doc("Console"      ,  "slate.common.console.Console"              , "1.4.0", "Example_Console"          , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "Enhanced printing to console with support for semantic writing like title, subtitle, url, error, etc with colors")
    ,new Doc("Csv"          ,  "slate.core.csv.Csv"                        , "1.4.0", "Example_Csv"              , false , false, false, "utils", "", "slate.common.jar"  , ""                    , "Csv parser with additional services like auto-mapping and serialization")
    ,new Doc("Data"         ,  "slate.common.databases.Db"                 , "1.4.0", "Example_Database"         , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "Database access utilty to query and manage data using JDBC for MySql. Other database support coming later.")
    ,new Doc("DateTime"     ,  "slate.common.DateTime"                     , "1.4.0", "Example_DateTime"         , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "DataTime wrapper around Java 8 LocalDateTime providing a simplified interface, some convenience, extra features.")
    ,new Doc("Encrypt"      ,  "slate.common.encrypt.Encryptor"            , "1.4.0", "Example_Encryptor"        , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "Encryption using AES")
    ,new Doc("Env"          ,  "slate.common.envs.Env"                     , "1.4.0", "Example_Env"              , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "Environment selector and validator for environments such as (local, dev, qa, stg, prod) )")
    ,new Doc("Folders"      ,  "slate.common.info.Folders"                 , "1.4.0", "Example_Folders"          , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "Standardized application folder setup; includes conf, cache, inputs, logs, outputs")
    ,new Doc("Info"         ,  "slate.common.info._"                       , "1.4.0", "Example_Info"             , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "Get/Set useful diagnostics about the system, language runtime, application and more")
    ,new Doc("Lex"          ,  "slate.common.lex.Lexer"                    , "1.4.0", "Example_Lexer"            , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "Lexer for parsing text into tokens")
    ,new Doc("Logger"       ,  "slate.common.logging.Logger"               , "1.4.0", "Example_Logger"           , true  , false, true , "utils", "", "slate.common.jar"  , ""                    , "A simple logger with extensibility for using other 3rd party loggers")
    ,new Doc("Model"        ,  "slate.common.Model"                        , "1.4.0", "Example_Model"            , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "Allows construction of model schema with fields for code-generation. Also used in the ORM mapper" )
    ,new Doc("Mapper"       ,  "slate.common.mapper.Mapper"                , "1.4.0", "Example_Mapper"           , false , false, true , "utils", "", "slate.common.jar"  , ""                    , "A simple auth component for desktop/local and web/concurrent apps" )
    ,new Doc("Notifications",  "slate.core."                               , "1.4.0", "Example_Notifications"    , false , false, false, "infra", "", "slate.common.jar"  , ""                    , "Push notifications for Mobile" )
    ,new Doc("Query"        ,  "slate.common.query.Query"                  , "1.4.0", "Example_Query"            , false , false, true , "utils", "", "slate.common.jar"  , ""                    , "Query pattern used for specifying search and selection criteria" )
    ,new Doc("Queue"        ,  "slate.common.queues.QueueSource"           , "1.4.0", "Example_Queue"            , false , false, true , "infra", "", "slate.common.jar"  , ""                    , "Queue implementation and interfaces used for Queue abstractions over Amazon SQS" )
    ,new Doc("Random"       ,  "slate.common.Random"                       , "1.4.0", "Example_Random"           , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "A random generator for strings, guids, numbers, alpha-numeric, and alpha-numeric-symbols for various lengths" )
    ,new Doc("Results"      ,  "slate.common.Result"                       , "1.4.0", "Example_Results"          , true  , false, false , "utils", "", "slate.common.jar"  , ""                    , "A monad that wraps a value with status codes, message, and other fields. Support failure, success branchs and supports http status codes." )
    ,new Doc("Timer"        ,  "slate.common.Timer"                        , "1.4.0", "Example_Timer"            , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "A timer to benchmark time/duration of code blocks")
    ,new Doc("Todo"         ,  "slate.common.Todo"                         , "1.4.0", "Example_Todo"             , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "A programmatic approach to marking and tagging code that is strongly typed and consistent")
    ,new Doc("Serialization",  "slate.common.serialization.Serializer"     , "1.4.0", "Example_Serialization"    , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "Serializers for classes / fields that support serializing them as CSV, HoCon format and JSON ( coming soon )")
    ,new Doc("Templates"    ,  "slate.common.templates.Templates"          , "1.4.0", "Example_Templates"        , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "A micro template system for processing text with variables, useful for generating dynamic emails/messages.")
    ,new Doc("Validations"  ,  "slate.common.validation.ValidationFuncs"   , "1.4.0", "Example_Validation"       , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "A set of validation related components, simple validation checks, RegEx checks, error collection and custom validators")
    ,new Doc("Utils"        ,  "slate.common.console.ConsoleWriter"        , "1.4.0", "Example_Utils"            , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "Various utilities available in the Slate library")
    ,new Doc("Reflect"      ,  "slate.common.Reflector"                    , "1.4.0", "Example_Reflect"          , true  , false, false, "utils", "", "slate.common.jar"  , ""                    , "Reflection helper for Scala to create instances, get methods, fields, annotations and more" )
    ,new Doc("Orm-Model"    ,  "slate.common.Model"                        , "1.4.0", "Example_Model"            , true  , false, false, "orm"  , "", "slate.common.jar"  , "com"                 , "A model schema builder")
    ,new Doc("Orm-Entity"   ,  "slate.common.entities.Entity"              , "1.4.0", "Example_Entities"         , true  , false, false, "orm"  , "", "slate.entities.jar", "com"                 , "A base class for persistent domain entities")
    ,new Doc("Orm-Mapper"   ,  "slate.common.entities.EntityMapper"        , "1.4.0", "Example_Mapper"           , true  , false, false, "orm"  , "", "slate.entities.jar", "com"                 , "A mapper that converts a entity to a sql create/updates")
    ,new Doc("Orm-Repo"     ,  "slate.common.entities.EntityRepo"          , "1.4.0", "Example_Entities_Repo"    , true  , false, false, "orm"  , "", "slate.entities.jar", "com"                 , "A repository pattern for entity/model CRUD operations")
    ,new Doc("Orm-Service"  ,  "slate.common.entities.EntityService"       , "1.4.0", "Example_Entities_Service" , true  , false, false, "orm"  , "", "slate.entities.jar", "com"                 , "A service pattern for entity/model CRUD + business operations")
    ,new Doc("Orm-Setup"    ,  "slate.common.entities.Entities"            , "1.4.0", "Example_Entities_Reg"     , true  , false, false, "orm"  , "", "slate.entities.jar", "com"                 , "A registration system for entities and their corresponding repository/service impelementations")
    ,new Doc("Api"          ,  "slate.core.apis.ApiContainer"              , "1.4.0", "Example_Api"              , true  , false, true , "infra", "", "slate.core.jar"    , "com"                 , "An API Container to host protocol agnostic apis to run on the command line or web")
    ,new Doc("App"          ,  "slate.core.app.AppProcess"                 , "1.4.0", "Example_App"              , true  , false, true , "infra", "", "slate.core.jar"    , "com"                 , "A base application with support for command line args, environment selection, configs, encryption, logging, diagnostics and more")
    ,new Doc("Auth"         ,  "slate.core.auth.Auth"                      , "1.4.0", "Example_Auth"             , true  , false, false, "utils", "", "slate.core.jar"    , "com"                 , "A simple authentication component to check current user role and permissions")
    ,new Doc("Cache"        ,  "slate.core.cache.Cache"                    , "1.4.0", "Example_Cache"            , true  , false, false, "infra", "", "slate.core.jar"    , "com"                 , "Light-weight cache to load, store, and refresh data, with support for metrics and time-stamps. Default in-memory implementation available")
    ,new Doc("Ctx"          ,  "slate.core.common.AppContext"              , "1.4.0", "Example_Context"          , true  , false, false, "infra", "", "slate.core.jar"    , "com"                 , "An application context to contain common dependencies such as configs, logger, encryptor, etc, to be accessible to other components")
    ,new Doc("Cmd"          ,  "slate.core.cmds.Cmd"                       , "1.4.0", "Example_Command"          , true  , false, false, "infra", "", "slate.core.jar"    , "com"                 , "A variation to the command pattern to support ad-hoc execution of code, with support for metrics and time-stamps")
    ,new Doc("Email"        ,  "slate.core.sms.EmailService"               , "1.4.0", "Example_Email"            , true  , false, false, "infra", "", "slate.core.jar"    , "com"                 , "An Email service to send emails with support for templates using SendGrid as the default implementation")
    ,new Doc("Shell"        ,  "slate.core.shell.ShellService"             , "1.4.0", "Example_Shell"            , true  , false, false, "infra", "", "slate.core.jar"    , "com"                 , "A CLI ( Command Line Interface ) you can extend / hook into to run handle user. Can also be used to execute your APIs")
    ,new Doc("Sms"          ,  "slate.core.sms.SmsService"                 , "1.4.0", "Example_Sms"              , true  , false, false, "infra", "", "slate.core.jar"    , "com"                 , "An Sms ( Text message ) service to send text messages to mobile phones for confirmation codes and invites.")
    ,new Doc("Tasks"        ,  "slate.core.tasks.Task"                     , "1.4.0", "Example_Task"             , true  , false, false, "infra", "", "slate.core.jar"    , "com"                 , "A robust Task/Job implementation that can be hooked up with a Queue")
    ,new Doc("AWS-S3"       ,  "slate.cloud.aws.AwsCloudFiles"             , "1.4.0", "Example_Aws_S3"           , true  , false, false, "infra", "", "slate.cloud.jar"   , "com,core"            , "Abstraction layer on cloud file storage to Amazon S3"             )
    ,new Doc("AWS-SQS"      ,  "slate.cloud.aws.AwsCloudQueue"             , "1.4.0", "Example_Aws_Sqs"          , true  , true , false, "infra", "", "slate.cloud.jar"   , "com,core"            , "Abstraction layer on message queues using Amazon SQS"              )
    ,new Doc("ext-Users"    ,  "slate.ext.users.User"                      , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
    ,new Doc("ext-Devices"  ,  "slate.ext.devices.Device"                  , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
    ,new Doc("ext-Reg"      ,  "slate.ext.reg.RegService"                  , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
    ,new Doc("ext-Settings" ,  "slate.ext.settings.Settings"               , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
    ,new Doc("ext-Tasks"    ,  "slate.ext.tasks.Tasks"                     , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
    ,new Doc("ext-Invites"  ,  "slate.ext.invites.Invite"                  , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
    ,new Doc("ext-Logs"     ,  "slate.ext.logs.Log"                        , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
    ,new Doc("ext-Status"   ,  "slate.ext.status.Status"                   , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
    ,new Doc("ext-Audits"   ,  "slate.ext.audits.Audits"                   , "1.4.0", "Example_Ext_Users"        , false , true , false, "feat" , "", "slate.ext.jar"     , "com,ent,core,cloud"  , "Feature to create and manage users"              )
  )


  def process():Result[String] =
  {
    val data = Map[String,String]()
    val keys = _docs.map( d => d.name).toList
    val maxLength = Strings.maxLength(keys) + 3
    var pos = 1
    for(doc <- _docs)
    {
      val number = Strings.pad(pos.toString, 2)
      val displayName = Strings.pad(doc.name, maxLength)
      _writer.highlight(s"$number. $displayName :", false)
      init(doc, data)
      val parseResult = if(doc.available) parse(doc, data) else fillComingSoon(doc, data)
      val formatResult = fill(doc, data)
      val filePath = generate(doc, data, formatResult)
      if(doc.available && doc.readme) {
        generateReadMe(doc, data, formatResult)
      }
      _writer.url(filePath.asInstanceOf[String], true)
      pos = pos + 1
    }
    success(_outputDir, Some("generated docs to " + _outputDir))
  }


  private def init(doc:Doc, data:Map[String,String]): Unit =
  {
    _template = Files.readAllText(_templatePath)
    data("output")      = ""
    data("layout")      = doc.layout
    data("name")        = doc.name
    data("namelower")   = doc.name.toLowerCase
    data("desc")        = doc.desc
    data("date")        = DateTime.now().toString()
    data("version")     = doc.version
    data("jar")         = doc.jar
    data("dependencies")= doc.dependsOn()
    data("namespace")   = doc.namespace
    data("source")      = doc.source
    data("sourceFolder")= doc.sourceFolder()
    data("example")     = doc.example
    data("setup")      = ""
    data("examplefile") = DocHelper.buildComponentExamplePathLink(doc)
  }


  private def parse(doc:Doc, data:Map[String,String]): Result[Boolean] =
  {
    val filePath = DocHelper.buildComponentExamplePath(_rootdir, doc )
    val content = Files.readAllText(filePath)
    val parser = new StringParser(content)

    parser.moveTo("<doc:import_required>", ensure = true)
    .saveUntil("//</doc:import_required>", name = "import_required", ensure = true)

    .moveTo("<doc:import_examples>", ensure = true)
    .saveUntil("//</doc:import_examples>", name = "import_examples", ensure = true)

    .moveTo("<doc:setup>", ensure = false)
    .saveUntil("//</doc:setup>", name = "setup", ensure = false)

    .moveTo("<doc:examples>", ensure = true)
    .saveUntil("//</doc:examples>", name = "examples", ensure = true)

    .moveTo("<doc:output>", ensure = false)
    .saveUntil("//</doc:output>", name = "output", ensure = false)

    val extractedData = parser.extracts
    for(entry <- extractedData)
    {
      data(entry._1) = entry._2
    }
    ok()
  }


  private def fillComingSoon(doc:Doc, data:Map[String,String]): Result[Boolean] =
  {
    data("import_required") = Strings.newline() + "coming soon"
    data("import_examples") = Strings.newline() + "coming soon"
    data("setup") = "-"
    data("examples") = "coming soon"
    data("output") = ""
    ok()
  }


  private def fill(doc:Doc, data:Map[String,String]):Result[String] =
  {
    var template = _template
    template = replace(template, "layout"                     , data, "layout"            )
    template = replace(template, "name"	                      , data, "name"	            )
    template = replace(template, "namelower"	                , data, "namelower"	        )
    template = replace(template, "desc"	                      , data, "desc"	            )
    template = replace(template, "date"	                      , data, "date"	            )
    template = replace(template, "version"	                  , data, "version"	          )
    template = replace(template, "jar"                        , data, "jar"	              )
    template = replace(template, "namespace"                  , data, "namespace"	        )
    template = replace(template, "source"	                    , data, "source"	          )
    template = replace(template, "sourceFolder"	              , data, "sourceFolder"      )
    template = replace(template, "example"	                  , data, "example"	          )
    template = replace(template, "dependencies"	              , data, "dependencies"	    )
    template = replace(template, "examplefile"                , data, "examplefile"	      )

    //https://github.com/kishorereddy/blend-server/blob/master/src/apps/scala/slate-examples/src/main/scala/slate/examples/Example_Args.scala

    template = replace(template, DocConstants.header          , data, "header"	          )
    template = replace(template, DocConstants.import_required , data, "import_required"	  )
    template = replace(template, DocConstants.import_optional , data, "import_optional"	  )
    template = replace(template, DocConstants.import_examples , data, "import_examples"	  )
    template = replace(template, DocConstants.depends         , data, "depends"	          )
    template = replace(template, DocConstants.setup           , data, "setup"	            , true)
    template = replace(template, DocConstants.examples        , data, "examples"	        )
    template = replace(template, DocConstants.notes           , data, "notes"             )
    template = replaceWithSection("Output", template, DocConstants.output , data, "output" )
    success(template)
  }


  private def generate(doc:Doc, data:Any, result:Result[String]): String =
  {
    val fileName = "mod-" + doc.name.toLowerCase + ".md"
    val outputPath = DocHelper.buildDistDocComponentPath(_rootdir, _outputDir, doc)
    val filePath = outputPath + "\\" + fileName
    val content = result.get
    Files.writeAllText(filePath,content)
    filePath
  }


  private def generateReadMe(doc:Doc, data:Any, result:Result[String]): Unit =
  {
    val fileName = if (doc.multi) "Readme_" + doc.name + ".md" else "Readme.md"
    val outputPath = DocHelper.buildComponentFolder(_rootdir, doc)
    val filePath = outputPath + "\\" + fileName
    val content = result.get
    Files.writeAllText(filePath,content)
  }


  private def replace(template:String, name:String, data:Map[String, String], key:String,
                      enableNotApplicableIfEmptyData:Boolean = false):String =
  {
    if(!data.contains(key))
    {
      return template.replaceAllLiterally("@{" + name + "}", "n/a")
    }

    val replacement = data(key)
    if(Strings.isNullOrEmpty(replacement) && enableNotApplicableIfEmptyData){
      return template.replaceAllLiterally("@{" + name + "}", "n/a")
    }
    val result = template.replaceAllLiterally("@{" + name + "}", replacement)
    result
  }


  private def replaceWithSection(sectionName:String, template:String,
                                 name:String, data:Map[String, String], key:String):String =
  {
    if(!data.contains(key)) return replaceItem(template, name)

    val replacement = data(key)
    if(Strings.isNullOrEmpty(replacement)) return replaceItem(template, name)

    val section = Strings.newline() + s"## $sectionName" + Strings.newline() + replacement
    val result = template.replaceAllLiterally("@{" + name + "}", section)
    result
  }


  private def replaceItem(template:String, name:String):String =
  {
    template.replaceAllLiterally("@{" + name + "}", "")
  }
}
