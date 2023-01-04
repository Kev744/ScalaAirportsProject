ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

ThisBuild / libraryDependencies ++= Seq("org.scala-lang.modules" %% "scala-swing" % "3.0.0","org.scalikejdbc" %% "scalikejdbc" % "4.0.0","com.h2database"% "h2"% "2.1.214", "ch.qos.logback" % "logback-classic" % "1.4.5",   "org.http4s"      %% "http4s-blaze-server" % "1.0.0-M21", "org.http4s"      %% "http4s-circe"        % "1.0.0-M21", "org.http4s"      %% "http4s-dsl"          % "1.0.0-M21", "io.circe"        %% "circe-generic"       % "0.14.3", "org.scalikejdbc" %% "scalikejdbc-config" % "4.0.0")


lazy val root = (project in file("."))
  .settings(
    name := "untitled"

)
