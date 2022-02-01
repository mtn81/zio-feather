ThisBuild / organization       := "com.github.mtn81"
ThisBuild / version            := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion       := "3.1.0"
ThisBuild / run / fork         := true
ThisBuild / run / connectInput := true
ThisBuild / resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-feather",
    libraryDependencies ++= zioDeps,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val docs = project
  .in(file("docs"))
  .dependsOn(root)
  .enablePlugins(MdocPlugin)
  .settings(
    name := "docs",
    mdocVariables := Map(
      "VERSION" -> version.value
    ),
    mdocIn  := file("src"),
    mdocOut := file("target/mdoc")
  )

// dependendies
lazy val zioVer = "1.0.13"
lazy val zioDeps = Seq(
  "dev.zio" %% "zio"               % zioVer,
  "dev.zio" %% "zio-streams"       % zioVer,
  "dev.zio" %% "zio-test"          % zioVer % "test",
  "dev.zio" %% "zio-test-sbt"      % zioVer % "test",
  "dev.zio" %% "zio-test-magnolia" % zioVer % "test"
)
