name := """play-imgur"""
organization := "com.rexardiente"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  ws,
  guice,
  "commons-io" % "commons-io" % "2.6",
  "commons-codec"           %  "commons-codec"          % "1.12",
  "com.typesafe.akka" 		 	%% "akka-actor"							% "2.5.17",
  "com.typesafe.play" 			%% "play-json" 							% "2.7.0-M1",
  "com.h2database" 					%  "h2" 										% "1.4.192",
  "com.typesafe.slick" 			%% "slick" 									% "3.2.1",
  "com.typesafe.play" 			%% "play-slick" 						% "3.0.3",
  "com.typesafe.play" 			%% "play-slick-evolutions"	% "3.0.3",
  "com.github.tminglei" 		%% "slick-pg" 							% "0.15.2",
  "org.scalatestplus.play"	%% "scalatestplus-play" 		% "4.0.1"			% Test
)

resolvers += Resolver.sonatypeRepo("releases")
// overrides the update, updateClassifiers, and updateSbtClassifiers tasks.
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.1.0-M11")

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
play.sbt.routes.RoutesKeys.routesImport += "java.util.UUID"
