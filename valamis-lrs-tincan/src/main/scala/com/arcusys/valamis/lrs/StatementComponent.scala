package com.arcusys.valamis.lrs

import scalaz._
import java.util.UUID

import com.arcusys.valamis.lrs.exception.ConflictEntityException
import com.arcusys.valamis.lrs.tincan.Statement


/**
  * Created by itryapitsin on 18/12/15.
  */
trait StatementComponent {
  this: BaseComponent =>

  /**
    * Abstract statements storage
    */
  type StatementStorage = InsertInvoker[Statement, Statement#Id]
    with FindByIdInvoker[Statement, Statement#Id] {

      def findVoidedStatement(id: Statement#Id): Option[Statement]

      def findStatementsByParams(params: StatementQuery): PartialSeq[Statement]

      def contains(s: Statement): Boolean
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
      case q                                  => inv findStatementsByParams q
    }
  } (statementStorage)

  /**
    * Save new [[Statement]] in the LRS
    * @param statement [[Statement]] instance
    * @return Saved [[Some[Statement#Id]] or [[None]] if can't create
    */
  def addStatement(statement: Statement): Statement#Id = Reader { (storage: StatementStorage) =>
    if (storage contains statement)
      throw new ConflictEntityException(s"Statement with key = '${statement.id}' already exist")

    storage add statement

  } (statementStorage)
}
