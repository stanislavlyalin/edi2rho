package coop.rchain.edi2rho

import DeployServiceV1.{DeployDataProto, DeployServiceGrpc}
import cats.effect.Sync
import cats.syntax.all._
import coop.rchain.edi2rho.Parser.Parsed
import io.grpc.ManagedChannelBuilder

import scala.concurrent.ExecutionContext.Implicits.global

object NodeAPI {
  def deploy[F[_]: Sync](payload: Parsed): F[Unit] = {
    // TODO: Should be configurable.
    val (host, port) = ("http://localhost/", 40401)
    for {
      channel <- Sync[F].delay(
        ManagedChannelBuilder.forAddress(host, port).usePlaintext().asInstanceOf[ManagedChannelBuilder[_]].build
      )
      client = DeployServiceGrpc.stub(channel)
      // TODO: You should not use a raw protobuf message. Deploy should be signed.
      request = DeployDataProto(term = "Nil")
      reply = client.doDeploy(request)
    } yield reply.onComplete { answer =>
      println(answer.map(_ => "Deploy completed successfully").getOrElse("Deploy execution error"))
    }
  }
}
