package com.livesafe.tips


import zio._
import zio.macros.accessible

/**
 * Asynchronous in-memory repository. Meant to mock / imitate a true database connection
 * Subclass this with a concrete data type to use.
 * Example usage:
 * {{{
 *   case class Foo(a: Int, id: String)
 *   class FooRepository extends Repository[Foo](_.id)
 * }}}
 *
 * @param extractId A function which, given an instance of [[A]], will extract an ID for A as a String
 * @tparam A The subject of this repository
 */
@accessible
trait Repository[A <: AnyRef] {
  def save(a: A): Task[String]
  def get(id: String): Task[A]
  def getAll: Task[List[A]]
}

object Repository {
  def live[A <: AnyRef: Tag](extractId: A => String): ULayer[Has[Repository[A]]] = {
    for {
      ref <- Ref.make(Map.empty[String, A])
    }yield RepositoryLive(ref, extractId)
  }.toLayer[Repository[A]]
}

case class RepositoryLive[A <: AnyRef]
(
  ref: Ref[Map[String, A]],
  extractId: A => String
) extends Repository[A] {

  override def save(a: A): Task[String] = {
    val id = extractId(a)
    ref.update(_.updated(id, a)).as(id)
  }
  override def get(id: String): Task[A] = ref.get.flatMap(map => Task(map(id)))
  override def getAll: Task[List[A]] =
    ref.get.map(_.values.toList)
}

