package coop.rchain.edi2rho

import pureconfig._
import pureconfig.generic.auto._

case class AppConf(
  rabbitHost: String,
  rabbitPort: Int,
  queueName: String
)
