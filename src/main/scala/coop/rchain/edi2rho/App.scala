package coop.rchain.edi2rho

import cats.effect._
import dev.profunktor.fs2rabbit.config.Fs2RabbitConfig
import dev.profunktor.fs2rabbit.interpreter.RabbitClient
import dev.profunktor.fs2rabbit.model.QueueName
import fs2.Stream
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import scala.concurrent.duration.DurationInt
import cats.syntax.all._

object App extends IOApp.Simple {
  implicit val async = Async[IO]

  val appConf = ConfigSource.default.load[AppConf].leftMap(println).toOption.get
  val rabbitConfig: Fs2RabbitConfig =
    Fs2RabbitConfig(appConf.rabbitHost,
                    appConf.rabbitPort,
                    appConf.rabbitHost,
                    5.seconds,
                    false,
                    none[String],
                    none[String],
                    true,
                    false,
                    none[Int],
                    10.seconds,
                    true
    )

  val queueName = QueueName(appConf.queueName)

  override def run: IO[Unit] = Stream
    .resource { RabbitClient.default(rabbitConfig).resource }
    .flatMap { rabbit =>
      Stream
        .resource(rabbit.createConnectionChannel)
        .flatMap { implicit channel =>
          Stream.eval(rabbit.createAutoAckConsumer(queueName))
        }
    }
    .flatten
    .map(_.payload)
    .map(Parser.parse)
    .evalMap(NodeAPI.deploy[IO])
    .compile
    .drain
}
