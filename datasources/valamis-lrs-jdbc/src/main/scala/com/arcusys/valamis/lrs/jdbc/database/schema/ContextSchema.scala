package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait ContextSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  class ContextsTable(tag: Tag) extends UUIDKeyTable[ContextRow](tag: Tag, tblName("contexts")) {
    override def * = (key.?, instructor.?, team.?, revision.?, platform.?, language.?, statementRefId.?, extensions.?) <>(ContextRow.tupled, ContextRow.unapply)

    def instructor = column[ActorRow#Type]("instructor", O.Nullable)
    def team = column[GroupRow#Type]("team", O.Nullable)
    def revision = column[String]("revision", O.Nullable, O.DBType(varCharMax))
    def platform = column[String]("platform", O.Nullable, O.DBType(varCharMax))
    def language = column[String]("language", O.Nullable, O.DBType(varCharMax))
    def statementRefId = column[StatementReferenceRow#Type]("statementRefId", O.Nullable)
    def extensions = column[ExtensionMap]("extensions", O.Nullable, O.DBType(varCharMax))

    def statementRef = foreignKey(fkName("cntxt2stmntRef"), statementRefId, TableQuery[StatementReferenceTable])(x => x.key)
  }

  lazy val contexts = TableQuery[ContextsTable]

}
