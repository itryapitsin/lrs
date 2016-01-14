package com.arcusys.valamis.lrs

/**
  * Created by iliyatryapitsin on 18/12/15.
  */
trait BaseComponent {

  trait Rep[T]

  trait InsertInvoker[T, Id] extends Rep[T] {
    def add (t: T): Id
  }

  trait UpdateInvoker[T] extends Rep[T] {
    def update (t: T): Int
  }

  trait DeleteInvoker[T] extends {
    def delete (t: T): Int
  }

  trait FindByParamInvoker[T, Param] extends Rep[T] {
    def findBy (param: Param): Option[T]
  }

  trait FindByIdInvoker[T, Id] extends Rep[T] {
    def findById (id: Id): Option[T]
  }

  trait HasInvoker[T] extends Rep[T] {
    def has (t: T): Boolean
  }
}
