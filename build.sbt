import sbt._
import Keys._

val paradiseVersion = "2.1.0"
val buildSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "org.scalamacros",
  version := "1.0.0",
  scalacOptions ++= Seq(),
  //scalaVersion := "2.11.12",
  scalaVersion := "2.12.10",
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += Resolver.sonatypeRepo("releases"),
  addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)
)

lazy val root: Project = Project("root", file("."),
  settings = buildSettings ++ Seq(
    run := (run in Compile in core).evaluated
  )
) aggregate(macros, core)

lazy val macros: Project = Project("macros", file("macros"),
  settings = buildSettings ++ Seq(
    libraryDependencies += scalaVersion("org.scala-lang" % "scala-reflect" % _).value,
    libraryDependencies ++= (
      if (scalaVersion.value.startsWith("2.10")) List("org.scalamacros" %% "quasiquotes" % paradiseVersion)
      else Nil
    )
  )
)

lazy val core: Project = Project("core", file("core"),
  settings = buildSettings
) dependsOn macros
