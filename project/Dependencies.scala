import sbt._

object Version {

  val scala                = "2.11.7"
  val scalaAsync           = "0.9.4"
  val scalaz               = "7.2.0"
  val slick                = "2.1.0"
  val slickDrivers         = "2.1.0"
  val config               = "1.2.1"
  val json4s               = "3.2.11"
  val scalatest            = "2.2.3"
  val scalacheck           = "1.12.1"
  val slf4j                = "1.6.4"
  val log4jExt             = "1.1"
  val httpClient           = "4.4"
  val httpMime             = "4.4"
  val portlet              = "2.0"
  val liferayPortal62      = "6.2.2"
  val akka                 = "2.3.9"
  val websocket            = "1.1"

  val jodaConvert          = "1.7"
  val jodaTime             = "2.8.1"
  val scalaGuice           = "4.0.0"
  val guice                = "4.0"
  val mockito              = "1.10.17"
  val junit                = "4.12"
  val logback              = "1.1.3"
  val servletApi           = "3.0.1"
  val commonsValidator     = "1.4.1"
  val commonsLang          = "2.6"
  val oauth                = "20100527"
  val fileUpload           = "1.3.1"
  val valamis              = "2.6-SNAPSHOT"
  val slickMigrations      = "2.1.0.2"
  val commonsIO            = "2.2"

  // Db
  val h2                   = "1.3.170"
  val hsql                 = "0.0.15"
  val mysql                = "5.1.34"
  val postgres             = "9.4-1201-jdbc41"

  // Spark
  val spark                = "1.4.1"
  val sparkJetty           = "8.1.14.v20131031"
  val sparkDateTime        = "0.0.2"

  // Message Server
  val kafka                = "0.8.2.1"
  val zookeeper            = "3.4.6"

  val metricsScala         = "3.5.1_a2.3"
  val metrics              = "3.1.2"

  // Phantom Cassandra
  val phantom              = "1.12.2"

  val crossScala = Seq(scala, "2.10.5")

  lazy val scalaVersion = sys.props.get("scala-2.11") match {
    case Some(is) if is.nonEmpty && is.toBoolean => crossScala.head
    case crossBuildFor                           => crossScala.last
  }

  /* For `scalaBinaryVersion.value outside an sbt task. */
  lazy val scalaBinary = scalaVersion.dropRight(2)
}

object Libraries {

  val scalaAsync            = "org.scala-lang.modules"        %%  "scala-async"                           % Version.scalaAsync
  val slick                 = "com.typesafe.slick"            %%  "slick"                                 % Version.slick
  val scalaz                = "org.scalaz"                    %% "scalaz-core"                            % Version.scalaz
  val slickDrivers          = "com.arcusys.slick"             %%  "slick-drivers"                         % Version.slickDrivers
  val json4s                = "org.json4s"                    %%  "json4s-native"                         % Version.json4s
  val json4sJackson         = "org.json4s"                    %%  "json4s-jackson"                        % Version.json4s
  val json4sExt             = "org.json4s"                    %%  "json4s-ext"                            % Version.json4s
  val scalatest             = "org.scalatest"                 %%  "scalatest"                             % Version.scalatest
  val scalacheck            = "org.scalacheck"                %%  "scalacheck"                            % Version.scalacheck
  val scalaGuice            = "net.codingwell"                %%  "scala-guice"                           % Version.scalaGuice
  val config                = "com.typesafe"                  %   "config"                                % Version.config
  val guiceMultibindings    = "com.google.inject.extensions"  %   "guice-multibindings"                   % Version.guice
  val commonsIO             = "commons-io"                    %   "commons-io"                            % Version.commonsIO
  val slf4j                 = "org.slf4j"                     %   "slf4j-api"                             % Version.slf4j
  val log4jExt              = "log4j"                         %   "apache-log4j-extras"                   % Version.log4jExt
  val logback               = "ch.qos.logback"                %   "logback-classic"                       % Version.logback
  val jodaTime              = "joda-time"                     %   "joda-time"                             % Version.jodaTime
  val jodaConvert           = "org.joda"                      %   "joda-convert"                          % Version.jodaConvert
  val commonsValidator      = "commons-validator"             %   "commons-validator"                     % Version.commonsValidator
  val commonsLang           = "commons-lang"                  %   "commons-lang"                          % Version.commonsLang

  // Web
  val httpClient            = "org.apache.httpcomponents"     %   "httpclient"                            % Version.httpClient
  val httpMime              = "org.apache.httpcomponents"     %   "httpmime"                              % Version.httpMime
  val guiceServlet          = "com.google.inject.extensions"  %   "guice-servlet"                         % Version.guice
  val javaxServlet          = "javax.servlet"                 %   "javax.servlet-api"                     % Version.servletApi
  val portlet               = "javax.portlet"                 %   "portlet-api"                           % Version.portlet
  val websocket             = "javax.websocket"               %   "javax.websocket-api"                   % Version.websocket
  val fileUpload            = "commons-fileupload"            %   "commons-fileupload"                    % Version.fileUpload
  val mockito               = "org.mockito"                   %   "mockito-all"                           % Version.mockito

  // Liferay
  val liferayPortal62       = "com.liferay.portal"            %   "portal-service"                        % Version.liferayPortal62
  val liferayPortalImpl62   = "com.liferay.portal"            %   "portal-impl"                           % Version.liferayPortal62

