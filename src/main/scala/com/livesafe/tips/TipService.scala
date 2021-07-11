package com.livesafe.tips

import com.livesafe.tips.model.Tip
import zio.{Chunk, Has, Task, ZIO, ZLayer}
import zio.macros.accessible

@accessible
trait TipService {
  def allTips: Task[List[Tip]]

  def getTip(id: String): Task[Option[Tip]]

  def save(newTip: Tip): Task[String]
}

case class TipServiceLive(tipRepository: Repository[Tip]) extends TipService {
  override def allTips: Task[List[Tip]] = tipRepository.getAll.map(_.sortWith(_.created > _.created))

  override def getTip(id: String): Task[Option[Tip]] =
    tipRepository
      .get(id)
      .map(Some(_))
      .orElseSucceed(None)

  override def save(newTip: Tip): Task[String] =
    tipRepository.save(newTip)
}

object TipService {

  val live: ZLayer[Has[Repository[Tip]], Throwable, Has[TipService]] = {
    for {
      _ <- loadData
      repo <- ZIO.service[Repository[Tip]]
    } yield TipServiceLive(repo)
  }.toLayer[TipService]

  private lazy val cachedTips: ZIO[Any, Throwable, Chunk[Tip]] =
    LegacyTipsService.fetchLegacy1Tips
      .zipWithPar(LegacyTipsService.fetchLegacy2Tips)(_ ++ _)


  private lazy val loadData: ZIO[Has[Repository[Tip]], Throwable, Unit] =
    cachedTips.flatMap { tips =>
      ZIO.foreach_(tips)(Repository.save[Tip])
    }

}


