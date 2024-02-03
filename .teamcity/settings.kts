package TestJava.buildTypes

import jetbrains.buildServer.configs.kotlin.*

object TestJava_DeployTest : BuildType({
    name = "Deploy Test"
    description = "First Deploy for java project"

    vcs {
        root(TestJava.vcsRoots.TestJava_GitHub)
    }

    steps {
        gradle {
            name = "Gradle build"
            id = "gradle_runner"
            tasks = "bootJar"
        }
    }
})
