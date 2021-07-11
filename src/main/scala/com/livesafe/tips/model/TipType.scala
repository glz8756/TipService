package com.livesafe.tips.model

import io.circe.{Encoder, Json}

abstract sealed class TipType

object TipType {

  case object Disturbance extends TipType

  case object SuspiciousAct extends TipType

  case object Smell extends TipType

  case object Arson extends TipType

  case object Violence extends TipType

  def fromString(value: String): TipType = value match {
    case "Disturbance" | "disturbance" => Disturbance
    case "Suspicious" | "SuspiciousActivity" | "suspicious" | "suspiciousActivity" => SuspiciousAct
    case "Smell" | "smell" => Smell
    case "Arson" | "arson" => Arson
    case "Violence" | "violence" => Violence
  }

  implicit val encodeTipType: Encoder[TipType] = new Encoder[TipType] {
    override def apply(ty: TipType): Json = ty match {
      case Smell => Json.fromString("Smell")
      case Disturbance => Json.fromString("Disturbance")
      case SuspiciousAct => Json.fromString("SuspiciousAct")
      case Arson => Json.fromString("Arson")
      case Violence => Json.fromString("Violence")
    }
  }
}


