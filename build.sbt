import sbt._
import sbt.Keys._
import Settings._
import Tasks._

lazy val `valamis-lrs-spark` = (project in file("datasources/valamis-lrs-spark"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-spark",
    libraryDependencies ++= Dependencies.spark,
    mappings in(Compile, packageBin) ++= mappings.in(`openlrs-xapi`, Compile, packageBin).value,
    mappings in(Compile, packageBin) ++= mappings.in(`openlrs-util`,   Compile, packageBin).value
  )
  .dependsOn(
    `openlrs-util`,
    `openlrs-xapi`,
    `valamis-lrs-jdbc`,
    `openlrs-test` % Test
  )

lazy val `valamis-lrs-jdbc` = (project in file("datasources/valamis-lrs-jdbc"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-jdbc",
    libraryDependencies ++= Dependencies.database,
    mappings in(Compile, packageBin) ++= mappings.in(`openlrs-xapi`, Compile, packageBin).value,
    mappings in(Compile, packageBin) ++= mappings.in(`openlrs-util`,   Compile, packageBin).value
  )
  .dependsOn(
    `openlrs-util`,
    `openlrs-xapi`,
    `openlrs-test` % Test
  )

//lazy val `valamis-lrs-cassandra` = (project in file("datasources/valamis-lrs-cassandra"))
//  .settings(commonSettings: _*)
//  .settings(disablePublishSettings: _*)
//  .settings(
//    name := "valamis-lrs-cassandra",
//    libraryDependencies ++= Dependencies.cassandra,
//    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-tincan`, Compile, packageBin).value
//  )
//  .dependsOn(
//    `valamis-lrs-tincan`
//  )

lazy val `openlrs-liferay` = (project in file("openlrs-liferay"))
  .settings(commonSettings: _*)
  .settings(publishToNexusSettings: _*)
  .settings(
    name := "openlrs-liferay",
    publishMavenStyle :=  true,
    sqlStatementsTask <<= sqlStatementsGeneration,
    sqlTablesTask     <<= sqlTablesGeneration,
    libraryDependencies ++= Dependencies.liferay62,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan", "valamis-lrs-data-storage", "valamis-lrs-auth"))
    ),
    mappings     in(Compile, packageBin) ++= mappings.in(`valamis-lrs-jdbc`, Compile, packageBin).value,
    artifactName in packageWar := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>  "valamis-lrs-portlet." + artifact.extension }
  )
  .dependsOn(
    `openlrs-util`,
    `openlrs-xapi`,
    `valamis-lrs-api`  % Test,
    `openlrs-test` % Test,
    `valamis-lrs-jdbc`,
    `valamis-lrs-spark`
  )
  .enablePlugins(SbtTwirl)

lazy val `valamis-lrs-api` = (project in file("valamis-lrs-api"))
  .settings(commonSettings: _*)
  .settings(publishToNexusSettings: _*)
  .settings(
    mappings in(Compile, packageBin) ++=
      mappings.in(`openlrs-xapi`, Compile, packageBin).value ++
      mappings.in(`openlrs-util`,   Compile, packageBin).value,
    name := "valamis-lrs-api",
    libraryDependencies ++= Dependencies.api ++ Dependencies.tincan,
    publishMavenStyle := true,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan", "valamis-lrs-util"))
    )
  )
  .dependsOn(
    `openlrs-xapi`
  )

// === Additional project definitions
lazy val `openlrs-xapi` = (project in file("openlrs-xapi"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "openlrs-xapi",
    libraryDependencies ++= Dependencies.tincan
  )
  .dependsOn(
    `openlrs-util`,
    `openlrs-test` % Test
  )

lazy val `openlrs-util` = (project in file("openlrs-util"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "openlrs-util"
  )

lazy val `openlrs-test` = (project in file("openlrs-test"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "openlrs-test",
    libraryDependencies ++= (Dependencies.database ++ Dependencies.testCluster)
  )
  .dependsOn(`openlrs-util`)

lazy val `openlrs` = (project in file("."))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(name := "openlrs")
  .aggregate(
    `openlrs-liferay`,
    `openlrs-xapi`,
    `valamis-lrs-jdbc`,
    `openlrs-test`,
    `openlrs-util`,
    `valamis-lrs-api`
  )