  //OAuth 1.0 Provider & Consumer Library
  val oauthCore             = "net.oauth.core"                %   "oauth"                                 % Version.oauth
  val oauthConsumer         = "net.oauth.core"                %   "oauth-consumer"                        % Version.oauth
  val oauthProvider         = "net.oauth.core"                %   "oauth-provider"                        % Version.oauth

  // Db
  val h2                    = "com.h2database"                %   "h2"                                    % Version.h2
  val hsql                  = "com.danidemi.jlubricant"       %   "jlubricant-embeddable-hsql"            % Version.hsql
  val mysql                 = "mysql"                         %   "mysql-connector-java"                  % Version.mysql
  val postgres              = "org.postgresql"                %   "postgresql"                            % Version.postgres

  // Valamis
  val valamisUtils          = "com.arcusys.valamis"           %%  "arcusys-util"                          % Version.valamis
  val arcusysJson4s         = "com.arcusys.valamis"           %%  "arcusys-json4s"                        % Version.valamis
  val slickMigration        = "com.arcusys.slick"             %%  "slick-migration"                       % Version.slickMigrations

  // Spark
  val sparkCore             = "org.apache.spark"              %% "spark-core"                             % Version.spark % Provided
  val sparkStreaming        = "org.apache.spark"              %% "spark-streaming"                        % Version.spark % Provided
  val sparkSql              = "org.apache.spark"              %% "spark-sql"                              % Version.spark % Provided
  val sparkCatalyst         = "org.apache.spark"              %% "spark-catalyst"                         % Version.spark % Provided
  val sparkHive             = "org.apache.spark"              %% "spark-hive"                             % Version.spark % Provided
  val sparkDateTime         = "org.sparklinedata"             %% "spark-datetime"                         % Version.sparkDateTime
  val kafkaStreaming        = "org.apache.spark"              %% "spark-streaming-kafka"                  % Version.spark

  val kafka                 = "org.apache.kafka"              %% "kafka"                                  % Version.kafka
  val kafkaClient           = "org.apache.kafka"              %  "kafka-clients"                          % Version.kafka
  val zookeeper             = "org.apache.zookeeper"          %  "zookeeper"                              % Version.zookeeper

  val metrics               = "nl.grons"                      %% "metrics-scala"                          % Version.metricsScala
  val metricsServlet        = "io.dropwizard.metrics"         % "metrics-servlets"                        % Version.metrics

  // Phantom Cassandra
//  val phantomDsl            = "com.websudos"                  %%  "phantom-dsl"                           % Version.phantom
//  val phantomTestkit        = "com.websudos"                  %%  "phantom-testkit"                       % Version.phantom
//  val phantomConnectors     = "com.websudos"                  %%  "phantom-connectors"                    % Version.phantom
}


object Dependencies {
  import Libraries._

  private val reducedCommonsValidator =
    commonsValidator
      .exclude("commons-beanutils",   "commons-beanutils-core")
      .exclude("commons-collections", "commons-collections"   )

  val testSet = Seq(
    mockito    % Test,
    scalatest  % Test,
    scalacheck % Test
  )

  val testCluster = Seq (
    kafka,
    zookeeper
  )

  val testWeb = Seq(
    httpClient % Test,
    httpMime   % Test
  )

  val testDbSet = Seq(
    config   % Test,
    h2       % Test,
    hsql     % Test,
    mysql    % Test,
    postgres % Test
  ) ++ testSet

  // TODO: Uncomment when upload code to repository
  val jsonSet = Seq(
//    json4s,
//    json4sJackson,
    json4sExt,
    arcusysJson4s
  )

  val slickSet = Seq(
    slick,
    slickMigration,
    slickDrivers
  )

  val joda = Seq(
    jodaTime,
    jodaConvert
  )

  val common = Seq(
    scalaz,
    scalaAsync,
    logback,
    slf4j,
    metrics
  ) ++ testSet ++ joda

  val guice = Seq(
    guiceMultibindings,
    scalaGuice
  )

  val baseWeb = Seq(
    fileUpload,
    guiceServlet,
    javaxServlet % Provided
  )

  val tincan = Seq(
    reducedCommonsValidator,
    commonsLang,
    valamisUtils % Test
  ) ++ jsonSet ++ testDbSet

  val util = guice :+ valamisUtils

  val database = slickSet ++ jsonSet ++ testDbSet ++ util :+ reducedCommonsValidator

  val web = Seq(
    commonsIO,
    websocket,
    portlet % Provided,
    oauthCore,
    oauthConsumer,
    oauthProvider,
    kafkaClient,
    metricsServlet,
    log4jExt
  ) ++ guice ++ baseWeb ++  jsonSet ++ testDbSet ++ testWeb

  val api = baseWeb ++ testWeb ++ Seq(httpMime, httpClient)

  val spark = Seq(sparkSql, sparkStreaming,
    kafkaStreaming
      .exclude("org.spark-project.spark", "unused")
      .exclude("org.apache.spark", "spark")
      .exclude("org.apache.spark", "spark-streaming-kafka"),
    mysql, postgres, h2)

//  val cassandra = Seq(
//    reducedCommonsValidator,
//    phantomDsl,
//    phantomConnectors,
//    phantomTestkit % Test)

  val liferay62 = Seq(liferayPortal62, liferayPortalImpl62).map { _ % Provided } ++ web ++ util
}
