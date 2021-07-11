name := "tips"

version := "0.1"

scalaVersion := "2.13.6"

val zioVersion = "1.0.9"

libraryDependencies += "dev.zio"  %% "zio"   % zioVersion
libraryDependencies += "dev.zio"  %% "zio-streams"   % zioVersion
libraryDependencies += "dev.zio"  %% "zio-macros"   % zioVersion

libraryDependencies += "dev.zio"  %% "zio-json"   % "0.1.5"
libraryDependencies += "io.d11"   %% "zhttp"      % "1.0.0.0-RC17"
libraryDependencies += "io.github.kitlangton"  %% "zio-magic"   % "0.3.5"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-fs2",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-refined"
).map(_ % "0.13.0")

scalacOptions += "-Ymacro-annotations"


/**
 * Test deps
 */

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test"          % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt"      % zioVersion % "test",
  "dev.zio" %% "zio-test-magnolia" % zioVersion % "test" // optional
)
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
