name := "vehicle-tracker"

version := "0.1"

scalaVersion := "2.12.7"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "lightshed-maven" at "http://dl.bintray.com/content/lightshed/maven"
)

val httpDependencies = Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.9"
)

val loggingDependencies = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

val configurationDependencies = Seq( 
  "com.typesafe" % "config" % "1.3.0",
  "com.iheart" %% "ficus" % "1.4.3"
)

val testDependencies = Seq(
  "org.scalatest"  %% "scalatest"    % "3.0.3",
  "org.mockito"    %  "mockito-core" % "1.10.19" ,
  "org.scalacheck" %% "scalacheck"   % "1.13.5" ,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9"
).map (_ % "test")

val jsonDependencies = Seq(
  "io.spray" %%  "spray-json" % "1.3.2"
)

libraryDependencies ++=
    httpDependencies ++ 
      loggingDependencies ++
      configurationDependencies ++
      testDependencies ++
      jsonDependencies
