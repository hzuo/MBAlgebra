name := "MBAlgebra"

scalaVersion := "2.11.1"

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature" )


resolvers += "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"


libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.6"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.11.5"

