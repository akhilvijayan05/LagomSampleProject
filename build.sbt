organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
val kafkaStream = "org.apache.kafka" % "kafka-streams" % "1.0.1"
val embeddedKafka = "net.manub" %% "scalatest-embedded-kafka" % "1.1.0" % "test"

lazy val `lagom-sample-project` = (project in file("."))
  .aggregate(`sample-example-api`, `sample-example-impl`, `models`)

lazy val `sample-example-api` = (project in file("sample-example-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
  .dependsOn(`models`)

lazy val `sample-example-impl` = (project in file("sample-example-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      kafkaStream,
      embeddedKafka
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`sample-example-api`, `models`)

lazy val `models` = (project in file("models"))
  .settings( libraryDependencies +="com.typesafe.play" %% "play-json" % "2.6.7")

lagomCassandraEnabled in ThisBuild := false
lagomUnmanagedServices in ThisBuild := Map("cas_native" -> "http://localhost:9042")

lagomKafkaEnabled in ThisBuild := false
lagomKafkaAddress in ThisBuild := "localhost:9092"