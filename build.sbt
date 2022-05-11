name := "edi2rho"

version := "0.1"

scalaVersion := "2.13.8"

val catsVersion = "2.7.0"
val catsEffectVersion = "3.3.11"

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

libraryDependencies += ("org.typelevel" %% "cats-kernel" % catsVersion).withSources().withJavadoc()
libraryDependencies += ("org.typelevel" %% "cats-core" % catsVersion).withSources().withJavadoc()
libraryDependencies += ("org.typelevel" %% "cats-effect" % catsEffectVersion).withSources().withJavadoc()
libraryDependencies += "dev.profunktor" %% "fs2-rabbit" % "5.0.0"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.17.1"
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
libraryDependencies += "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-language:postfixOps", "-language:higherKinds")
