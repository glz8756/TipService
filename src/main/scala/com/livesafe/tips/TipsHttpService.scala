package com.livesafe.tips

import com.livesafe.tips.model.Tip
import zhttp.http._
import zhttp.service._
import zio._
import zio.magic._
import io.circe.syntax.EncoderOps
import io.circe.generic.auto._


object TipsHttpService extends App {
  val app: HttpApp[Has[TipService], Throwable]=
    HttpApp.collectM{
      case Method.GET -> Root / "tips" / tipId =>
        TipService.getTip(tipId)
          .map{
            case Some(tip) => Response.jsonString(tip.asJson.toString)
            case None  => Response.fromHttpError(HttpError.InternalServerError(s"No tip with ID $tipId"))
          }

      case Method.GET -> Root / "tips" =>
        TipService.allTips
          .map { allTips =>
            Response.jsonString(allTips.asJson.toString)
          }
    }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode]=
    Server
      .start(8080, app)
      .inject(
        Repository.live[Tip](_.tipId),
        TipService.live
      ).exitCode
}
