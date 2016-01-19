package org.openlrs

import org.openlrs.exception.ConflictEntityException
import org.openlrs.xapi.Statement

import scalaz._


/**
  * Created by itryapitsin on 18/12/15.
  */
trait StatementComponent {
  this: BaseComponent =>

  implicit val hasInvoker:                     HasInvoker[Statement]
  implicit val findByVoidedStatementIdInvoker: FindByIdInvoker[Statement, Statement#Id]
  implicit val findByStatementIdInvoker:       FindByParamInvoker[Statement, Statement#Id]
  implicit val selectByStatementQueryInvoker:  SelectByParamInvoker[Statement, StatementQuery]
  implicit val statementInsertInvoker:         InsertInvoker[Statement, Statement#Id]

  /**
    * Abstract statements storage
    */
  class StatementStorage {
    def findById(id: Statement#Id)(implicit inv: FindByIdInvoker[Statement, Statement#Id]) = inv invoke id

    def findVoidedStatement(id: Statement#Id)(implicit inv: FindByIdInvoker[Statement, Statement#Id]) = inv invoke id

    def findStatementsByParams(params: StatementQuery)(implicit inv: SelectByParamInvoker[Statement, StatementQuery]) = inv invoke params

    def has(s: Statement)(implicit inv: HasInvoker[Statement]) = inv invoke s

    def add(s: Statement)(implicit inv: InsertInvoker[Statement, Statement#Id]) = inv invoke s
  }

  val statementStorage: StatementStorage

  /**
    * This method may be called to fetch a multiple Statements.
    * If the statementId or voidedStatementId parameter is specified a single [[Statement]] is returned.
    * @param query
    * @return
    */
  def findStatements(query: StatementQuery): PartialSeq[Statement] = Reader { (inv: StatementStorage) =>
    query match {
      case q if q.statementId isDefined       => inv findById q.statementId toPartialSeq
      case q if q.voidedStatementId isDefined => inv findVoidedStatement q.voidedStatementId toPartialSeq
      case q                                  => inv findStatementsByParams q afterThat { list =>
        list toPartialSeq q.limit == list.length
      }
    }
  } (statementStorage)

  /**
    * Save new [[Statement]] in the LRS
    * @param statement [[Statement]] instance
    * @return Saved [[Some[Statement#Id]] or [[None]] if can't create
    */
  def addStatement(statement: Statement): Statement#Id = Reader { (storage: StatementStorage) =>
    if (storage has statement)
      throw new ConflictEntityException(s"Statement with key = '${statement.id}' already exist")

    storage add statement

  } (statementStorage)
}
