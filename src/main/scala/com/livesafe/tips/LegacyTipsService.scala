package com.livesafe.tips

import com.livesafe.tips.model._
import zio.blocking.Blocking
import zio.stream.{ZStream, ZTransducer}
import zio.{Chunk, Task, UIO}
import java.time.{LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

object LegacyTipsService {

  def getTimestamp(in: String): Long = {
    val dttm = LocalDateTime.parse(in, DateTimeFormatter.ISO_DATE_TIME)
    dttm.atZone(ZoneId.of("UTC")).toInstant.getEpochSecond
  }

  def getFileLine(path: String): UIO[Chunk[String]] =
    ZStream
      .fromResource(path)
      .transduce(ZTransducer.utf8Decode >>> ZTransducer.splitLines)
      .runCollect
      .provideLayer(Blocking.live)
      .orDie

  def fetchLegacy1Tips: Task[Chunk[Tip]] =
    getFileLine("legacy-1-tips.csv")
      .map {
        _.drop(1)
          .map(_.split(","))
          .filter(_.length == 4)
          .map { t => Tip(t(0), t(1).toLong, TipType.fromString(t(2)), t(3)) }
      }

  def fetchLegacy2Tips: Task[Chunk[Tip]] =
    getFileLine("legacy-1-tips.csv")
      .map {
        _.drop(1)
          .map(_.split(","))
          .filter(x => x.length == 4 && x(2).matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+Z$"))
          .map { t => Tip(t(0), t(1).toLong, TipType.fromString(t(2)), t(3)) }
      }
}
