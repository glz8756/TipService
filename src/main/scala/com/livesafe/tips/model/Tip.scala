package com.livesafe.tips.model

case class Tip
(
  tipId: String,
  created: Long,
  tipType: TipType,
  message: String,
)

