package org.openlrs

/**
  * Created by iliyatryapitsin on 18/12/15.
  */
trait BaseComponent {

  trait Invoker[-In, +Out] {
    def invoke(t: In): Out
  }

  trait Rep[T]

  trait Filter[Filter, T] extends Invoker[Filter, T]

  trait

  trait InsertInvoker[T, Id] extends Invoker[T, Id]

  trait UpdateInvoker[T] extends Invoker[T, Int]

  trait DeleteInvoker[T] extends Invoker[T, Int]

  trait FindByParamInvoker[T, Param] extends Invoker[Param, Option[T]]

  trait SelectByParamInvoker[T, Param] extends Invoker[Param, Seq[T]]

  trait FindByIdInvoker[T, Id] extends Invoker[Id, Option[T]]

  trait HasInvoker[T] extends Invoker[T, Boolean]
}
