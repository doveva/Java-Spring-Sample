package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.*

object DeployTest : BuildType({
    name = "Deploy Test"
    description = "First Deploy for java project"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "Gradle Build"
            id = "__NEW_RUNNER__"
            tasks = "bootJar"
        }
    }
})
