//package com.arcusys.valamis.lrs.spark
//
//import java.sql.ResultSet
//import java.util
//import java.util.Properties
//import javax.inject.Inject
//
//import com.arcusys.valamis.lrs._
//import com.arcusys.valamis.lrs.jdbc.QueryContext
//import com.arcusys.valamis.lrs.jdbc.database.utils.TableUtils
//import com.arcusys.valamis.lrs.util.ast.AstHelpers
//import org.apache.spark.sql.SQLContext
//import org.apache.spark.sql.types._
//import org.h2.tools.SimpleResultSet
//
//import scala.collection.{mutable, JavaConverters}
//import scala.language.higherKinds
//import scala.reflect.ClassTag
//import scala.slick.ast.TypeUtil.:@
//import scala.slick.ast._
//import scala.slick.driver._
//import scala.slick.jdbc.{JdbcBackend, JdbcResultConverterDomain}
//import scala.slick.lifted.{Query => Q, _}
//import scala.slick.relational.{CompiledMapping, ResultConverter}
//import scala.util._
//
///**
// * Created by Iliya Tryapitsin on 29.07.15.
//*/
//
//class SparkExecutionContext @Inject() (val driver: JdbcDriver,
//                                       val db:     JdbcBackend#Database,
//                                       val sqlContext: SQLContext)
//  extends QueryContext
//  with AstHelpers
//  with TableUtils {
//
//  import driver.simple._
//
//  @deprecated
//  override def run[T](f: => Rep[T]): T = ???
//
//  @deprecated
//  override def run[T](c: => AppliedCompiledFunction[_, _, Seq[T]]): Seq[T] = ???
//
//  def insertTo [E, U, C[_]] (q: => Q[E,U,C]):  Invoker[U] = new Invoker[U] {
//    def value(v: U): Int = ???
//  }
//
//  def updateTo [E, U, C[_]] (q: => Q[E,U,C]):  Invoker[U] = new Invoker[U] {
//    def value(v: U): Int = ???
//  }
//
//  def delete   [E <: Table[_], U, C[_]] (q: => Q[E,U,C]): Int = Try {
//    val rawQuery = q.deleteStatement
//    runQuery (rawQuery)
//  } match {
//    case Success(_)  =>  1
//    case Failure(ex) => -1
//  }
//
//  def from [E, U] (q: => Q[E, U, Seq]): SelectInvoker[U] = new SelectInvokerImpl(q)
//
//  class SelectInvokerImpl [U] (q: => Q[_, U, Seq]) extends SelectInvoker[U] {
//
//    override def selectFirst: U = {
//      val data  = runQuery (q)
//      val state = driver.profile.queryCompiler run q.toNode
//
//      state tree match {
//        case rsm @ ResultSetMapping(_, _, CompiledMapping(converter, elemType)) :@ CollectionType(cons, el) =>
//
//          val c = converter.asInstanceOf[ResultConverter[JdbcResultConverterDomain, U]]
//
//          data next()
//          c read data
//      }
//    }
//
//    def selectFirstOpt: Option[U] = {
//      val state = driver.profile.queryCompiler run q.toNode
//      val data  = runQuery (q)
//
//      state tree match {
//        case rsm @ ResultSetMapping(_, _, CompiledMapping(converter, elemType)) :@ CollectionType(cons, el) =>
//
//          val c = converter.asInstanceOf[ResultConverter[JdbcResultConverterDomain, U]]
//
//          Try {
//            data next()
//            c read data
//          } match {
//            case Success(r) => r ?
//            case Failure(_) => None
//          }
//      }
//      }
//
//    def select: Seq[U] = {
//      val state = driver.profile.queryCompiler run q.toNode
//      val data  = runQuery (q)
//
//      state tree match {
//        case rsm @ ResultSetMapping(_, _, CompiledMapping(converter, elemType)) :@ CollectionType(cons, el) =>
//
//        val c = converter.asInstanceOf[ResultConverter[JdbcResultConverterDomain, U]]
//        var result = Seq[U]()
//
//        while (data next()) {
//          val o = c read data
//          result = result ++ Seq(o)
//        }
//
//        result
//      }
//    }
//
//    def exists: Boolean = selectFirstOpt isDefined
//
//  }
//
//  protected lazy val url = db.createConnection().getMetaData.getURL
//  protected lazy val identifierQuoteString = db.createConnection().getMetaData.getIdentifierQuoteString
//
//  def runQuery(rawQuery: String) = {
//    val rows = sqlContext sql rawQuery collect
//
//    val resultSet = new SimpleResultSet()
//
//    rows.headOption match {
//      case Some(row) =>
//        val fields = row.schema.fields
//
//        fields foreach { x =>
//          import java.sql.Types
//
//          val tpe = x.dataType match {
//            case tpe: ArrayType   => Types.ARRAY
//            case tpe: BinaryType  => Types.BINARY
//            case tpe: BooleanType => Types.BOOLEAN
//            case tpe: DateType    => Types.DATE
//            case tpe: DecimalType => Types.DECIMAL
//            case tpe: DoubleType  => Types.DOUBLE
//            case tpe: FloatType   => Types.FLOAT
//            case tpe: IntegerType => Types.INTEGER
//            case tpe: LongType    => Types.BIGINT
//            case tpe: NullType    => Types.NULL
//            case tpe: NumericType => Types.NUMERIC
//            case tpe: StringType  => Types.VARCHAR
//            case tpe: TimestampType => Types.TIMESTAMP
//          }
//
//          resultSet.addColumn(x.name, tpe, x.dataType.defaultSize, 0)
//        }
//
//        rows foreach { dt =>
//          var i = 0
//
//          val jdt = new util.ArrayList[Any]()
//
//          fields foreach { field =>
//            val k = dt.get(i)
//            i = i + 1
//            jdt.add(k)
//          }
//
//          val values = jdt.toArray
//
//          resultSet.addRow(values: _*)
//        }
//        resultSet
//
//      case None => resultSet
//    }
//  }
//
//  def runQuery(q: Rep[_]): ResultSet = {
//    import driver.simple._
//
//    val rawQuery = q.selectStatement removeAll identifierQuoteString
//
//    runQuery (rawQuery)
//  }
//
//  implicit class TableQExtension[T <: JdbcProfile#Table[_]](val t: TableQuery[T]) {
//    implicit val dr = driver
//    import dr.simple._
//
//    def dataFrame = {
//      val tableName = driver.quoteTableName(t.baseTableRow.tableNode)
//      val jdbc = sqlContext.read.jdbc(url, tableName, new Properties) as t.baseTableRow.tableName
//      jdbc registerTempTable t.baseTableRow.tableName
//      jdbc
//    }
//
//    def runQuery[R, PU](q: AppliedCompiledFunction[PU, Query[T, R, Seq], Seq[R]])
//                       (implicit clazz: ClassTag[R]): Seq[R] = ??? //runQuery(q.extract)
//  }
//}
