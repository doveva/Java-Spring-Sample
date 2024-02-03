import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.DockerCommandStep
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.11"

project {
    description = "Test Java pipeline"
    sequential{
        buildType(Build)
        parallel {
            buildType(TestFront)
            buildType(BackendTest)
        }
        buildType(Deploy)
    }
    template(Docker)
}

object Build : BuildType({
    name = "Build Test"
    description = "First Build config for java project"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            id = "gradle_runner"
            tasks = "bootJar"
        }
    }
})

object TestFront: BuildType({
    name = "Frontend test"
    vcs {
        root(DslContext.settingsRoot)
    }
    steps{
        script {
            id = "Frontend Test"
            scriptContent = """echo "Frontend has been tested!""""
        }
    }
})

object BackendTest: BuildType({
    name = "Backend test"
    vcs {
        root(DslContext.settingsRoot)
    }
    steps{
        script {
            id = "Backend Test"
            scriptContent = """echo "Backend has been tested!""""
        }
    }
})

object Deploy: BuildType({
    name = "Deploy"
    params {
        param("TestVar", "true")
    }
    vcs {
        root(DslContext.settingsRoot)
    }
    steps{
        if ("true" == "%TestVar%"){
            script {
                id = "script run"
                scriptContent = """echo "Runner True step!""""
            }
        }
        else{
            script {
                id = "script run"
                scriptContent = """echo "Runner False step!""""
            }
        }
    }
    triggers{
        vcs {
        }
    }
})

object Docker : Template({
    name = "Docker"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dockerCommand {
            name = "Build"
            id = "Build"
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
                platform = DockerCommandStep.ImagePlatform.Linux
                namesAndTags = "springtest:latest"
                commandArgs = "--pull"
            }
        }
    }
})
