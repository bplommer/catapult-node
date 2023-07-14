// https://typelevel.org/sbt-typelevel/faq.html#what-is-a-base-version-anyway
ThisBuild / tlBaseVersion := "0.0" // your current series x.y

ThisBuild / organization := "org.typelevel"
ThisBuild / organizationName := "Typelevel"
ThisBuild / startYear := Some(2022)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  // your GitHub handle and name
  tlGitHubDev("bplommer", "Ben Plommer"),
  tlGitHubDev("averymcnab", "Avery McNab"),
)

// publish to s01.oss.sonatype.org (set to true to publish to oss.sonatype.org instead)
ThisBuild / tlSonatypeUseLegacyHost := false

// publish website from this branch
ThisBuild / tlSitePublishBranch := Some("main")

val Scala213 = "2.13.11"
ThisBuild / crossScalaVersions := Seq(Scala213, "3.3.0")
ThisBuild / scalaVersion := Scala213 // the default Scala

lazy val root = tlCrossRootProject.aggregate(core, testkit)

lazy val testkit = crossProject(JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("testkit"))
  .settings(
    name := "catapult-testkit",
    libraryDependencies ++= Seq(
      "com.disneystreaming" %%% "weaver-cats" % "0.8.3" % Test,
    ),
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
  )
  .dependsOn(core)

lazy val core = crossProject(JSPlatform)
  .crossType(CrossType.Pure)
  .enablePlugins(ScalablyTypedConverterGenSourcePlugin)
  .in(file("core"))
  .settings(
    name := "catapult",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % "2.9.0",
      "org.typelevel" %%% "cats-effect" % "3.5.0",
      "co.fs2" %%% "fs2-core" % "3.7.0",
    ),
    libraryDependencies ++= Seq(
      "com.disneystreaming" %% "weaver-cats" % "0.8.3" % Test,
    ),
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
  )
  .jsSettings(
//    externalNpm := baseDirectory.value / ".." / "..",
    stOutputPackage := "facade",
    Compile / npmDependencies ++= Seq(
      "launchdarkly-node-server-sdk" -> "7.0.2"
    ),
  )

lazy val docs = project.in(file("site")).enablePlugins(TypelevelSitePlugin)

ThisBuild / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
