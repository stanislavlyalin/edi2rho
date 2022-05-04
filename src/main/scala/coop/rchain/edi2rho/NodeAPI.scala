package coop.rchain.edi2rho

import cats.effect.Sync
import coop.rchain.edi2rho.Parser.Parsed

object NodeAPI {
  def deploy[F[_]: Sync](payload: Parsed): F[Unit] = ???
}
